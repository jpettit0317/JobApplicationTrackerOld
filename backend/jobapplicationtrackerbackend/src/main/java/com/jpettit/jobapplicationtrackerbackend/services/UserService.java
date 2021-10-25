package com.jpettit.jobapplicationtrackerbackend.services;

import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.models.*;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    private UserDAO userDAO;
    private final Integer strength;

    public UserService(UserDAO newUserDAO, Integer strength) {
        this.userDAO = newUserDAO;
        this.strength = strength;
    }

    private String printUserDAO(UserDAO dao) {
        return String.format("UserDAO is %s", dao.toString());
    }

    public UserServiceUserPair getUserById(Long id) {
        final Optional<User> user = userDAO.getById(id);

        if (!user.isPresent()) {
            System.out.println("User is not present");
            return UserServiceUserPair.createPair(Optional.empty(), "Couldn't find user.");
        }

        return UserServiceUserPair.createPair(user, "");
    }

    public UserServiceIntPair createUser(User user) {
        final Optional<User> duplicateUser = userDAO.getByUsername(user.getUsername());

        if (duplicateUser.isPresent()) {
            return UserServiceIntPair.createPair(0, "User exists");
        }
        final String hashedPassword = hashPassword(user.getPassword());
        final Session session = Session.createSessionWithExpDateTomorrow(UUID.randomUUID().toString());
        final User newUser = User.createUserFromUserPasswordSession(user, hashedPassword, session);

        final int recordsInserted = userDAO.insertOne(newUser);

        return UserServiceIntPair.createPair(recordsInserted, "");
    }

    public UserServiceResultPair<String> validateUserLogin(Login login) {
        final UserServiceResultPair<String> PASSWORD = userDAO.getPasswordForUser(login);

        if (!PASSWORD.getMessage().equals("")) {
            return new UserServiceResultPair<>("", PASSWORD.getMessage());
        }

        if (!comparePassword(login.getPassword(), PASSWORD.getValue())) {
            return new UserServiceResultPair<>("", "Username or password doesn't match");
        }
        final Session NEW_SESSION = Session.createSessionWithExpDateTomorrow(UUID.randomUUID().toString());

        final Integer RESULT = userDAO.updateSession(login.getUsername(), NEW_SESSION);

        if (RESULT == 1) {
            return new UserServiceResultPair<>(NEW_SESSION.getSessionName(), "");
        } else {
            return new UserServiceResultPair<>("", "Couldn't update session");
        }
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(this.strength);

        return passwordEncoder.encode(password);
    }

    private boolean comparePassword(String rawPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(this.strength);

        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}

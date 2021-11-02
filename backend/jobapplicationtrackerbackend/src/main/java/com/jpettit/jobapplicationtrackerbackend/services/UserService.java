package com.jpettit.jobapplicationtrackerbackend.services;

import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.PasswordEncoder;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.models.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    public final String SESSION_NOT_FOUND = "Can't find session with that id.";

    public UserService(UserDAO newUserDAO, PasswordEncoder encoder) {
        this.userDAO = newUserDAO;
        this.passwordEncoder = encoder;
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

    public UserServiceResultPair<String> createUser(User user) {
        final Optional<User> duplicateUser = userDAO.getByUsername(user.getUsername());

        if (duplicateUser.isPresent()) {
            return new UserServiceResultPair<>("", "User exists");
        }

        final String hashedPassword = hashPassword(user.getPassword());
        final String sessionName = UUID.randomUUID().toString();
        final LocalDate nextDay = LocalDate.now().plusDays(1);

        final User newUser = createNewUser(user, hashedPassword, sessionName, nextDay);

        final int recordsInserted = userDAO.insertOne(newUser);

        if (recordsInserted == 1) {
            return new UserServiceResultPair<>(sessionName, "");
        } else {
            return new UserServiceResultPair<>("", "Couldn't create user");
        }
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

    public ResultPair<String> getUsernameBySessionId(String sessionId) {
        return userDAO.getUsernameBySessionId(sessionId);
    }

    public ResultPair<Boolean> hasSessionExpired(final LocalDate ACCESS_DATE, final String SESSION_ID) {
        final ResultPair<Optional<LocalDate>> PAIR = userDAO.getSessionExpDateBySessionId(SESSION_ID);
        System.out.println("In session has exipred Date is " + PAIR.toString());

        if (!PAIR.getValue().isPresent()) {
            return new ResultPair<>(true, SESSION_NOT_FOUND);
        }

        final LocalDate EXP_DATE = PAIR.getValue().get();
        final boolean RESULT = ACCESS_DATE.isAfter(EXP_DATE);


        return new ResultPair<>(RESULT, "");
    }

    private String hashPassword(String password) {
        return passwordEncoder.hashPassword(password);
    }

    private boolean comparePassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.comparePassword(rawPassword, hashedPassword);
    }

    private User createNewUser(User user, String hashedPassword, String sessionName, LocalDate expDate) {
        final Session NEW_SESSION = Session.createSession(sessionName, expDate);
        return User.createUserFromUserPasswordSession(user, hashedPassword, NEW_SESSION);
    }
}

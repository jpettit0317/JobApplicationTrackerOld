package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.interfaces.DAO;
import com.jpettit.jobapplicationtrackerbackend.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class UserDAO implements DAO<User> {
    @Autowired
    private UserDaoInfoBuilder builder;

    @Value(AppProperties.appEnv)
    String env;

    public static final String NONEXISTANT_SESSIONID = "Session id doesn't exist";
    public static final String EMPTY_SESSIONID = "Empty sessionId";
    public static final String USERNAME_NOT_FOUND = "Couldn't find username";

    public UserDAO() {
        final ProjectEnvironment ENV = ProjectEnvironmentReader.getEnvironment(env);
        this.builder = new UserDaoInfoBuilder(env);
    }

    public Optional<User> getByUsername(final String USERNAME) {
        try {
            return builder.getByUsername(USERNAME);
        } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
            sqlException.printStackTrace();
            return Optional.empty();
        }
    }

    public ResultPair<String> getPasswordForUser(Login login) {
        try {
            return builder.getPasswordByUsername(login);
        } catch(SQLException exception) {
            System.out.println(String.format("Error: %s", exception.getMessage()));
            exception.printStackTrace();
            return new ResultPair<>("", exception.getMessage());
        }
    }

    public ResultPair<String> getUsernameBySessionId(String sessionId) {
        if (sessionId.equals("")) {
            return new ResultPair<>("", EMPTY_SESSIONID);
        }
        try {
            return builder.getUsernameBySessionId(sessionId);
        } catch (SQLException exception) {
            return new ResultPair<>("", exception.getMessage());
        }
    }

    public ResultPair<Optional<LocalDate>> getSessionExpDateBySessionId(final String SESSION_ID) {
        if (SESSION_ID.equals("")) {
            return new ResultPair<>(Optional.empty(), EMPTY_SESSIONID);
        }
        try {
            return builder.getSessionExpDateBySessionId(SESSION_ID);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResultPair<>(Optional.empty(), e.getMessage());
        }
    }

    public int updateSession(String username, Session newSession) {
        try {
            return builder.updateSession(username, newSession);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    public int insertOne(final User NEW_USER) {
        try {
            return builder.insertOne(NEW_USER);
        } catch (SQLException sqlException) {
            final String ERROR = String.format("Error: %s", sqlException.getMessage());
            sqlException.printStackTrace();
            return 0;
        }
    }

    public User update(User t) {
        return User.createEmptyUser();
    }

    public User delete(User t) {
        return User.createEmptyUser();
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                ", builder=" + builder +
                '}';
    }
}

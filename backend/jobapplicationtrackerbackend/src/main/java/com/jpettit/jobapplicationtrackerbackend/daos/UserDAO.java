package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.enums.UserFields;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserQuerier;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.models.UserServiceResultPair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class UserDAO implements DAO<User> {
    private final Connection jobAppConnection;
    private final ProjectEnvironment environment;

    public UserDAO(Connection connection, ProjectEnvironment environment) {
        this.jobAppConnection = connection;
        this.environment = environment;
    }

    public Optional<User> getById(Long id) {
        try {
            final UserQuerier querier = new UserQuerier(environment);
            final String query = querier.buildGetUserByIdQuery(id);
            Statement statement = jobAppConnection.createStatement();
            ResultSet set = statement.executeQuery(query);

            final Optional<User> user = buildOptionalUser(set);

            return user;
        } catch(SQLException execption) {
            System.out.println("Error: " + execption.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> getByUsername(String userName) {
        try {
            final UserQuerier querier = new UserQuerier(environment);
            final String query = querier.buildGetUserByUsername(userName);
            Statement statement = jobAppConnection.createStatement();
            ResultSet set = statement.executeQuery(query);

            final Optional<User> user = buildOptionalUserNoPassword(set);

            return user;
        } catch(SQLException execption) {
            System.out.println("Error: " + execption.getMessage());
            return Optional.empty();
        }
    }

    public UserServiceResultPair<String> getPasswordForUser(Login login) {
        try {
            final UserQuerier QUERIER = new UserQuerier(environment);
            final String QUERY = QUERIER.buildGetPasswordForUserQuery(login.getUsername());
            Statement statement = jobAppConnection.createStatement();
            ResultSet set = statement.executeQuery(QUERY);

            return new UserServiceResultPair<>(buildPassword(set), "");
        } catch(SQLException exception) {
            System.out.println(String.format("Error: %s", exception.getMessage()));
            exception.printStackTrace();
            return new UserServiceResultPair<>("", exception.getMessage());
        }
    }

    private String buildPassword(ResultSet set) {
        try {
            if (!set.next()) {
                return "";
            }

            final String PASSWORD = set.getString(UserFields.password);
            return PASSWORD;
        } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
            sqlException.printStackTrace();
            return "";
        }
    }

    private Optional<User> buildOptionalUser(ResultSet set) {
        try {
            if (!set.next()) {
                return Optional.empty();
            }

            final Long userId = set.getLong(UserFields.userId);
            final String userName = set.getString(UserFields.userName);
            final String email = set.getString(UserFields.email);
            final String password = set.getString(UserFields.password);
            final String sessionName = set.getString(UserFields.sessionName);
            final LocalDate expDate = set.getDate(UserFields.expDate).toLocalDate();

            final Session userSession = Session.createSession(sessionName, expDate);

            return Optional.of(User.createUser(userName, email, password, userSession, userId));
        } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
            return Optional.empty();
        }
    }

    private Optional<User> buildOptionalUserNoPassword(ResultSet set) {
        try {
            if (!set.next()) {
                return Optional.empty();
            }

            final Long userId = set.getLong(UserFields.userId);
            final String userName = set.getString(UserFields.userName);
            final String email = set.getString(UserFields.email);
            final String sessionName = set.getString(UserFields.sessionName);
            final LocalDate expDate = set.getDate(UserFields.expDate).toLocalDate();

            final Session userSession = Session.createSession(sessionName, expDate);

            return Optional.of(User.createUser(userName, email, "", userSession, userId));
        } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
            return Optional.empty();
        }
    }

    private ArrayList<User> buildUsers() {
            ArrayList<User> users = new ArrayList<>();
            try {
                UserQuerier querier = new UserQuerier(environment);
                final String query = querier.buildGetAllUsersQuery();
                Statement statement = jobAppConnection.createStatement();
                ResultSet set = statement.executeQuery(query);

                while(set.next()) {
                    final User user = buildUser(set);
                    users.add(user);
                }
                return users;
            } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
            return new ArrayList<>();
        }
    }

    public User buildUser(ResultSet set) throws SQLException {
        final Long userId = set.getLong(UserFields.userId);
        final String userName = set.getString(UserFields.userName);
        final String email = set.getString(UserFields.email);
        final String password = set.getString(UserFields.password);
        final String sessionName = set.getString(UserFields.sessionName);
        final LocalDate expDate = set.getDate(UserFields.expDate).toLocalDate();

        final Session userSession = Session.createSession(sessionName, expDate);

        return User.createUser(userName, email, password, userSession, userId);
    }

    private int insertUser(final PreparedStatement statement, final User user) {
        System.out.println("The user that is being inserted is " + user.toString());
        try {
            final Date sessionExpDate = convertLocalDateToDate(user.getSession().getExpirationDate());

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getSession().getSessionName());
            statement.setDate(5, sessionExpDate);

            statement.addBatch();

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private Date convertLocalDateToDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }

    public ArrayList<User> getAll() {
        return buildUsers();
    }

    public int updateSession(String username, Session newSession) {
        final UserQuerier QUERIER = new UserQuerier(environment);
        final String QUERY = QUERIER.buildUpdateSession();

        try {
           PreparedStatement statement = jobAppConnection.prepareStatement(QUERY);
           return updateSession(statement, username, newSession);
        } catch (SQLException e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
            e.printStackTrace();
            return 0;
        }
    }

    public int updateSession(PreparedStatement statement, String username, Session newSession) {
        final Date sessionExpDate = convertLocalDateToDate(newSession.getExpirationDate());

        try {
            statement.setString(1, newSession.getSessionName());
            statement.setDate(2, sessionExpDate);
            statement.setString(3, username);

            statement.addBatch();

            return statement.executeUpdate();
        } catch (SQLException sqlEx) {
            System.out.println(String.format("Error: %s", sqlEx.getMessage()));
            sqlEx.printStackTrace();
            return 0;
        }
    }

    public int insertOne(User t) {
        final UserQuerier querier = new UserQuerier(environment);
        final String query = querier.buildInsertOneUserQuery();

        try {
            PreparedStatement statement = jobAppConnection.prepareStatement(query);
            return insertUser(statement, t);
        } catch (SQLException sqlException) {
            System.out.println("Error " + sqlException.getMessage());
            return -1;
        }
    }

    public User update(User t) {
        return User.createEmptyUser();
    }

    public User delete(User t) {
        return User.createEmptyUser();
    }

    public int insertMany(Collection<User> users) {
        final UserQuerier querier = new UserQuerier(environment);
        final String query = querier.buildInsertOneUserQuery();
        try {
            final PreparedStatement statement = jobAppConnection.prepareStatement(query);
            return insertUsers(statement, users);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int insertUsers(final PreparedStatement statement, final Collection<User> users) {
        try {
            if (users.isEmpty()) {
                return 0;
            }
            for (User user : users) {
                final Date sessionExpDate = convertLocalDateToDate(user.getSession().getExpirationDate());

                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setString(4, user.getSession().getSessionName());
                statement.setDate(5, sessionExpDate);

                statement.addBatch();
            }

            return statement.executeBatch().length;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void closeConnection() throws SQLException {
        try {
            jobAppConnection.close();
        } catch(SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
        }
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                "jobAppConnection=" + jobAppConnection +
                ", environment=" + environment +
                '}';
    }
}

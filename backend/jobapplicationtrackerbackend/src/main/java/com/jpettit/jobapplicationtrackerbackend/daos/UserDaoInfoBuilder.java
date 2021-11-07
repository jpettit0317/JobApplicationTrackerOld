package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.enums.UserFields;
import com.jpettit.jobapplicationtrackerbackend.helpers.DateConverter;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserQuerier;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class UserDaoInfoBuilder {
    private final ProjectEnvironment environment;

    public static final String USERNAME_NOT_FOUND = "Username not found.";
    public static final String EMPTY_SESSIONID = "Empty sessionId";
    public static final String NONEXISTANT_SESSIONID = "Session id doesn't exist";

    public UserDaoInfoBuilder(@Value(AppProperties.appEnv) String env) {
        environment = ProjectEnvironmentReader.getEnvironment(env);
    }

    public Connection createConnection() {
        return JobAppTrackerConnection.createConnection().get();
    }

    public Optional<User> getByUsername(final String USERNAME) throws SQLException {
        if (USERNAME.equals("")) {
            return Optional.empty();
        }
        final Connection connection = createConnection();
        final UserQuerier querier = new UserQuerier(environment);
        final String query = querier.buildGetUserByUsername(USERNAME);
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(query);

        return buildUserWithoutPassword(set);
    }

    private Optional<User> buildUserWithoutPassword(final ResultSet SET) throws SQLException {
        if (!SET.next()) {
            return Optional.empty();
        }
        final String USERNAME = SET.getString(UserFields.userName);
        final String EMAIL = SET.getString(UserFields.email);
        final String SESSION_ID = SET.getString(UserFields.sessionName);
        final Long ID = SET.getLong(UserFields.userId);

        final Date SESSION_EXP_DATE = SET.getDate(UserFields.expDate);
        final LocalDate NEW_DATE = DateConverter.convertDateToLocalDate(SESSION_EXP_DATE);

        final Session SESSION = Session.createSession(SESSION_ID, NEW_DATE);

        return Optional.of(User.createUser(USERNAME, EMAIL, "", SESSION, ID));
    }

    public ResultPair<String> getPasswordByUsername(Login login) throws SQLException {
        final Connection connection = createConnection();
        final UserQuerier QUERIER = new UserQuerier(environment);
        final String QUERY = QUERIER.buildGetPasswordForUserQuery(login.getUsername());
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(QUERY);

        return buildGetPasswordByUsernameResult(set);
    }

    private ResultPair<String> buildGetPasswordByUsernameResult(ResultSet set) throws SQLException {
        if (!set.next()) {
            return new ResultPair<>("", USERNAME_NOT_FOUND);
        }

        final String PASSWORD = set.getString(UserFields.password);
        return new ResultPair<>(PASSWORD, "");
    }

    public ResultPair<String> getUsernameBySessionId(String sessionId) throws SQLException {
        if (sessionId.equals("")) {
            return new ResultPair<>("", EMPTY_SESSIONID);
        }
        final Connection connection = createConnection();
        final UserQuerier querier = new UserQuerier(environment);
        final String query = querier.buildGetUsernameBySessionIdQuery(sessionId);
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(query);

        final String USERNAME = buildUsername(set);

        return buildGetUsernameResultPair(USERNAME);
    }

    public ResultPair<Optional<LocalDate>> getSessionExpDateBySessionId(final String SESSION_ID) throws SQLException {
        if (SESSION_ID.equals("")) {
            return new ResultPair<>(Optional.empty(), EMPTY_SESSIONID);
        }
        final Connection connection = createConnection();
        final UserQuerier QUERIER = new UserQuerier(environment);
        final String QUERY = QUERIER.getSessionExpirationDateBySessionId(SESSION_ID);

        final Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(QUERY);
        return buildGetSessionExpDateBySessionId(set);
    }

    public int updateSession(final String USERNAME, final Session NEW_SESSION) throws SQLException {
        if (USERNAME.equals("") || !Optional.ofNullable(NEW_SESSION).isPresent()) {
            return 0;
        }
        final UserQuerier QUERIER = new UserQuerier(environment);
        final String QUERY = QUERIER.buildUpdateSession();
        final Connection connection = createConnection();
        PreparedStatement statement = connection.prepareStatement(QUERY);
        return updateSessionInDatabase(statement, USERNAME, NEW_SESSION);
    }

    public int insertOne(User t) throws SQLException {
        if (!Optional.ofNullable(t).isPresent()) {
            return 0;
        }
        final UserQuerier querier = new UserQuerier(environment);
        final String query = querier.buildInsertOneUserQuery();
        final Connection connection = createConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        return insertUser(statement, t);
    }

    private int insertUser(final PreparedStatement STATEMENT, final User USER) throws SQLException {
        final Session USER_SESSION = USER.getSession();
        final Date sessionExpDate = convertLocalDateToDate(USER_SESSION.getExpirationDate());

        STATEMENT.setString(1, USER.getUsername());
        STATEMENT.setString(2, USER.getEmail());
        STATEMENT.setString(3, USER.getPassword());
        STATEMENT.setString(4, USER.getSession().getSessionName());
        STATEMENT.setDate(5, sessionExpDate);

        STATEMENT.addBatch();

        return STATEMENT.executeUpdate();
    }

    private Date convertLocalDateToDate(final LocalDate DATE) {
        return DateConverter.convertLocalDateToDate(DATE);
    }

    private int updateSessionInDatabase(PreparedStatement statement, final String USERNAME, final Session NEW_SESSION) throws SQLException {
        final Date SESSION_EXP_DATE = Date.valueOf(NEW_SESSION.getExpirationDate());

        statement.setString(1, NEW_SESSION.getSessionName());
        statement.setDate(2, SESSION_EXP_DATE);
        statement.setString(3, USERNAME);

        statement.addBatch();

        return statement.executeUpdate();
    }

    private String buildUsername(ResultSet set) throws SQLException {
        if (!set.next()) {
            return "";
        }
        final String USERNAME = set.getString(UserFields.userName);
        return USERNAME;
    }

    private ResultPair<String> buildGetUsernameResultPair(String username) {
        if (username.equals("")) {
            return new ResultPair<>("", USERNAME_NOT_FOUND);
        } else {
            return new ResultPair<>(username, "");
        }
    }

    private ResultPair<Optional<LocalDate>> buildGetSessionExpDateBySessionId(ResultSet set) throws SQLException {
        if (!set.next()) {
            return new ResultPair<>(Optional.empty(), NONEXISTANT_SESSIONID);
        }
        final Date EXP_DATE = set.getDate(UserFields.expDate);
        final LocalDate EXP_DATE_LOCAL_DATE = DateConverter.convertDateToLocalDate(EXP_DATE);

        return new ResultPair<>(Optional.of(EXP_DATE_LOCAL_DATE), "");
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}

package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.models.*;

import helpers.TestPair;
import helpers.UserDAOTestHelpers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.mockito.*;
import static helpers.UserDAOTestHelpers.convertLocalDateToDate;
import static helpers.UserDAOTestHelpers.createDefaultTestUsers;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@SpringBootConfiguration
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "application.env=test"
})
class UserDAOTest {
    private static final ProjectEnvironment testEnvironment = ProjectEnvironment.TEST;
    private ArrayList<User> users;

    @Value(AppProperties.appEnv)
    String env;

    @InjectMocks
    private UserDAO sut;

    @Mock
    private UserDaoInfoBuilder builder;

    final String EXCEPTION_THROWN_ERR_MSG = "Exception thrown";

    @AfterEach
    private void afterAll() {
        destroyVars();
    }

    private void destroyVars() {
        destroyUsers();
        sut = null;
        builder = null;
    }

    private void destroyUsers() {
        if (Optional.ofNullable(users).isPresent()) {
            users.clear();
            users = null;
        }
    }

    // insertOne tests
    @Test
    public void testInsertOne_whenPassedInUser_shouldReturnOne() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final User USER = users.get(0);
        final Integer EXPECTED = 1;
        Mockito.when(builder.insertOne(USER)).thenReturn(EXPECTED);

        final Integer ACTUAL = sut.insertOne(USER);
        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).insertOne(USER);
    }

    @Test
    public void testInsertOne_whenPassedInUserThatIsNull_shouldReturnZero() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final Integer EXPECTED = 0;
        Mockito.when(builder.insertOne(null)).thenReturn(EXPECTED);

        final Integer ACTUAL = sut.insertOne(null);
        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).insertOne(null);
    }

    @Test
    public void testInsertOne_whenSQLExceptionIsThrown_shouldReturnZero() throws SQLException {
        final int EXPECTED = 0;
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final User USER = users.get(0);
        Mockito.when(builder.insertOne(USER)).thenThrow(new SQLException(EXCEPTION_THROWN_ERR_MSG));

        final int ACTUAL = sut.insertOne(USER);

        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).insertOne(USER);
    }
    // end of insertOne tests
    // getPasswordForUser tests
    @Test
    public void testGetPasswordForUser_WithValidLogin_ShouldReturnPasswordAndNoErrorMessage() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final User USER = users.get(0);
        final Login LOGIN = Login.createLogin(USER.getUsername(), USER.getPassword());
        final ResultPair<String> PAIR = new ResultPair<>(USER.getPassword(), "");

        Mockito.when(builder.getPasswordByUsername(LOGIN)).thenReturn(PAIR);

        final ResultPair<String> ACTUAL_PAIR = sut.getPasswordForUser(LOGIN);

        UserDAOTestHelpers.assertStringResultPairsAreEqual(ACTUAL_PAIR, PAIR);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).getPasswordByUsername(LOGIN);
    }

    @Test
    public void testGetPasswordForUser_WithNonExistantUser_ShouldReturnNoPasswordAndErrorMessage() throws SQLException {
        final User USER = UserDAOTestHelpers.nonExistantUser2;
        final Login LOGIN = Login.createLogin(USER.getUsername(), USER.getPassword());

        Mockito.when(builder.getPasswordByUsername(LOGIN))
                .thenReturn(new ResultPair<>("", EXCEPTION_THROWN_ERR_MSG));
        final ResultPair<String> ACTUAL_PAIR = sut.getPasswordForUser(LOGIN);

        UserDAOTestHelpers.verifyErrorStringResultPair(ACTUAL_PAIR, EXCEPTION_THROWN_ERR_MSG);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).getPasswordByUsername(LOGIN);
    }

    @Test
    public void testGetPasswordForUser_whenSQLExceptionIsThrown_shouldReturnEmptyValueAndExceptionThrownErrorMessage() throws SQLException {
        final User USER = UserDAOTestHelpers.nonExistantUser;
        final Login LOGIN = Login.createLogin(USER.getUsername(), USER.getPassword());

        Mockito.when(builder.getPasswordByUsername(LOGIN)).thenThrow(new SQLException(EXCEPTION_THROWN_ERR_MSG));

        final ResultPair<String> ACTUAL = sut.getPasswordForUser(LOGIN);

        UserDAOTestHelpers.verifyErrorStringResultPair(ACTUAL, EXCEPTION_THROWN_ERR_MSG);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).getPasswordByUsername(LOGIN);
    }
    // end of getPasswordForUser tests
    // getUsernameBySessionId tests
    @Test
    public void testGetUsernameBySessionId_whenPassingInAnEmptySessionId_shouldReturnResultPairWithNoValueAndEmptySessionId() {
        final String SESSION_ID = "";

        final ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);
        final ResultPair<String> EXPECTED_PAIR = new ResultPair<>("", sut.EMPTY_SESSIONID);

        UserDAOTestHelpers.assertResultPairAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
    }

    @Test
    public void testGetUsernameBySessionId_whenPassingInFirstId_shouldReturnResultPairWithU1AndNoErrorMessage() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final User USER = users.get(0);
        final String SESSION_ID = USER.getSession().getSessionName();
        final ResultPair<String> EXPECTED_PAIR = new ResultPair<>(USER.getUsername(), "");

        Mockito.when(builder.getUsernameBySessionId(SESSION_ID)).thenReturn(EXPECTED_PAIR);

        ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertResultPairAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).getUsernameBySessionId(SESSION_ID);
    }

    @Test
    public void testGetUsernameBySessionId_whenPassingInThirdId_shouldReturnResultPairWithNoUsernameAndErrorMessageAsSessionIdDoesntExist() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();

        final String SESSION_ID = "third";
        ResultPair<String> EXPECTED_PAIR = new ResultPair<>("", sut.USERNAME_NOT_FOUND);
        Mockito.when(builder.getUsernameBySessionId(SESSION_ID)).thenReturn(EXPECTED_PAIR);

        ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertResultPairAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).getUsernameBySessionId(SESSION_ID);
    }

    @Test
    public void testGetUsernameBySessionId_whenSQLExceptionIsThrown_shouldReturnResultPairWithNoUsernameAndErrorMessage() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final String SESSION_ID = "third";
        final SQLException SQL_EXECPTION = new SQLException(EXCEPTION_THROWN_ERR_MSG);

        Mockito.when(builder.getUsernameBySessionId(SESSION_ID)).thenThrow(SQL_EXECPTION);

        ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);
        UserDAOTestHelpers.verifyErrorStringResultPair(ACTUAL_PAIR, EXCEPTION_THROWN_ERR_MSG);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).getUsernameBySessionId(SESSION_ID);
    }

    // end of getUsernameBySessionId tests
    // getSessionExpDateBySessionId tests
    @Test
    public void testGetSessionExpDateBySessionId_whenPassingInFirstSessionId_shouldReturnJan12000() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();

        final Session SESSION = users.get(0).getSession();
        final LocalDate EXP_DATE = SESSION.getExpirationDate();

        final ResultPair<Optional<LocalDate>> EXPECTED_PAIR = UserDAOTestHelpers.getExpectedPair(EXP_DATE, "");

        Mockito.when(builder.getSessionExpDateBySessionId(SESSION.getSessionName())).thenReturn(EXPECTED_PAIR);

        final ResultPair<Optional<LocalDate>> ACTUAL_PAIR = sut.getSessionExpDateBySessionId(SESSION.getSessionName());

        UserDAOTestHelpers.assertLocalDateResultPair(ACTUAL_PAIR, EXPECTED_PAIR);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT))
                .getSessionExpDateBySessionId(SESSION.getSessionName());
    }

    @Test
    public void testGetSessionExpDateBySessionId_whenPassingInThirdSessionId_shouldReturnEmptyOptional() throws SQLException {
        final String SESSION_ID = "third";

        final ResultPair<Optional<LocalDate>> EXPECTED = new ResultPair<>(Optional.empty(), sut.NONEXISTANT_SESSIONID);

        Mockito.when(builder.getSessionExpDateBySessionId(SESSION_ID)).thenReturn(EXPECTED);
        final ResultPair<Optional<LocalDate>> ACTUAL = sut.getSessionExpDateBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertLocalDateResultPair(ACTUAL, EXPECTED);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT))
                .getSessionExpDateBySessionId(SESSION_ID);
    }

    @Test
    public void testGetSessionExpDateBySessionId_whenPassingInEmptyString_shouldReturnEmptyDateAndSessionIdDoesntExist() {
        final String SESSION_ID = "";

        final ResultPair<Optional<LocalDate>> EXPECTED = new ResultPair<>(Optional.empty(), sut.EMPTY_SESSIONID);
        final ResultPair<Optional<LocalDate>> ACTUAL = sut.getSessionExpDateBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertLocalDateResultPair(ACTUAL, EXPECTED);
    }

    @Test
    public void testGetSessionExpDateBySessionId_whenSQLExceptionIsThrown_shouldReturnEmptyOptionalAndErrorMessage() throws SQLException {
        final String SESSION_ID = "third";
        final SQLException EXCEPTION = new SQLException(EXCEPTION_THROWN_ERR_MSG);

        Mockito.when(builder.getSessionExpDateBySessionId(SESSION_ID)).thenThrow(EXCEPTION);

        final ResultPair<Optional<LocalDate>> ACTUAL = sut.getSessionExpDateBySessionId(SESSION_ID);

        UserDAOTestHelpers.verifyErrorOptionalLocalDateResultPair(ACTUAL, EXCEPTION_THROWN_ERR_MSG);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT))
                .getSessionExpDateBySessionId(SESSION_ID);
    }
    // end of getSessionExpDate tests
    // updateSession tests
    @Test
    public void testUpdateSession_whenGivenValidSession_shouldReturnOne() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final String USERNAME = users.get(0).getUsername();
        final Session SESSION = users.get(0).getSession();

        Mockito.when(builder.updateSession(USERNAME, SESSION)).thenReturn(1);

        final int ACTUAL = sut.updateSession(USERNAME, SESSION);
        final int EXPECTED = 1;

        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
        Mockito.verify(builder, Mockito.times(UserDAOTestHelpers.CALL_COUNT)).updateSession(USERNAME, SESSION);
    }

    @Test
    public void testUpdateSession_whenGivenInvalidSession_shouldReturnZero() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final String USERNAME = users.get(0).getUsername();
        final Session SESSION = users.get(0).getSession();

        Mockito.when(builder.updateSession(USERNAME, SESSION)).thenReturn(0);

        final int ACTUAL = sut.updateSession(USERNAME, SESSION);
        final int EXPECTED = 0;
        final int EXPECTED_CALL_COUNT = 1;

        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
        Mockito.verify(builder, Mockito.times(EXPECTED_CALL_COUNT)).updateSession(USERNAME, SESSION);
    }

    @Test
    public void testUpdateSession_whenSQLExceptionIsThrown_shouldReturnZero() throws SQLException {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final String USERNAME = users.get(0).getUsername();
        final Session SESSION = users.get(0).getSession();
        final SQLException EXCEPTION = new SQLException(EXCEPTION_THROWN_ERR_MSG);

        Mockito.when(builder.updateSession(USERNAME, SESSION)).thenThrow(EXCEPTION);

        final int ACTUAL = sut.updateSession(USERNAME, SESSION);
        final int EXPECTED = 0;
        final int EXPECTED_CALL_COUNT = 1;

        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
        Mockito.verify(builder, Mockito.times(EXPECTED_CALL_COUNT)).updateSession(USERNAME, SESSION);
    }

    @Test
    public void testUpdateSession_whenPassedInEmptyUsername_shouldReturnZero() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final String USERNAME = "";
        final Session SESSION = users.get(0).getSession();

        final int ACTUAL = sut.updateSession(USERNAME, SESSION);
        final int EXPECTED = 0;

        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
    }

    @Test
    public void testUpdateSession_whenPassedInNullSession_shouldReturnZero() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        final String USERNAME = users.get(0).getUsername();
        final Session SESSION = null;

        final int ACTUAL = sut.updateSession(USERNAME, SESSION);
        final int EXPECTED = 0;

        UserDAOTestHelpers.assertInsertCountsAreEqual(EXPECTED, ACTUAL);
    }
}
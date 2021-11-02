package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.models.*;

import helpers.TestPair;
import helpers.UserDAOTestHelpers;
import org.junit.jupiter.api.*;
import org.springframework.test.context.TestPropertySource;

import static helpers.UserDAOTestHelpers.convertLocalDateToDate;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@TestPropertySource(properties = {
        "application.env=test"
})
class UserDAOTest {
    private static final ProjectEnvironment testEnvironment = ProjectEnvironment.TEST;
    private static Optional<Connection> testConnection;
    private static UserDAO sut;
    private static ArrayList<User> users;

    @BeforeAll
    public static void setUpBeforeUserDAOTests() {
        try {
            testConnection = UserDAOTestHelpers.createConnection();
            createUserDAO();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @AfterAll
    public static void tearDownAfterDAOTests() {
        try {
            UserDAOTestHelpers.closeConnection(sut);
            destroyVars();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @AfterEach
    public void tearDownAfterEach() {
        try {
            users.clear();
            UserDAOTestHelpers.deleteAllUsers(testConnection);
        } catch(SQLException sqlException) {
            System.out.println("In after each " + sqlException.getMessage());
            fail();
        }
    }

    private static void destroyVars() {
        testConnection = Optional.empty();
        users = null;
        sut = null;
    }

    private static void createUserDAO() {
        if (testConnection.isPresent()) {
            sut = UserDAOTestHelpers.createSut(testEnvironment, testConnection.get());
        }
    }

    @Test
    public void testIsUserSavedCorrectly() {
        final String testMethodName = "testGetById_WhenPassedInValidId_ShouldReturnCorrectUser";
        users = new ArrayList<>();
        users.add(UserDAOTestHelpers.nonExistantUser);

        try {
            UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);
            final Optional<User> expectedUser = Optional.ofNullable(users.get(0));
            final Long expectedUserId = users.get(0).getUserId();
            final Optional<User> actualUser = sut.getById(expectedUserId);
            final TestPair<User> userPair = new TestPair<>(actualUser.get(), expectedUser.get());

            UserDAOTestHelpers.checkIfUsersAreEqualIgnoringId(userPair);
        } catch (Exception e) {
            System.out.println("In " + testMethodName + " " + e.getMessage());
            fail();
        }
    }

    @Test
    public void testGetById_WhenPassedInInvalidId_ShouldReturnEmptyOptional() {
        final String testMethodName = "testGetById_WhenPassedInInvalidId_ShouldReturnEmptyOptional";
        try {
            users = UserDAOTestHelpers.createDefaultTestUsers();
            UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

            final Long id = Long.MAX_VALUE;

            final Optional<User> actualUser = sut.getById(id);

            UserDAOTestHelpers.assertUserShouldBeNull(actualUser);
        } catch (Exception e) {
           System.out.println("Error in " + testMethodName + " " + e.getMessage());
           fail();
        }
    }

    @Test
    public void testGetByUsername_WhenPassedExistingUsername_ShouldReturnUser() {
        final String testMethodName = "testGetById_WhenPassedInInvalidId_ShouldReturnEmptyOptional";
        try {
            users = UserDAOTestHelpers.createDefaultTestUsers();
            UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

            for (int i = 0; i < users.size(); i++) {
                final Optional<User> actualUser = sut.getByUsername(users.get(i).getUsername());

                UserDAOTestHelpers.assertUserIsNotNull(actualUser);
            }
        } catch (Exception e) {
            System.out.println("Error in " + testMethodName + " " + e.getMessage());
            fail();
        }
    }

    @Test
    public void testGetByUsername_WhenPassedNonExistingUsername_ShouldReturnNull() {
        final String testMethodName = "testGetById_WhenPassedInInvalidId_ShouldReturnEmptyOptional";
        try {
            users = UserDAOTestHelpers.createDefaultTestUsers();
            UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

            final Optional<User> actualUser = sut.getByUsername(UserDAOTestHelpers.newUser.getUsername());

            UserDAOTestHelpers.assertUserShouldBeNull(actualUser);
        } catch (Exception e) {
            System.out.println("Error in " + testMethodName + " " + e.getMessage());
            fail();
        }
    }
//
    @Test
    public void testGetAll_ShouldReturnAllUsers() {
        final String testName = "testGetAll_ShouldReturnAllUsers";
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

        final ArrayList<User> actualUsers = new ArrayList<>(sut.getAll());
        final ArrayList<User> expectedUsers = new ArrayList<>(users);

        final TestPair<ArrayList<User>> pair = new TestPair<>(actualUsers, expectedUsers);

        UserDAOTestHelpers.assertUserListIsEqual(pair);
    }
//
    @Test
    public void testInsertOne_WhenPassedInValidUser_ShouldReturnOne() {
        final User user = UserDAOTestHelpers.nonExistantUser;
        users = new ArrayList<>();

        final int expectedCount = 1;
        final int actualCount = sut.insertOne(user);

        final TestPair<Integer> countPair = new TestPair<>(actualCount, expectedCount);

        UserDAOTestHelpers.assertUserCountIsEqual(countPair);
    }
//
    @Test
    public void testInsertMany_WhenPassedInArrayOfUsers_ShouldReturnArrayCountAndCorrectUsers() {
        users = UserDAOTestHelpers.createDefaultTestUsers();

        final int expectedCount = users.size();
        final int actualCount = sut.insertMany(users);

        final TestPair<Integer> countPair = new TestPair<>(actualCount, expectedCount);

        UserDAOTestHelpers.assertUserCountIsEqual(countPair);
    }

    @Test
    public void testGetPasswordForUser_WithValidLogin_ShouldReturnPasswordAndNoErrorMessage() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

        final User USER = users.get(0);
        final Login LOGIN = Login.createLogin(USER.getUsername(), USER.getPassword());
        final UserServiceResultPair<String> PAIR = new UserServiceResultPair<>(USER.getPassword(), "");

        final UserServiceResultPair<String> ACTUAL_PAIR = sut.getPasswordForUser(LOGIN);

        Assertions.assertEquals(PAIR.getValue(), ACTUAL_PAIR.getValue());
        Assertions.assertEquals(PAIR.getMessage(), ACTUAL_PAIR.getMessage());
    }

    @Test
    public void testGetPasswordForUser_WithNonExistantUser_ShouldReturnNoPasswordAndErrorMessage() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);
        final User USER = UserDAOTestHelpers.nonExistantUser2;
        final Login LOGIN = Login.createLogin(USER.getUsername(), USER.getPassword());

        final UserServiceResultPair<String> ACTUAL_PAIR = sut.getPasswordForUser(LOGIN);

        Assertions.assertEquals("", ACTUAL_PAIR.getValue());
        Assertions.assertNotEquals("", ACTUAL_PAIR.getMessage());
    }

    @Test
    public void testGetUsernameBySessionId_whenPassingInAnEmptySessionId_shouldReturnResultPairWithNoValueAndEmptySessionId() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

        final String SESSION_ID = "";
        final ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);
        final ResultPair<String> EXPECTED_PAIR = new ResultPair<>("", sut.EMPTY_SESSIONID);

        UserDAOTestHelpers.assertResultPairAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
    }

    @Test
    public void testGetUsernameBySessionId_whenPassingInFirstId_shouldReturnResultPairWithU1AndNoErrorMessage() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);
        final User USER = users.get(0);

        final String SESSION_ID = USER.getSession().getSessionName();
        ResultPair<String> EXPECTED_PAIR = new ResultPair<>(USER.getUsername(), "");
        ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertResultPairAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
    }

    @Test
    public void testGetUsernameBySessionId_whenPassingInThirdId_shouldReturnResultPairWithNoUsernameAndErrorMessageAsSessionIdDoesntExist() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);


        final String SESSION_ID = "third";
        ResultPair<String> EXPECTED_PAIR = new ResultPair<>("", sut.NONEXISTANT_SESSIONID);
        ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertResultPairAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
    }

    @Test
    public void testGetSessionExpDateBySessionId_whenPassingInFirstSessionId_shouldReturnJan12000() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

        final Session SESSION = users.get(0).getSession();
        final LocalDate EXP_DATE = SESSION.getExpirationDate();

        final ResultPair<Optional<LocalDate>> EXPECTED_PAIR = UserDAOTestHelpers.getExpectedPair(EXP_DATE, "");
        final ResultPair<Optional<LocalDate>> ACTUAL_PAIR = sut.getSessionExpDateBySessionId(SESSION.getSessionName());

        UserDAOTestHelpers.assertLocalDateResultPair(ACTUAL_PAIR, EXPECTED_PAIR);
    }

    @Test
    public void testGetSessionExpDateBySessionId_whenPassingInThirdSessionId_shouldReturnEmptyOptional() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

        final String SESSION_ID = "third";

        final ResultPair<Optional<LocalDate>> EXPECTED = new ResultPair<>(Optional.empty(), sut.NONEXISTANT_SESSIONID);
        final ResultPair<Optional<LocalDate>> ACTUAL = sut.getSessionExpDateBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertLocalDateResultPair(ACTUAL, EXPECTED);
    }

    @Test
    public void testGetSessionExpDateBySessionId_whenPassingInEmptyString_shouldReturnEmptyDateAndSessionIdDoesntExist() {
        users = UserDAOTestHelpers.createDefaultTestUsers();
        UserDAOTestHelpers.insertManyUsers(testConnection.get(), users);

        final String SESSION_ID = "";

        final ResultPair<Optional<LocalDate>> EXPECTED = new ResultPair<>(Optional.empty(), sut.EMPTY_SESSIONID);
        final ResultPair<Optional<LocalDate>> ACTUAL = sut.getSessionExpDateBySessionId(SESSION_ID);

        UserDAOTestHelpers.assertLocalDateResultPair(ACTUAL, EXPECTED);
    }
}
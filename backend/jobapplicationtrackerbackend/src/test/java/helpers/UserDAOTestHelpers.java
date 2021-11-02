package helpers;

import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.UserFields;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserQuerier;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTestHelpers {
   public static final String tableName = "testusers";
   public static final ProjectEnvironment testEnvironment = ProjectEnvironment.TEST;
   public static final User nonExistantUser = User.createUser("u1", "e1",
           "p1",
           Session.createSession("invalidSession", LocalDate.of(2000, 1, 1)),
           3L);
   public static final User nonExistantUser2 = User.createUser("u3", "e3", "p3",
           Session.createSession("invalidSession", LocalDate.of(2000, 1, 1)), 4L);
   public static final User newUser = User.createUser("u3", "e3", "p1",
           Session.createSession("s1", LocalDate.of(2000, 1, 1)), 4L);
   public static final Login login = Login.createLogin("u1", "p1");

   public static String hashPassword(final Integer NUM_OF_ROUNDS, final String RAW_PASSWORD) {
      final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(NUM_OF_ROUNDS);

      return ENCODER.encode(RAW_PASSWORD);
   }

   public static void assertSavedUserIsCorrect(TestPair<Optional<User>> userPair, TestPair<Integer> countPair) {
      assertUserIsEqual(userPair);
      assertUserCountIsEqual(countPair);
   }
   public static void assertUserIsEqual(final TestPair<Optional<User>> pair) {
      pair.assertEqual(getUserIsEqualErrorMessage(pair));
   }

   public static void assertUserCountIsEqual(final TestPair<Integer> pair) {
      pair.assertEqual(getUserCountErrorMessage(pair));
   }

   public static void assertResultPairAreEqual(final ResultPair<String> ACTUAL,
                                               final ResultPair<String> EXPECTED) {
      final String ACTUAL_VALUE = ACTUAL.getValue();
      final String EXPECTED_VALUE = EXPECTED.getValue();
      final String ACTUAL_ERR_MSG = ACTUAL.getMessage();
      final String EXPECTED_ERR_MSG = EXPECTED.getMessage();

      final String[] ERR_MSGS = {
              String.format("For value: %s", getErrorMessage(ACTUAL_VALUE, EXPECTED_VALUE)),
              String.format("For error message: %s", getErrorMessage(ACTUAL_ERR_MSG, EXPECTED_ERR_MSG))
      };

      Assertions.assertEquals(EXPECTED_VALUE, ACTUAL_VALUE, ERR_MSGS[0]);
      Assertions.assertEquals(EXPECTED_ERR_MSG, ACTUAL_ERR_MSG, ERR_MSGS[1]);
   }
   private static String getUserCountErrorMessage(final TestPair<Integer> pair) {
      return "Expected " + pair.getExpectedValue() + ", got " + pair.getActualValue() + " instead.";
   }

   public static void assertUserListIsEqual(final TestPair<ArrayList<User>> pair) {
      pair.assertEqual(getErrorMessageForArrayListIsEqual(pair));
   }

   public static void assertUserIsNotNull(final Optional<User> user) {
      assertTrue(user.isPresent(), getUserIsNotNullErrorMessage(user));
   }

   public static void assertUserShouldBeNull(final Optional<User> user) {
      assertFalse(user.isPresent(), getUserIsNullErrorMessage(user));
   }

   public static void assertLocalDateResultPair(final ResultPair<Optional<LocalDate>> ACTUAL,
                                                final ResultPair<Optional<LocalDate>> EXP) {
      final String DATE_MSG = getErrorMessage(ACTUAL.getValue().toString(), EXP.getValue().toString());
      final String ERR_MSG = getErrorMessage(ACTUAL.getMessage(), EXP.getMessage());

      Assertions.assertEquals(ACTUAL.getValue(), EXP.getValue(), DATE_MSG);
      Assertions.assertEquals(ACTUAL.getMessage(), EXP.getMessage(), ERR_MSG);
   }

   private static String getUserIsNotNullErrorMessage(final Optional<User> user) {
      return "User is null";
   }

   private static String getUserIsNullErrorMessage(final Optional<User> user) {
      return "User should be null, got " + user.toString() + " instead.";
   }

   private static String getUserIsEqualErrorMessage(TestPair<Optional<User>> pair) {
      return "Expected " + pair.getExpectedValue().toString() + ", got " +
              pair.getActualValue().toString() + " instead.";
   }

   private static String getErrorMessageForArrayListIsEqual(TestPair<ArrayList<User>> pair) {
      return "Expected " + pair.getExpectedValue().toString() + ", got " +
              pair.getActualValue().toString() + " instead.";
   }

   public static ArrayList<User> createTestUsers(User[] userToAdd) {
      ArrayList<User> users = new ArrayList<>();

      Collections.addAll(users, userToAdd);

      return users;
   }

   public static ArrayList<User> createDefaultTestUsers() {
      final Session[] sessions = {
              Session.createSession("first", LocalDate.of(2000, 1, 1)),
              Session.createSession("second", LocalDate.of(3000, 1, 1))
      };
      final User[] users = {
              User.createUser("u1", "e1", "p1", sessions[0], 1),
              User.createUser("u2", "e2", "p2", sessions[1], 2)
      };

      return createTestUsers(users);
   }

   private static void addOptionalUser(ArrayList<User> users, Optional<User> user) {
      if (!user.isPresent()) {
         return;
      }

      users.add(user.get());
   }

   public static String buildInsertUserTestQuery() {
      return "INSERT INTO " + tableName + " (userid, username, email, password, sessionname, expdate)" +
              " VALUES (?, ?, ?, ?, ?, ?)";
   }

   public static int insertManyUsers(Connection jobAppConnection, ArrayList<User> users) {
      final String query = buildInsertUserTestQuery();

      try {
         final PreparedStatement statement = jobAppConnection.prepareStatement(query);
         return insertUsers(statement, users);
      } catch (SQLException e) {
         e.printStackTrace();
         return -1;
      }
   }

   public static ResultPair<Optional<LocalDate>> getExpectedPair(final LocalDate EXP_DATE, final String MSG) {
      return new ResultPair<>(Optional.of(EXP_DATE), MSG);
   }

   private static int insertUsers(PreparedStatement statement, ArrayList<User> users) {
      try {
         if (users.isEmpty()) {
            return 0;
         }
         for (User user : users) {
            final Date sessionExpDate = convertLocalDateToDate(user.getSession().getExpirationDate());

            statement.setLong(1, user.getUserId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getSession().getSessionName());
            statement.setDate(6, sessionExpDate);

            statement.addBatch();
         }

         return statement.executeBatch().length;
      } catch (SQLException e) {
         e.printStackTrace();
         return -1;
      }
   }

   public static void deleteAllUsers(Optional<Connection> connection) throws SQLException {
      if (!connection.isPresent()) {
         System.out.println("Connection is null");
         fail();
      }
      try {
         final Statement statement = connection.get().createStatement();
         statement.execute("DELETE FROM " + tableName + ";");
      } catch (SQLException sqlException) {
         throw new SQLException(sqlException.getMessage());
      }
   }

   public static Optional<Connection> createConnection() {
      final Optional<Connection> connection = JobAppTrackerConnection.createConnection();

      if (!connection.isPresent()) {
         System.out.println("Failed to create connection");
         fail();
      }

      return connection;
   }

   public static void closeConnection(UserDAO userDAO) {
      try {
         userDAO.closeConnection();
      } catch(SQLException sqlException) {
         System.out.println("Failed to close connection: " + sqlException.getMessage());
         fail();
      }
   }

   public static UserDAO createSut(ProjectEnvironment env, Connection connection) {
      return new UserDAO();
   }

   public static Date convertLocalDateToDate(LocalDate localDate) {
      return Date.valueOf(localDate);
   }

   public ArrayList<User> getAllUsersWithoutId(Connection jobAppConnection, UserDAO userDAO) {
      final String query = "SELECT username, email, password, sessionname, expdate"
              + " FROM " + tableName + ";";
      ArrayList<User> users = new ArrayList<>();
      try {
            Statement statement = jobAppConnection.createStatement();
            ResultSet set = statement.executeQuery(query);

            while(set.next()) {
               final User user = userDAO.buildUser(set);
               users.add(user);
            }
            return users;
      } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
            return new ArrayList<>();
         }
   }

   public static Optional<User> getUserByUsername(Connection connection, String username) {
     final String query = String.format("SELECT userid, username, email, password, sessionname, expdate" +
             " FROM %s WHERE username = '%s';", tableName, username);

      try {
         final Statement statement = connection.createStatement();
         final ResultSet set = statement.executeQuery(query);

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

   public static void checkIfUsersAreEqualIgnoringId(TestPair<User> userPair) {
      final User actualUser = userPair.getActualValue();
      final User expectedUser = userPair.getExpectedValue();

      assertEquals(expectedUser.getUsername(), actualUser.getUsername());
      assertEquals(expectedUser.getEmail(), actualUser.getEmail());
      assertEquals(expectedUser.getPassword(), actualUser.getPassword());
      assertEquals(expectedUser.getSession(), actualUser.getSession());
   }

   public static String getErrorMessage(final String ACTUAL, final String EXPECTED) {
      return String.format("Expected %s, got %s instead.", EXPECTED, ACTUAL);
   }
}

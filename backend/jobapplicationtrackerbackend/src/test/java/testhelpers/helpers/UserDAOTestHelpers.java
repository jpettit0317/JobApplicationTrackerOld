package testhelpers.helpers;

import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.junit.jupiter.api.Assertions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import testhelpers.helpers.TestPair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTestHelpers {
   public static Optional<Connection> createConnection() {
      return JobAppTrackerConnection.createConnection();
   }
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

   public static void assertInsertCountsAreEqual(final Integer EXPECTED, final Integer ACTUAL) {
      final String EXPECTED_STR = EXPECTED.toString();
      final String ACTUAL_STR = ACTUAL.toString();

      final String ERR_MSG = getErrorMessage(ACTUAL_STR, EXPECTED_STR);
      Assertions.assertEquals(EXPECTED, ACTUAL, ERR_MSG);
   }

   public static void assertStringResultPairsAreEqual(final ResultPair<String> ACTUAL,
                                                      final ResultPair<String> EXPECTED) {
      final String VALUE_ERR_MSG = getErrorMessage(ACTUAL.getValue(), EXPECTED.getValue());
      final String MSG_ERR_MSG = getErrorMessage(ACTUAL.getMessage(), EXPECTED.getMessage());

      Assertions.assertEquals(EXPECTED.getValue(), ACTUAL.getValue(), VALUE_ERR_MSG);
      Assertions.assertEquals(EXPECTED.getMessage(), ACTUAL.getMessage(), MSG_ERR_MSG);
   }

   public static void verifyErrorStringResultPair(final ResultPair<String> ACTUAL, final String MSG) {
      final String VALUE_ERR_MSG = getErrorMessage(ACTUAL.getValue(), "");
      final String MSG_ERR_MSG = getErrorMessage(ACTUAL.getMessage(), MSG);

      Assertions.assertEquals("", ACTUAL.getValue(), VALUE_ERR_MSG);
      Assertions.assertEquals(MSG, ACTUAL.getMessage(), MSG_ERR_MSG);
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



   public static ResultPair<Optional<LocalDate>> getExpectedPair(final LocalDate EXP_DATE, final String MSG) {
      return new ResultPair<>(Optional.of(EXP_DATE), MSG);
   }

   public static void checkIfUsersAreEqualIgnoringId(TestPair<User> userPair) {
      final User actualUser = userPair.getActualValue();
      final User expectedUser = userPair.getExpectedValue();

      assertEquals(expectedUser.getUsername(), actualUser.getUsername());
      assertEquals(expectedUser.getEmail(), actualUser.getEmail());
      assertEquals(expectedUser.getPassword(), actualUser.getPassword());
      assertEquals(expectedUser.getSession(), actualUser.getSession());
   }

   public static void verifyErrorOptionalLocalDateResultPair(final ResultPair<Optional<LocalDate>> ACTUAL,
                                                             final String ERROR_MSG) {
      final String LOCAL_DATE_STR = ACTUAL.getValue().toString();
      final String[] ERR_MSGS = {
              getErrorMessage(LOCAL_DATE_STR, ""),
              getErrorMessage(ACTUAL.getMessage(), ERROR_MSG)
      };
      try {
         final ArrayList<String> ERROR_MSGS = createErrorMessages(ERR_MSGS);
         Assertions.assertFalse(ACTUAL.getValue().isPresent(), ERR_MSGS[0]);
         Assertions.assertEquals(ACTUAL.getMessage(), ERROR_MSG);
      } catch(Exception exception) {
         final String MSG = String.format("Error: %s", exception.getMessage());
         System.out.println(MSG);
         exception.printStackTrace();
         fail(MSG);
      }
   }

   private static ArrayList<String> createErrorMessages(final String[] ERROR_MSGS) {
      ArrayList<String> messages = new ArrayList<>();
      Collections.addAll(messages, ERROR_MSGS);

      return messages;
   }

   public static String getErrorMessage(final String ACTUAL, final String EXPECTED) {
      return String.format("Expected %s, got %s instead.", EXPECTED, ACTUAL);
   }
}

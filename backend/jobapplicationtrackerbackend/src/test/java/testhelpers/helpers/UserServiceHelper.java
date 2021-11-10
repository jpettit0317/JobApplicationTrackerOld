package testhelpers.helpers;

import com.jpettit.jobapplicationtrackerbackend.models.*;
import org.junit.jupiter.api.Assertions;
import testhelpers.helpers.TestPair;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

public class UserServiceHelper {
    public static void verifyUserServiceUserPair(UserServiceIntPair actual, UserServiceIntPair expected) {
        TestPair<Integer> countPair = new TestPair<>(actual.getValue(), expected.getValue());
        TestPair<String> messagePair = new TestPair<>(actual.getMessage(), expected.getMessage());

        String[] errorMessages = {
                errorMessageForUserServiceUserPairCount(countPair),
                errorMessageForUserUserServiceUserPairMessage(messagePair)
        };

        countPair.assertEqual(errorMessages[0]);
        messagePair.assertEqual(errorMessages[1]);
    }

    public static void assertThatStringResultPairsAreEqual(ResultPair<String> ACTUAL, ResultPair<String> EXPECTED) {
        final String VALUE_ERR_MSG = getErrorMessage(ACTUAL.getValue(), EXPECTED.getValue());
        final String MESSAGE_ERR_MSG = getErrorMessage(ACTUAL.getMessage(), EXPECTED.getMessage());

        Assertions.assertEquals(ACTUAL.getValue(), EXPECTED.getValue(), VALUE_ERR_MSG);
        Assertions.assertEquals(ACTUAL.getMessage(), EXPECTED.getMessage(), MESSAGE_ERR_MSG);
    }

    public static String getErrorMessage(final String ACTUAL, final String EXPECTED) {
        return String.format("Expected %s, got %s instead.", EXPECTED, ACTUAL);
    }

    public static String getErrorMessageForHasExpired(final boolean hasExpired,
                                                      final LocalDate ACCESS_DATE,
                                                      final LocalDate EXP_DATE) {
        final String ACCESS_DATE_STR = convertLocalDateToString(ACCESS_DATE);
        final String EXP_DATE_STR = convertLocalDateToString(EXP_DATE);

        if (hasExpired) {
            return String.format("Session with expiration date %s, with access date %s has expired.",
                    EXP_DATE_STR, ACCESS_DATE_STR);
        } else {
            return String.format("Session with expiration date %s, with access date %s has not expired.",
                    EXP_DATE_STR, ACCESS_DATE_STR);
        }
    }

    public static void assertSessionHasExpired(ResultPair<Boolean> ACTUAL,
                                               LocalDate ACCESS_DATE,
                                               LocalDate EXP_DATE) {
        final String ERR_MSG = getErrorMessageForHasExpired(true, ACCESS_DATE, EXP_DATE);

        Assertions.assertTrue(ACTUAL.getValue(), ERR_MSG);
        Assertions.assertEquals("", ACTUAL.getMessage());
    }

    public static void assertSessionHasNotExpired(ResultPair<Boolean> ACTUAL,
                                                  LocalDate ACCESS_DATE,
                                                  LocalDate EXP_DATE) {
        final String ERR_MSG = getErrorMessageForHasExpired(false, ACCESS_DATE, EXP_DATE);

        Assertions.assertFalse(ACTUAL.getValue(), ERR_MSG);
        Assertions.assertEquals("", ACTUAL.getMessage());
    }

    public static void assertHasSessionExpiredPair(ResultPair<Boolean> ACTUAL, ResultPair<Boolean> EXPECTED) {
        final String[] ERR_MSGS = {
                getErrorMessage(ACTUAL.getValue().toString(), EXPECTED.getValue().toString()),
                getErrorMessage(ACTUAL.getMessage(), EXPECTED.getMessage())
        };

        Assertions.assertEquals(ACTUAL.getValue(), EXPECTED.getValue(), ERR_MSGS[0]);
        Assertions.assertEquals(ACTUAL.getMessage(), EXPECTED.getMessage(), ERR_MSGS[1]);
    }

    private static String errorMessageForUserServiceUserPairCount(TestPair<Integer> pair) {
        final String actualString = pair.getActualValue().toString();
        final String expectedString = pair.getExpectedValue().toString();

        return String.format("Expected %s, got %s instead.", expectedString, actualString);
    }

    private static String errorMessageForUserUserServiceUserPairMessage(TestPair<String> pair) {
        final String actualString = pair.getActualValue().toString();
        final String expectedString = pair.getExpectedValue().toString();

        return String.format("Expected %s, got %s instead.", expectedString, actualString);
    }

    private static String convertLocalDateToString(final LocalDate DATE) {
        final String MONTH_NAME = DATE.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
        final int DAY = DATE.getDayOfMonth();
        final int YEAR = DATE.getYear();

        return String.format("%s %d, %d", MONTH_NAME, DAY, YEAR);
    }
}

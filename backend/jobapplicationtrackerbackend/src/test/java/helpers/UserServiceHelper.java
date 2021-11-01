package helpers;

import com.jpettit.jobapplicationtrackerbackend.models.*;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.Optional;

public class UserServiceHelper {
    public static final User user = User.createUser("u1", "e1",
            "p1",
            Session.createSession("invalidSession", LocalDate.of(2000, 1, 1)),
            0L);
    public static final User user2 = User.createUser("u2", "e2", "p2",
            Session.createSession("goodSession", LocalDate.of(2000, 1, 1)), 1L);
    public static final Optional<User> nullUser = Optional.empty();
    public static final Login login = Login.createLogin("u1", "p1");
    public static final Login nonExistantLogin = Login.createLogin("dude", "p5");

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
}

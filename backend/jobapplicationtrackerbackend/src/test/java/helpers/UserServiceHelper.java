package helpers;

import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.models.UserServiceIntPair;
import com.jpettit.jobapplicationtrackerbackend.models.UserServiceUserPair;

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

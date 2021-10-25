package helpers;

import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.models.UserServiceUserPair;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserServiceUserPairHelper {
    public static final User user = User.createUser("u1", "e1",
            "p1",
            Session.createSession("invalidSession", LocalDate.of(2000, 1, 1)),
            0L);
    public static final Optional<User> nullUser = Optional.empty();

    public static void verifyDaoPairConstructor(UserServiceUserPair pair, Optional<User> expectedUser, String errorMessage) {
        final TestPair<Optional<User>> userPair = new TestPair<>(pair.getValue(), expectedUser);
        final TestPair<String> errorMessagePair = new TestPair<>(pair.getMessage(), errorMessage);

        final String [] errorMessages = {
                getDaoPairConstructorErrorMessageForUser(userPair),
                getDaoPairConstructorErrorMessageForMessage(errorMessagePair)
        };

        userPair.assertEqual(errorMessages[0]);
        errorMessagePair.assertEqual(errorMessages[1]);
    }

    public static void verifyUserPairHasAnEmptyUser(UserServiceUserPair pair) {
        final String errorMessage = getDaoPairConstructorEmptyUserErrorMessage(pair.getValue());

        assertFalse(pair.getValue().isPresent(), errorMessage);
    }

    private static String getDaoPairConstructorErrorMessageForUser(TestPair<Optional<User>> pair) {
        final String expectedUser = pair.getExpectedValue().toString();
        final String actualUser = pair.getActualValue().toString();

        return String.format("Expected %s, got %s instead.", expectedUser, actualUser);
    }

    private static String getDaoPairConstructorErrorMessageForMessage(TestPair<String> pair) {
        return String.format("Expected %s, got %s instead.", pair.getExpectedValue(), pair.getActualValue());
    }

    private static String getDaoPairConstructorEmptyUserErrorMessage(Optional<User> user) {
        return String.format("Expected empty optional, got %s instead.", user.toString());
    }
}

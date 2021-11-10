package testhelpers.helpervars;

import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

public class UserControllerTestHelperVars {

    public static User createDefaultUser() {
        final Session SESSION = Session.createSession("Session1", LocalDate.of(2000, 1, 1));

        return User.createUser("user1", "user1@email.com", "password", SESSION, 1L);
    }
}

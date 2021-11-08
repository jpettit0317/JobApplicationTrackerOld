package testhelpers;

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

    public static ResultPair<String> convertJSONResponseToStringResultPair(final String JSON_RESPONSE) throws JSONException {
        final JSONObject obj = new JSONObject(JSON_RESPONSE);
        final String VALUE = obj.getString("value");
        final String ERROR_MSG = obj.getString("message");

        return new ResultPair<>(VALUE, ERROR_MSG);
    }
}

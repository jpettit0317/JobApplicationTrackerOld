package testhelpers;

import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {
    public static String convertJSONObjectToString(final User USER) {
        try {
            return convertUserToJsonObject(USER).toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    private static JSONObject convertUserToJsonObject(final User USER) throws JSONException {
        JSONObject jsonUser = new JSONObject();
        final Session SESSION = USER.getSession();

        jsonUser.put("username", USER.getUsername());
        jsonUser.put("password", USER.getPassword());
        jsonUser.put("email", USER.getEmail());
        jsonUser.put("userId", USER.getUserId());
        jsonUser.put("sessionName", SESSION.getSessionName());
        jsonUser.put("expDate", SESSION.getExpirationDate());

        return jsonUser;
    }
}

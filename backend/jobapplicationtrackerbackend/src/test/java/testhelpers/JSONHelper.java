package testhelpers;

import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {
    public static String convertJSONUserToString(final User USER) {
        try {
            return convertUserToJsonObject(USER).toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String convertJSONLoginToString(final Login LOGIN) {
        try {
            return convertLoginToJsonObject(LOGIN).toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static ResultPair<String> convertJSONResponseToStringResultPair(final String RES) throws JSONException {
        final JSONObject obj = new JSONObject(RES);
        final String VALUE = obj.getString("value");
        final String ERROR_MSG = obj.getString("message");

        return new ResultPair<>(VALUE, ERROR_MSG);
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

    private static JSONObject convertLoginToJsonObject(final Login LOGIN) throws JSONException {
        JSONObject jsonUser = new JSONObject();

        jsonUser.put("username", LOGIN.getUsername());
        jsonUser.put("password", LOGIN.getPassword());

        return jsonUser;
    }
}

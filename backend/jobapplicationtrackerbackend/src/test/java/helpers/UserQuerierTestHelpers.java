package helpers;

import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserQuerier;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.HashMap;

public class UserQuerierTestHelpers {
    public static final Session user1Session = Session.createSession("1", LocalDate.now());

    public static UserQuerier createUserQuerier(ProjectEnvironment environment) {
        return new UserQuerier(environment);
    }

    public static String getExpectedGetUsernameBySessionIdQuery(ProjectEnvironment env, String sessionId) {
        final String TABLE_NAME = getTableNameForUser(env);

        return String.format("SELECT username FROM %s WHERE sessionname = '%s';", TABLE_NAME, sessionId);
    }

    private static String getTableNameForUser(ProjectEnvironment env) {
        HashMap<ProjectEnvironment, String> map = new HashMap<>();

        map.put(ProjectEnvironment.PROD, "users");
        map.put(ProjectEnvironment.TEST, "testusers");
        map.put(ProjectEnvironment.DEV, "devusers");

        return map.getOrDefault(env, "");
    }

    public static void assertQueriesAreEqual(final String ACTUAL, final String EXPECTED) {
        final String ERR_MSG = String.format("Expected %s, got %s instead.", EXPECTED, ACTUAL);

        Assertions.assertEquals(EXPECTED, ACTUAL, ERR_MSG);
    }
}

package helpers;

import com.jpettit.jobapplicationtrackerbackend.helpers.JobAppQuerier;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;

public class JobAppQuerierTestHelpers {
    public static final ProjectEnvironment TEST_ENV = ProjectEnvironment.TEST;
    public static final  ProjectEnvironment DEV_ENV = ProjectEnvironment.DEV;
    public static final ProjectEnvironment PROD_ENV = ProjectEnvironment.PROD;

    public static final String INVALID_USERNAME = "Username is empty";
    private static final String INVALID_TABLENAME = "Table name is empty";

    public static final String USER1 = "user1";

    public static JobAppQuerier createJobAppQuerier(ProjectEnvironment env) {
        return new JobAppQuerier(env);
    }

    private static String buildGetAllJobAppCardsQueryErrorMessage(String actual, String expected) {
        return String.format("Expected %s, got %s instead.", expected, actual);
    }

    public static void comparePairs(final ResultPair<String> ACTUAL,
                                    final ResultPair<String> EXPECTED) {
        final String VALUE_ERR_MSG = buildGetAllJobAppCardsQueryErrorMessage(ACTUAL.getValue(),
                EXPECTED.getValue());
        final String MESSAGE_ERR_MSG = buildGetAllJobAppCardsQueryErrorMessage(ACTUAL.getMessage(),
                EXPECTED.getMessage());

        Assertions.assertEquals(EXPECTED.getValue(), ACTUAL.getValue(), VALUE_ERR_MSG);
        Assertions.assertEquals(EXPECTED.getMessage(), ACTUAL.getMessage(), MESSAGE_ERR_MSG);
    }

    public static void compareErrorMessages(final ResultPair<String> ACTUAL, final ResultPair<String> EXPECTED) {
        final String VALUE_ERR_MSG = buildGetAllJobAppCardsQueryErrorMessage(ACTUAL.getValue(),
                EXPECTED.getValue());
        final String MESSAGE_ERR_MSG = buildGetAllJobAppCardsQueryErrorMessage(ACTUAL.getMessage(),
                EXPECTED.getMessage());

        Assertions.assertEquals(EXPECTED.getValue(), ACTUAL.getValue(), VALUE_ERR_MSG);
        Assertions.assertEquals(EXPECTED.getMessage(), ACTUAL.getMessage(), MESSAGE_ERR_MSG);
    }
    public static String getQuery(ProjectEnvironment environment, String model, String username) {
        final String TABLE_NAME = getTableNameForJobApps(environment);

        return String.format("SELECT jobappid, jobapptitle, company, " +
                "interviewcount FROM %s WHERE applicant = '%s';", TABLE_NAME, username);
    }

    private static String getTableNameForJobApps(ProjectEnvironment env) {
         HashMap<ProjectEnvironment, String> map = new HashMap<>();

         map.put(ProjectEnvironment.DEV, "devjobapps");
         map.put(ProjectEnvironment.PROD, "jobapps");
         map.put(ProjectEnvironment.TEST, "testjobapps");

        return map.getOrDefault(env, "");
    }
}
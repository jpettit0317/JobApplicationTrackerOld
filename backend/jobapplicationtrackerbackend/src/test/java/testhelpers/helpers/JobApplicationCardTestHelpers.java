package testhelpers.helpers;

import com.jpettit.jobapplicationtrackerbackend.models.Interview;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplication;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import org.junit.jupiter.api.Assertions;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class JobApplicationCardTestHelpers {
    private static final String TITLE_KEY = "title";
    private static final String COMPANY_KEY = "company";
    private static final String COUNT_KEY = "count";
    private static final String ID_KEY = "id";

    public static void verifyIfJobAppCardAndJobAppAreEqual(JobApplicationCard card, JobApplication jobApp) {
        HashMap<String, String> map = getErrorMessages(card, jobApp);

        Assertions.assertEquals(jobApp.getJobAppTitle(), card.getJobTitle(),
                map.getOrDefault(TITLE_KEY, ""));
        Assertions.assertEquals(jobApp.getCompanyName(), card.getCompanyName(),
                map.getOrDefault(COMPANY_KEY, ""));
        Assertions.assertEquals(jobApp.getInterviews().size(), card.getInterviewCount(),
                map.getOrDefault(COUNT_KEY, ""));
        Assertions.assertEquals(jobApp.getId(), card.getJobAppId(),
                map.getOrDefault(ID_KEY, ""));
    }

    public static HashMap<String, String> getErrorMessages(JobApplicationCard card, JobApplication app) {
       HashMap<String, String> map = new HashMap<>();

       map.put("title", String.format("Expected %s, got %s instead.", card.getJobTitle(), app.getJobAppTitle()));
       map.put("company", String.format("Expected %s, got %s instead.", card.getCompanyName(), app.getCompanyName()));
       map.put("count", String.format("Expected %s, got %s instead.", card.getInterviewCount(),
               app.getInterviews().size()));
       map.put("id", String.format("Expected %s, got %s instead.", card.getJobAppId(), app.getId()));

       return map;
    }
}

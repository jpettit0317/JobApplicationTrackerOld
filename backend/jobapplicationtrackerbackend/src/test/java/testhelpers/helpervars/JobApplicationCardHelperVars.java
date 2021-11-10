package testhelpers.helpervars;

import com.jpettit.jobapplicationtrackerbackend.models.Interview;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplication;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class JobApplicationCardHelperVars {
    public static JobApplication createDefaultJobApp() {
        final ArrayList<Interview> INTERVIEWS = createDefaultInterviews();

        return JobApplication.createJobApplication("iOS Developer", "EJ", "1",
                LocalDate.now(), "user1", "Pending", INTERVIEWS);
    }

    public static ArrayList<Interview> createDefaultInterviews() {
        final Interview[] INTERVIEWS = {
                Interview.createInterview("1", "1", "Phone interview",
                        Date.valueOf(LocalDate.now()), "Next Round"),
                Interview.createInterview("2", "1", "Phone interview",
                        Date.valueOf(LocalDate.now()), "Next Round"),
                Interview.createInterview("3", "1", "Phone interview",
                        Date.valueOf(LocalDate.now()), "Next Round")
        };

        return new ArrayList<>(Arrays.asList(INTERVIEWS));
    }
}

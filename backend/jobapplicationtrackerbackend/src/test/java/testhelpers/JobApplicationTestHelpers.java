package testhelpers;

import com.jpettit.jobapplicationtrackerbackend.models.Interview;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplication;
import org.junit.jupiter.api.Assertions;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class JobApplicationTestHelpers {
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

    public static void validateGetInterviewAtIndex(Integer index, Optional<Interview> ACTUAL, Optional<Interview> EXPECTED) {
        final String ERR_MSG = getErrorMessageForGetInterviewAtIndex(index, ACTUAL, EXPECTED);

        Assertions.assertEquals(EXPECTED, ACTUAL, ERR_MSG);
    }

    private static String getErrorMessageForGetInterviewAtIndex(Integer index, Optional<Interview> ACTUAL,
                                                                Optional<Interview> EXPECTED) {
        final String INDEX_STR = index.toString();
        final String EXPECTED_STR = EXPECTED.toString();
        final String ACTUAL_STR = ACTUAL.toString();

        return String.format("At index %s: expected %s, got %s instead.",
                INDEX_STR, EXPECTED_STR, ACTUAL_STR);
    }
}

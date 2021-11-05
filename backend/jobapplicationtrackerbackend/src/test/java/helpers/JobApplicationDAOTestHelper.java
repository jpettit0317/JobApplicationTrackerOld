package helpers;

import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class JobApplicationDAOTestHelper<Object> {
    private final Object ACTUAL;
    private final Object EXPECTED;

    public JobApplicationDAOTestHelper(final Object ACTUAL, final Object EXPECTED) {
        this.ACTUAL = ACTUAL;
        this.EXPECTED = EXPECTED;
    }

    public void verifyResultPairOfJobAppCardsAreEqual() {
        final String ACTUAL_STR = convertToString(ACTUAL);
        final String EXP_STR = convertToString(EXPECTED);
        final String ERR_MSG = getErrorMessage(EXP_STR, ACTUAL_STR);

        Assertions.assertEquals(EXPECTED, ACTUAL, ERR_MSG);
    }

    private String convertToString(final Object VALUE) {
        return VALUE.toString();
    }

    private String getErrorMessage(final String EXPECTED, final String ACTUAL) {
        return String.format("Expected %s, got %s instead.", EXPECTED, ACTUAL);
    }

}

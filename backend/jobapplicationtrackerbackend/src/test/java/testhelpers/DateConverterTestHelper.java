package testhelpers;

import org.junit.jupiter.api.Assertions;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class DateConverterTestHelper<Object> {
    private Object actualValue;
    private Object expectedValue;

    public DateConverterTestHelper(final Object ACTUAL, final Object EXPECTED) {
        if (!Optional.ofNullable(ACTUAL).isPresent()) {
            setActualValue(null);
        }

        if (!Optional.ofNullable(EXPECTED).isPresent()) {
            setExpectedValue(null);
        }

        setActualValue(ACTUAL);
        setExpectedValue(EXPECTED);
    }

    private String convertValueToString(final Object OBJ) {
        if (!Optional.ofNullable(OBJ).isPresent()) {
            return "";
        }
        return OBJ.toString();
    }

    public void verifyDatesAreEqual() {
        final String ERR_MSG = getErrorMessage(actualValue, expectedValue);

        Assertions.assertEquals(expectedValue, actualValue, ERR_MSG);
    }

    private String getErrorMessage(final Object ACTUAL, final Object EXPECTED) {
        final String ACTUAL_ERR_MSG = convertValueToString(ACTUAL);
        final String EXPECTED_ERR_MSG = convertValueToString(EXPECTED);

        return String.format("Expected %s, got %s instead.",
                EXPECTED_ERR_MSG, ACTUAL_ERR_MSG);
    }

    public void setActualValue(Object actualValue) {
        this.actualValue = actualValue;
    }

    public void setExpectedValue(Object expectedValue) {
        this.expectedValue = expectedValue;
    }
}

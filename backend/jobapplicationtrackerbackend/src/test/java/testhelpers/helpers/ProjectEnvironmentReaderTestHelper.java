package testhelpers.helpers;

import org.junit.jupiter.api.Assertions;

public class ProjectEnvironmentReaderTestHelper<Object> {
    private Object actualValue;
    private Object expectedValue;

    public ProjectEnvironmentReaderTestHelper(Object actualValue, Object expectedValue) {
        setActualValue(actualValue);
        setExpectedValue(expectedValue);
    }

    public void setActualValue(Object actualValue) {
        this.actualValue = actualValue;
    }

    public void setExpectedValue(Object expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void verifyEnvironmentsAreEqual() {
        final String ERROR_MSG = getErrorMessage();

        Assertions.assertEquals(expectedValue, actualValue, ERROR_MSG);
    }

    public String getErrorMessage() {
        final String ACTUAL = actualValue.toString();
        final String EXPECTED = expectedValue.toString();

        return String.format("Expected %s, got %s instead.",
                EXPECTED, ACTUAL);
    }
}

package testhelpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPair<T> {
    private T actualValue;
    private T expectedValue;

    public TestPair(T actualValue, T expectedValue) {
        setActualValue(actualValue);
        setExpectedValue(expectedValue);
    }

    public T getActualValue() {
        return actualValue;
    }

    public void setActualValue(T actualValue) {
        this.actualValue = actualValue;
    }

    public T getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(T expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void assertEqual(String errorMessage) {
        assertEquals(expectedValue, actualValue, errorMessage);
    }
}

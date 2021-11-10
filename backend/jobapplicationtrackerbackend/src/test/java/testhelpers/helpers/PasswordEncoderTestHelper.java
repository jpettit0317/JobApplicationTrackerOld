package testhelpers.helpers;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

public class PasswordEncoderTestHelper<Object> {
    private Object actual;
    private Object expected;

    public PasswordEncoderTestHelper(Object actual, Object expected) {
        setActual(actual);
        setExpected(expected);
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public void setExpected(Object expected) {
        this.expected = expected;
    }

    private String getStringForValue(final Object OBJ) {
        if (Optional.ofNullable(OBJ).isPresent()) {
            return OBJ.toString();
        } else {
            return "null";
        }
    }

    public String getErrorMessage() {
        final String ACTUAL = getStringForValue(actual);
        final String EXPECTED = getStringForValue(expected);

        return String.format("Expected %s, got %s instead.", EXPECTED, ACTUAL);
    }

    public void verifyValuesAreEqual() {
        final String ERR_MSG = getErrorMessage();

        Assertions.assertEquals(expected, actual, ERR_MSG);
    }

    public void verifyValuesAreNotEqual() {
        final String ERR_MSG = getErrorMessage();

        Assertions.assertNotEquals(expected, actual, ERR_MSG);
    }
}

package com.jpettit.jobapplicationtrackerbackend.models;

import com.jpettit.jobapplicationtrackerbackend.interfaces.DaoPair;

import java.util.Objects;

public class ResultPair<T> implements DaoPair<T> {
    private final T VALUE;
    private final String ERROR_MESSAGE;

    public ResultPair(final T VALUE, final String ERROR_MSG) {
        this.VALUE = VALUE;
        this.ERROR_MESSAGE = ERROR_MSG;
    }

    public ResultPair(ResultPair<T> newPair) {
        this.VALUE = newPair.getValue();
        this.ERROR_MESSAGE = newPair.getMessage();
    }

    public T getValue() {
        return VALUE;
    }

    public String getMessage() {
        return ERROR_MESSAGE;
    }

    @Override
    public String toString() {
        return "ResultPair{" +
                "VALUE=" + VALUE +
                ", ERROR_MESSAGE='" + ERROR_MESSAGE + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultPair<?> that = (ResultPair<?>) o;
        return VALUE.equals(that.VALUE) && ERROR_MESSAGE.equals(that.ERROR_MESSAGE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(VALUE, ERROR_MESSAGE);
    }
}

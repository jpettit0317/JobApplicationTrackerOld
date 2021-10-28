package com.jpettit.jobapplicationtrackerbackend.models;

import com.jpettit.jobapplicationtrackerbackend.helpers.DaoPair;

public class ResultPair<T> implements DaoPair<T> {
    private final T VALUE;
    private final String ERROR_MESSAGE;

    public ResultPair(final T VALUE, final String ERROR_MSG) {
        this.VALUE = VALUE;
        this.ERROR_MESSAGE = ERROR_MSG;
    }

    public T getValue() {
        return VALUE;
    }

    public String getMessage() {
        return ERROR_MESSAGE;
    }
}

package com.jpettit.jobapplicationtrackerbackend.models;

import com.jpettit.jobapplicationtrackerbackend.helpers.DaoPair;

public class UserServiceResultPair<T> implements DaoPair<T> {
    private T value;
    private String errorMessage;

    public UserServiceResultPair(T value, String errorMessage) {
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public T getValue() {
        return value;
    }

    public String getMessage() {
        return errorMessage;
    }
}

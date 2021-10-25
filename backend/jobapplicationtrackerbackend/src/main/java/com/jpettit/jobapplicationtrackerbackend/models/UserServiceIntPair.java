package com.jpettit.jobapplicationtrackerbackend.models;

import com.jpettit.jobapplicationtrackerbackend.helpers.DaoPair;

import java.util.Optional;

public class UserServiceIntPair implements DaoPair<Integer> {
    private Integer count;
    private String errorMessage;

    private UserServiceIntPair(Integer count, String errorMessage) {
        this.count = count;
        this.errorMessage = errorMessage;
    }

    public static UserServiceIntPair createPair(Integer count, String errorMessage) {
        return new UserServiceIntPair(count, errorMessage);
    }

    public Integer getValue() { return this.count; }

    public String getMessage() { return this.errorMessage; }
}

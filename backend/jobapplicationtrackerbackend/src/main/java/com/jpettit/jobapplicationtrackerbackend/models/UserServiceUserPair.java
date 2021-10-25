package com.jpettit.jobapplicationtrackerbackend.models;

import com.jpettit.jobapplicationtrackerbackend.helpers.DaoPair;

import java.util.Optional;

public class UserServiceUserPair implements DaoPair<Optional<User>> {
    Optional<User> user;
    String errorMessage;

    private UserServiceUserPair(Optional<User> user, String errorMessage) {
        this.user = user;
        this.errorMessage = errorMessage;
    }

    public static UserServiceUserPair createPair(Optional<User> user, String errorMessage) {
        return new UserServiceUserPair(user, errorMessage);
    }

    public Optional<User> getValue() {
        return this.user;
    }

    public String getMessage() {
        return this.errorMessage;
    }
}

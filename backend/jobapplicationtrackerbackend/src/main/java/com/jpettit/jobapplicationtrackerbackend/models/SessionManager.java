package com.jpettit.jobapplicationtrackerbackend.models;

import java.time.LocalDate;

public class SessionManager {
    public SessionManager() {

    }

    public User createNewUser(User user, String hashedPassword, String sessionName, LocalDate expDate) {
        final Session NEW_SESSION = Session.createSession(sessionName, expDate);
        return User.createUserFromUserPasswordSession(user, hashedPassword, NEW_SESSION);
    }
}

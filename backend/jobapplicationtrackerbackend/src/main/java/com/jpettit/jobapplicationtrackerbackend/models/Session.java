package com.jpettit.jobapplicationtrackerbackend.models;

import java.time.LocalDate;
import java.util.Objects;

public class Session {
    private final String sessionName;
    private final LocalDate expirationDate;

    private Session(String sessionName, LocalDate expirationDate) {
        this.sessionName = sessionName;
        this.expirationDate = expirationDate;
    }

    public static Session createSession(String sessionName, LocalDate expirationDate) {
        return new Session(sessionName, expirationDate);
    }

    public String getSessionName() {
        return sessionName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return sessionName.equals(session.sessionName) && expirationDate.equals(session.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionName, expirationDate);
    }
}

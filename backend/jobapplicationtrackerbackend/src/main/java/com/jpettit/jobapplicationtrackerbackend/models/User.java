package com.jpettit.jobapplicationtrackerbackend.models;

import java.time.LocalDate;
import java.util.Objects;

public class User {
    private final String username;
    private final String email;
    private String password;
    private Session session;
    private long userId;

    private User(String username, String email, String password, Session session, long userId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.session = session;
        this.userId = userId;
    }

    public static User createUser(String username, String email, String password, Session session, long id) {
        return new User(username, email, password, session, id);
    }

    public static User createUserNoId(String username, String email, String password, Session session) {
        return new User(username, email, password, session, -1L);
    }

    public static User createUserNoSessionOrId(String username, String email, String password) {
        final Session defaultSession = Session.createSession("", LocalDate.now());

        return new User(username, email, password, defaultSession, -1L);
    }

    public static User createEmptyUser() {
        final Session defaultSession = Session.createSession("", LocalDate.now());

        return new User("", "", "", defaultSession, -1L);
    }

    public static User createUserFromUser(final User user) {
        return new User(user.getUsername(), user.getEmail(), user.getPassword(),
                user.getSession(), user.getUserId());
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Session getSession() {
        return session;
    }

    public long getUserId() {
        return userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", session=" + session +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && username.equals(user.username) && email.equals(user.email) && password.equals(user.password) && session.equals(user.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, session, userId);
    }
}

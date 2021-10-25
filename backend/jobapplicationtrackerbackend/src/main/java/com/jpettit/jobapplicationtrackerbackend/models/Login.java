package com.jpettit.jobapplicationtrackerbackend.models;

public class Login {
    private final String username;
    private final String password;

    private Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Login  createLogin(String username, String password) {
        return new Login(username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

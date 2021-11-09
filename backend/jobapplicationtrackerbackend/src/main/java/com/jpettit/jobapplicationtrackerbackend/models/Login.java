package com.jpettit.jobapplicationtrackerbackend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
    private final String username;
    private final String password;

    public Login() {
        this.username = "";
        this.password = "";
    }

    @JsonCreator
    public Login(@JsonProperty("username") String username, @JsonProperty("password") String password) {
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

package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.models.Session;

public class UserQuerier extends Querier {
    private ProjectEnvironment env;

    public UserQuerier(ProjectEnvironment env) {
        setEnv(env);
    }

    private void setEnv(ProjectEnvironment env) {
        this.env = env;
    }

    @Override
    public String getTableName(ProjectEnvironment env) {
        if (env == ProjectEnvironment.TEST) {
            return "testusers";
        } else if (env == ProjectEnvironment.PROD) {
            return "users";
        } else {
            return "devusers";
        }
    }

    public String buildGetUserByIdQuery(Long id) {
        final String tableName = getTableName(env);

        return "SELECT userid, username, email, password, sessionname, expdate"
                + " FROM " + tableName + " WHERE userid = " + id + ";";
    }

    public String buildGetUserByUsername(String username) {
        final String tableName = getTableName(env);

        return String.format("SELECT userid, username, email, sessionname, expdate"
                + " FROM %s WHERE username LIKE '%s';", tableName, username);
    }

    public String buildGetAllUsersQuery() {
        final String tableName = getTableName(env);
        return String.format("SELECT userid, username, email, password, sessionname, expdate" +
                " FROM %s ORDER BY userid ASC;", tableName);
    }

    public String buildGetPasswordForUserQuery(String username) {
        final String TABLE_NAME = getTableName(env);

        return String.format("SELECT password FROM %s WHERE username LIKE '%s';", TABLE_NAME, username);
    }

    public String buildInsertOneUserQuery() {
        final String tableName = getTableName(env);

        return "INSERT INTO " + tableName + " (username, email, password, sessionname, expdate)" +
                "VALUES (?, ?, ?, ?, ?)";
    }

    public String buildUpdateSession() {
        final String TABLE_NAME = getTableName(env);

        return String.format("UPDATE %s SET sessionname = ?, expdate = ? WHERE username = ?;", TABLE_NAME);
    }
}

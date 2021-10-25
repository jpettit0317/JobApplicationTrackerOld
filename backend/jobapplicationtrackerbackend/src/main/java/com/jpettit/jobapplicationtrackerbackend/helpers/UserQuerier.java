package com.jpettit.jobapplicationtrackerbackend.helpers;

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

    public String buildInsertOneUserQuery() {
        final String tableName = getTableName(env);

        return "INSERT INTO " + tableName + " (username, email, password, sessionname, expdate)" +
                "VALUES (?, ?, ?, ?, ?)";
    }
}

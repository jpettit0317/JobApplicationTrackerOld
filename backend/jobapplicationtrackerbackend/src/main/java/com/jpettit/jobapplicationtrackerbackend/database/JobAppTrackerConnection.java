package com.jpettit.jobapplicationtrackerbackend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class JobAppTrackerConnection {
    public static Optional<Connection> createConnection() {
        try {
            final String dbURL = "jdbc:postgresql://localhost/jobapptracker";
            final String user = "guest";
            final String password = "pgpassword";

            return Optional.ofNullable(DriverManager.getConnection(dbURL, user, password));
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}

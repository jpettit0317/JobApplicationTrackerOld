package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.enums.JobApplicationFields;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobAppQuerier;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplication;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
public class JobApplicationDAO implements DAO<JobApplication>{
    @Autowired
    JobApplicationDaoInfoBuilder builder;

    private final ProjectEnvironment environment;

    public static final String EMPTY_USERNAME = "Username is empty.";

    public JobApplicationDAO(@Value(AppProperties.appEnv) String env) {
        this.environment = ProjectEnvironmentReader.getEnvironment(env);
        this.builder = new JobApplicationDaoInfoBuilder(env);
    }

    @Override
    public int insertOne(JobApplication jobApplication) {
        return 0;
    }

    @Override
    public JobApplication update(JobApplication jobApplication) {
        return null;
    }

    @Override
    public JobApplication delete(JobApplication jobApplication) {
        return null;
    }

    public Connection getConnection() {
        return JobAppTrackerConnection.createConnection().get();
    }

    public void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultPair<ArrayList<JobApplicationCard>> getJobAppCards(final String USERNAME) {
        if (USERNAME.equals("")) {
            return new ResultPair<>(new ArrayList<>(), EMPTY_USERNAME);
        }
        try {
            final ResultPair<ArrayList<JobApplicationCard>> RESULT = builder.getJobAppCards(USERNAME);
            return RESULT;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return createErrorCards(ex.getMessage());
        }
    }

    private ResultPair<ArrayList<JobApplicationCard>> createErrorCards(final String ERR) {
        return new ResultPair<>(new ArrayList<>(), ERR);
    }
}

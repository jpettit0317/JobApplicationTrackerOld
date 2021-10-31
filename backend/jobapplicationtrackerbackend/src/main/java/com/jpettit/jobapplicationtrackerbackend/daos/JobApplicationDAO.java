package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.enums.JobApplicationFields;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobAppQuerier;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplication;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class JobApplicationDAO implements DAO<JobApplication>{
    private final Connection connection;
    private final ProjectEnvironment environment;

    public JobApplicationDAO(Connection connection, ProjectEnvironment environment) {
        this.connection = connection;
        this.environment = environment;
    }

    public static JobApplicationDAO createDAO(Connection conn, ProjectEnvironment env) {
        return new JobApplicationDAO(conn, env);
    }

    @Override
    public Optional<JobApplication> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public ArrayList<JobApplication> getAll() {
        return null;
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

    @Override
    public int insertMany(Collection<JobApplication> list) {
        return 0;
    }

    @Override
    public void closeConnection() throws SQLException {
        connection.close();
    }

    public ResultPair<ArrayList<JobApplicationCard>> getJobAppCards(String username) {
        final JobAppQuerier QUERIER = new JobAppQuerier(environment);
        final ResultPair<String> QUERY = QUERIER.getAllJobAppCards(username);

        if (QUERY.getValue().equals("")) {
            return new ResultPair<>(new ArrayList<>(), QUERY.getMessage());
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(QUERY.getValue());
            final ArrayList<JobApplicationCard> CARDS = buildJobAppCardsForUsername(set);
            return new ResultPair<>(CARDS, "");
        } catch (SQLException ex) {
            return new ResultPair<>(new ArrayList<>(), ex.getMessage());
        }
    }

    private ArrayList<JobApplicationCard> buildJobAppCardsForUsername(ResultSet set) {
        ArrayList<JobApplicationCard> cards = new ArrayList<>();
        try {
            while (set.next()) {
                final String JOB_TITLE = set.getString(JobApplicationFields.jobTitle);
                final String NAME = set.getString(JobApplicationFields.company);
                final int COUNT = set.getInt(JobApplicationFields.interviewCount);
                final String ID = set.getString(JobApplicationFields.jobappId);

                final JobApplicationCard CARD = JobApplicationCard.createCard(JOB_TITLE,
                        NAME, COUNT, ID);
                cards.add(CARD);
            }
            return cards;
        } catch (SQLException ex) {
            return new ArrayList<>();
        }
    }
}

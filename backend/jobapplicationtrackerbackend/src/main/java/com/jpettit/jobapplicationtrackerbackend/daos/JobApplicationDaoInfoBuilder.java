package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.enums.JobApplicationFields;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobAppQuerier;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JobApplicationDaoInfoBuilder {
    private final ProjectEnvironment environment;

    public static final String CREATE_CONN_ERR_MSG = "Couldn't create session.";

    public JobApplicationDaoInfoBuilder(@Value(AppProperties.appEnv) String env) {
        environment = ProjectEnvironmentReader.getEnvironment(env);
    }

    private Optional<Connection> createConnection() {
        final Optional<Connection> CONN = JobAppTrackerConnection.createConnection();

        if (CONN.isPresent()) {
            return CONN;
        } else {
            return Optional.empty();
        }
    }

    public ResultPair<ArrayList<JobApplicationCard>> getJobAppCards(final String USERNAME) throws SQLException {
        final JobAppQuerier QUERIER = new JobAppQuerier(environment);
        final ResultPair<String> QUERY = QUERIER.getAllJobAppCards(USERNAME);
        final Optional<Connection> connection =  createConnection();

        if (!connection.isPresent()) {
            return new ResultPair<>(new ArrayList(), CREATE_CONN_ERR_MSG);
        }

        if (QUERY.getValue().equals("")) {
            return new ResultPair<>(new ArrayList<>(), QUERY.getMessage());
        }
        Statement statement = connection.get().createStatement();
        ResultSet set = statement.executeQuery(QUERY.getValue());
        final ArrayList<JobApplicationCard> CARDS = buildJobAppCardsForUsername(set);
        return new ResultPair<>(CARDS, "");
    }

    private ArrayList<JobApplicationCard> buildJobAppCardsForUsername(ResultSet set) throws SQLException {
        ArrayList<JobApplicationCard> cards = new ArrayList<>();
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
    }
}

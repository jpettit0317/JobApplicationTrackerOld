package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class UserDaoInfoBuilder {
    private final Connection connection;
    private final ProjectEnvironment environment;

    public UserDaoInfoBuilder(Connection conn,
                              @Value(AppProperties.appEnv) String env) {
        connection = JobAppTrackerConnection.createConnection().get();
        environment = ProjectEnvironmentReader.getEnvironment(env);
    }


}

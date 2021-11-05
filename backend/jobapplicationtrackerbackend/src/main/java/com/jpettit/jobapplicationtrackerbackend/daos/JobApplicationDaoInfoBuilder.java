package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationDaoInfoBuilder {
    private final ProjectEnvironment environment;

    public JobApplicationDaoInfoBuilder(@Value(AppProperties.appEnv) String env) {
        environment = ProjectEnvironmentReader.getEnvironment(env);
    }

}

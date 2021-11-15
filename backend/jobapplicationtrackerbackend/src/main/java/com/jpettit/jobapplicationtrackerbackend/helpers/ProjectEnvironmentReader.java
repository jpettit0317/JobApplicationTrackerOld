package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.enums.ProjectEnvironment;

import java.util.HashMap;

public class ProjectEnvironmentReader {
    public static ProjectEnvironment getEnvironment(String envArg) {
        HashMap<String, ProjectEnvironment> envs = new HashMap<>();

        envs.put("dev", ProjectEnvironment.DEV);
        envs.put("prod", ProjectEnvironment.PROD);
        envs.put("test", ProjectEnvironment.TEST);

        return envs.getOrDefault(envArg, ProjectEnvironment.DEV);
    }
}

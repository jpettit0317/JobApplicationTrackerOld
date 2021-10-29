package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;

import java.util.HashMap;

public class JobAppQuerier {
    private final ProjectEnvironment environment;
    private final String INVALID_USERNAME = "Username is empty";
    private final String INVALIDTABLENAME = "Table name is empty";

    public JobAppQuerier(ProjectEnvironment env) {
        this.environment = env;
    }

    private String getUserTableName() {
        return buildUserTableName().getOrDefault(environment, "");
    }

    private String getInterviewTableName() {
        return buildInterviewTableName().getOrDefault(environment, "");
    }

    private String getJobAppTableName() {
        return buildJobTableName().getOrDefault(environment, "");
    }

    private HashMap<ProjectEnvironment, String> buildUserTableName() {
        HashMap<ProjectEnvironment, String> map = new HashMap<>();

        map.put(ProjectEnvironment.DEV, "devusers");
        map.put(ProjectEnvironment.PROD, "users");
        map.put(ProjectEnvironment.TEST, "testusers");

        return map;
    }

    private HashMap<ProjectEnvironment, String> buildJobTableName() {
        HashMap<ProjectEnvironment, String> map = new HashMap<>();

        map.put(ProjectEnvironment.DEV, "devjobapps");
        map.put(ProjectEnvironment.PROD, "jobapps");
        map.put(ProjectEnvironment.TEST, "testjobapps");

        return map;
    }

    private HashMap<ProjectEnvironment, String> buildInterviewTableName() {
        HashMap<ProjectEnvironment, String> map = new HashMap<>();

        map.put(ProjectEnvironment.DEV, "devinterviews");
        map.put(ProjectEnvironment.PROD, "interviews");
        map.put(ProjectEnvironment.TEST, "testinterviews");

        return map;
    }

    public ResultPair<String> getAllJobAppCards(String username) {
        return buildGetAllJobAppCardsQuery(username);
    }

    private ResultPair<String> buildGetAllJobAppCardsQuery(String username) {
       final String JOBAPP_TITLENAME = getJobAppTableName();

       if (username.equals("")) {
           return new ResultPair<>("", this.INVALID_USERNAME);
       } else if (JOBAPP_TITLENAME.equals("")) {
           return new ResultPair<>("", this.INVALIDTABLENAME);
       }
       final String QUERY = String.format("SELECT jobappid, jobapptitle, company, interviewcount FROM %s WHERE applicant = '%s';"
               ,JOBAPP_TITLENAME, username);

       return new ResultPair<>(QUERY, "");
    }
}

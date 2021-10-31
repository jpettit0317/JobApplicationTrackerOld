package com.jpettit.jobapplicationtrackerbackend.services;

import com.jpettit.jobapplicationtrackerbackend.daos.JobApplicationDAO;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;

import java.util.ArrayList;

public class JobApplicationService {
    private JobApplicationDAO jobAppDAO;

    public JobApplicationService(JobApplicationDAO dao) {
        jobAppDAO = dao;
    }

    public ResultPair<ArrayList<JobApplicationCard>> getJobAppCardsBySessionId(String username) {
       return jobAppDAO.getJobAppCards(username);
    }
}

package com.jpettit.jobapplicationtrackerbackend.services;

import com.jpettit.jobapplicationtrackerbackend.daos.JobApplicationDAO;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationDAO jobAppDAO;

    public JobApplicationService(JobApplicationDAO dao) {
        jobAppDAO = dao;
    }

    public ResultPair<ArrayList<JobApplicationCard>> getJobAppCardsBySessionId(String username) {
       return jobAppDAO.getJobAppCards(username);
    }
}

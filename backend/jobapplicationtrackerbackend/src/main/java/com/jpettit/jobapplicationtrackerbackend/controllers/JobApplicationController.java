package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.JobapplicationtrackerbackendApplication;
import com.jpettit.jobapplicationtrackerbackend.daos.JobApplicationDAO;
import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobApplicationURLS;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.PasswordEncoder;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.SessionManager;
import com.jpettit.jobapplicationtrackerbackend.services.JobApplicationService;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class JobApplicationController {
    @GetMapping(JobApplicationURLS.getJobAppCard)
    public ResponseEntity<ResultPair<ArrayList<JobApplicationCard>>> getJobAppCards(@PathVariable String sessionId) {
        System.out.println("The sessionId is " + sessionId);

        final UserService SERVICE = createUserService();
        final JobApplicationService JOBAPP_SERVICE = createJobAppService();

        final ResultPair<String> PAIR = SERVICE.getUsernameBySessionId(sessionId);

        System.out.println("PAIR = " + PAIR.toString());
        if (!PAIR.getMessage().equals("")) {
            final ResultPair<ArrayList<JobApplicationCard>> RESULT = new ResultPair<>(new ArrayList<>(), "Couldn't get username");
            return new ResponseEntity<>(RESULT, HttpStatus.NOT_FOUND);
        }
        final ResultPair<ArrayList<JobApplicationCard>> jobAppCardPair = JOBAPP_SERVICE.getJobAppCardsBySessionId(PAIR.getValue());

        if (!jobAppCardPair.getMessage().equals("")) {
            return new ResponseEntity<>(new ResultPair<>(new ArrayList<>(), "Couldn't get jobapps"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(jobAppCardPair, HttpStatus.OK);
    }

    private UserService createUserService() {
        final Optional<Connection> connection = JobAppTrackerConnection.createConnection();
        final ProjectEnvironment appEnvironment = JobapplicationtrackerbackendApplication.environment;
        final PasswordEncoder encoder = PasswordEncoder.createPasswordEncoder(12);

        System.out.println(connection.get().toString());
        System.out.println("The app environment is " + appEnvironment);
        UserDAO dao = new UserDAO(connection.get(), appEnvironment);

        return new UserService(dao, 12, encoder, new SessionManager());
    }

    private JobApplicationService createJobAppService() {
        final Optional<Connection> connection = JobAppTrackerConnection.createConnection();
        final ProjectEnvironment appEnvironment = JobapplicationtrackerbackendApplication.environment;
        JobApplicationDAO dao = new JobApplicationDAO(connection.get(), appEnvironment);

        return new JobApplicationService(dao);
    }
}

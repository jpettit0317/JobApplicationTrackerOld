package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.JobapplicationtrackerbackendApplication;
import com.jpettit.jobapplicationtrackerbackend.daos.JobApplicationDAO;
import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobApplicationURLS;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.PasswordEncoder;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.SessionManager;
import com.jpettit.jobapplicationtrackerbackend.services.JobApplicationService;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class JobApplicationController {
    @Value(AppProperties.appEnv)
    String environment;

    private String USERNAME_NOT_FOUND = "Couldn't find username.";
    private String SESSION_HAS_EXPIRED_MSG = "Session has expired.";

    @GetMapping(JobApplicationURLS.getJobAppCard)
    public ResponseEntity<ResultPair<ArrayList<JobApplicationCard>>> getJobAppCards(@PathVariable String sessionId) {
        final UserService SERVICE = createUserService();
        final JobApplicationService JOBAPP_SERVICE = createJobAppService();

        final ResultPair<Boolean> SESSION_EXPIRED_PAIR = checkIfSessionHasExpired(SERVICE, sessionId);
        if (SESSION_EXPIRED_PAIR.getValue()) {
            final ResultPair<ArrayList<JobApplicationCard>> EXPIRED_SESSION = getBadResult(SESSION_HAS_EXPIRED_MSG);
            return new ResponseEntity<>(EXPIRED_SESSION, HttpStatus.NOT_FOUND);
        }

        final ResultPair<String> USERNAME = getUsernameBySessionId(SERVICE, sessionId);
        if (!USERNAME.getMessage().equals("")) {
            final ResultPair<ArrayList<JobApplicationCard>> BAD_USERNAME = getBadResult(USERNAME.getMessage());
            return new ResponseEntity<>(BAD_USERNAME, HttpStatus.NOT_FOUND);
        }

        final ResultPair<ArrayList<JobApplicationCard>> JOB_APPS = JOBAPP_SERVICE.getJobAppCardsBySessionId(USERNAME.getValue());
        return new ResponseEntity<>(JOB_APPS, HttpStatus.OK);
    }

    private ResultPair<ArrayList<JobApplicationCard>> getBadResult(final String ERR_MSG) {
        return new ResultPair<ArrayList<JobApplicationCard>>(new ArrayList<>(), ERR_MSG);
    }

    private ResultPair<Boolean> checkIfSessionHasExpired(final UserService SERVICE,
                                                         final String ID) {
        final LocalDate ACCESS_DATE = LocalDate.now();

        return SERVICE.hasSessionExpired(ACCESS_DATE, ID);
    }

    private ResultPair<String> getUsernameBySessionId(final UserService SERVICE, final String ID) {
        return SERVICE.getUsernameBySessionId(ID);
    }

    private void getJobAppCards(ResultPair<ArrayList<JobApplicationCard>> cardPair,
                                final JobApplicationService SERVICE,
                                final String USERNAME) {
        cardPair = SERVICE.getJobAppCardsBySessionId(USERNAME);
    }

    private boolean getUsernameBySessionId(final ResultPair<String> PAIR) {
        return PAIR.getMessage().equals("");
    }

    private UserService createUserService() {
        final Optional<Connection> connection = JobAppTrackerConnection.createConnection();
        final ProjectEnvironment appEnvironment = ProjectEnvironmentReader.getEnvironment(environment);
        final PasswordEncoder encoder = PasswordEncoder.createPasswordEncoder(12);

        UserDAO dao = new UserDAO(connection.get(), appEnvironment);

        return new UserService(dao, 12, encoder, new SessionManager());
    }

    private JobApplicationService createJobAppService() {
        final Optional<Connection> connection = JobAppTrackerConnection.createConnection();
        final ProjectEnvironment appEnvironment = ProjectEnvironmentReader.getEnvironment(environment);
        JobApplicationDAO dao = new JobApplicationDAO(connection.get(), appEnvironment);

        return new JobApplicationService(dao);
    }
}

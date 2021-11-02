package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobApplicationURLS;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.services.JobApplicationService;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
public class JobApplicationController {
    @Value(AppProperties.appEnv)
    String environment;

    @Autowired
    private final UserService USER_SERVICE;

    @Autowired
    private final JobApplicationService JOB_APP_SERVICE;

    public JobApplicationController(UserService userService,
                                    JobApplicationService jobAppService) {
        USER_SERVICE = userService;
        JOB_APP_SERVICE = jobAppService;
    }

    private String USERNAME_NOT_FOUND = "Couldn't find username.";
    private String SESSION_HAS_EXPIRED_MSG = "Session has expired.";

    @GetMapping(JobApplicationURLS.getJobAppCard)
    public ResponseEntity<ResultPair<ArrayList<JobApplicationCard>>> getJobAppCards(@PathVariable String sessionId) {
        final ResultPair<Boolean> SESSION_EXPIRED_PAIR = checkIfSessionHasExpired(sessionId);
        if (SESSION_EXPIRED_PAIR.getValue()) {
            final ResultPair<ArrayList<JobApplicationCard>> EXPIRED_SESSION = getBadResult(SESSION_HAS_EXPIRED_MSG);
            return new ResponseEntity<>(EXPIRED_SESSION, HttpStatus.NOT_FOUND);
        }

        final ResultPair<String> USERNAME = getUsernameBySessionId(sessionId);
        if (!USERNAME.getMessage().equals("")) {
            final ResultPair<ArrayList<JobApplicationCard>> BAD_USERNAME = getBadResult(USERNAME.getMessage());
            return new ResponseEntity<>(BAD_USERNAME, HttpStatus.NOT_FOUND);
        }

        final ResultPair<ArrayList<JobApplicationCard>> JOB_APPS = JOB_APP_SERVICE.getJobAppCardsBySessionId(USERNAME.getValue());
        return new ResponseEntity<>(JOB_APPS, HttpStatus.OK);
    }

    private ResultPair<ArrayList<JobApplicationCard>> getBadResult(final String ERR_MSG) {
        return new ResultPair<ArrayList<JobApplicationCard>>(new ArrayList<>(), ERR_MSG);
    }

    private ResultPair<Boolean> checkIfSessionHasExpired(final String ID) {
        final LocalDate ACCESS_DATE = LocalDate.now();

        return USER_SERVICE.hasSessionExpired(ACCESS_DATE, ID);
    }

    private ResultPair<String> getUsernameBySessionId(final String ID) {
        return USER_SERVICE.getUsernameBySessionId(ID);
    }

    private boolean getUsernameBySessionId(final ResultPair<String> PAIR) {
        return PAIR.getMessage().equals("");
    }
}

package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.enums.JobApplicationURLS;
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

    private String USERNAME_NOT_FOUND = "Couldn't find username.";
    private String SESSION_HAS_EXPIRED_MSG = "Session has expired.";
    private String SESSION_ID_EMPTY = "Session id is empty.";

    private final HttpStatus STATUS_OK = HttpStatus.OK;
    private final HttpStatus STATUS_CREATED = HttpStatus.CREATED;
    private final HttpStatus STATUS_NOTFOUND = HttpStatus.NOT_FOUND;
    private final HttpStatus STATUS_FORBIDDEN = HttpStatus.FORBIDDEN;

    public JobApplicationController(UserService userService,
                                    JobApplicationService jobAppService) {
        USER_SERVICE = userService;
        JOB_APP_SERVICE = jobAppService;
    }



    @GetMapping(JobApplicationURLS.getJobAppCard)
    public ResponseEntity<ResultPair<ArrayList<JobApplicationCard>>> getJobAppCards(@PathVariable String sessionId) {
        if (isSessionEmpty(sessionId)) {
            final ResultPair<ArrayList<JobApplicationCard>> PAIR = getBadResult(SESSION_ID_EMPTY);
           return new ResponseEntity<>(PAIR, STATUS_FORBIDDEN);
        }

        final ResultPair<Boolean> SESSION_EXPIRED_PAIR = checkIfSessionHasExpired(sessionId);
        if (SESSION_EXPIRED_PAIR.getValue()) {
            final ResultPair<ArrayList<JobApplicationCard>> EXPIRED_SESSION = getBadResult(SESSION_HAS_EXPIRED_MSG);
            return new ResponseEntity<>(EXPIRED_SESSION, STATUS_FORBIDDEN);
        }

        final ResultPair<String> USERNAME = getUsernameBySessionId(sessionId);
        if (!USERNAME.getMessage().equals("")) {
            final ResultPair<ArrayList<JobApplicationCard>> BAD_USERNAME = getBadResult(USERNAME.getMessage());
            return new ResponseEntity<>(BAD_USERNAME, STATUS_NOTFOUND);
        }

        final ResultPair<ArrayList<JobApplicationCard>> JOB_APPS = JOB_APP_SERVICE.getJobAppCardsBySessionId(USERNAME.getValue());
        if (!JOB_APPS.getMessage().equals("")) {
            final ResultPair<ArrayList<JobApplicationCard>> RESULT = getGoodResult(JOB_APPS.getValue(), JOB_APPS.getMessage());
            return new ResponseEntity<>(RESULT, STATUS_NOTFOUND);
        }
        return new ResponseEntity<>(JOB_APPS, STATUS_OK);
    }

    private ResultPair<ArrayList<JobApplicationCard>> getBadResult(final String ERR_MSG) {
        return new ResultPair<ArrayList<JobApplicationCard>>(new ArrayList<>(), ERR_MSG);
    }

    private ResultPair<ArrayList<JobApplicationCard>> getGoodResult(final ArrayList<JobApplicationCard> CARDS,
                                                                    final String ERR_MSG) {
        return new ResultPair<>(CARDS, ERR_MSG);
    }

    private ResultPair<Boolean> checkIfSessionHasExpired(final String ID) {
        if (ID.equals("")) {
            return new ResultPair<>(false, SESSION_ID_EMPTY);
        }

        final LocalDate ACCESS_DATE = LocalDate.now();


        return USER_SERVICE.hasSessionExpired(ACCESS_DATE, ID);
    }

    private boolean isSessionEmpty(final String ID) {
        return ID.equals("");
    }

    private ResultPair<String> getUsernameBySessionId(final String ID) {
        return USER_SERVICE.getUsernameBySessionId(ID);
    }

    private boolean getUsernameBySessionId(final ResultPair<String> PAIR) {
        return PAIR.getMessage().equals("");
    }
}

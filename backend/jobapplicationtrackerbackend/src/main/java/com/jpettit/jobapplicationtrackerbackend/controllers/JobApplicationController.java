package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.helpers.JobApplicationURLS;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobApplicationController {
    @GetMapping(JobApplicationURLS.getJobAppCard)
    public ResponseEntity<String[]> getJobAppCards(@PathVariable String sessionId) {
        System.out.println("The sessionId is " + sessionId);
        final String[] RESULT = {"Success"};

        return new ResponseEntity<>(RESULT, HttpStatus.OK);
    }
}

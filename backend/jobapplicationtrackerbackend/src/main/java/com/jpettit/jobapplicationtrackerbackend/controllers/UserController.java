package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.helpers.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostConstruct
    public void postConstruct() {
        System.out.println("In post construct at time ");
    }

    @PostMapping(UserControllerURL.addUser)
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        System.out.println("Added user at time .");

        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

//    private String getTime() {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//
//        return dtf.format(now);
//    }
}

package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.JobapplicationtrackerbackendApplication;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostConstruct
    public void postConstruct() {
        System.out.println("In post construct at time ");
        System.out.println("Environment is " + JobapplicationtrackerbackendApplication.environment);
    }

    @PostMapping(UserControllerURL.addUser)
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        System.out.println("Added user at time .");
        System.out.println("The new user is " + newUser.toString());

        return new ResponseEntity<>("", HttpStatus.CREATED);
    }
}
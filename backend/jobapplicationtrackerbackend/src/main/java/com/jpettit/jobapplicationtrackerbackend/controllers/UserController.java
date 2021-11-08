package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.JobapplicationtrackerbackendApplication;
import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.*;
import com.jpettit.jobapplicationtrackerbackend.services.JobApplicationService;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Objects;
import java.util.Optional;

@RestController
public class UserController {
    @Value(AppProperties.appEnv)
    String environment;

    @Autowired
    UserService userService;

    @PostMapping(UserControllerURL.addUser)
    public ResponseEntity<ResultPair<String>> addUser(@RequestBody User newUser) {
        final ResultPair<String> PAIR = userService.createUser(newUser);

        if(!PAIR.getMessage().equals("")) {
            return new ResponseEntity<>(PAIR, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(PAIR, HttpStatus.CREATED);
        }
    }

    @PostMapping(UserControllerURL.loginUser)
    public ResponseEntity<String> loginUser(@RequestBody Login login) {
        final ResultPair<String> result = userService.validateUserLogin(login);

        if (result.getMessage().equals("")) {
            return new ResponseEntity<>(result.getValue(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
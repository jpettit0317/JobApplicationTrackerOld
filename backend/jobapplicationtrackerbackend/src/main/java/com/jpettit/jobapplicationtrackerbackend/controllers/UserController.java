package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.enums.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.*;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResultPair<String>> loginUser(@RequestBody Login login) {
        final ResultPair<String> result = userService.validateUserLogin(login);

        if (result.getMessage().equals("")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.helpers.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @PostMapping(UserControllerURL.addUser)
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        System.out.println("Added user.");

        return new ResponseEntity<>("", HttpStatus.CREATED);
    }
}

package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.JobapplicationtrackerbackendApplication;
import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.HttpResponse;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.models.UserServiceIntPair;
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
import java.util.Objects;
import java.util.Optional;

@RestController
public class UserController {
    @PostConstruct
    public void postConstruct() {
        System.out.println("In post construct at time ");
        System.out.println("Environment is " + JobapplicationtrackerbackendApplication.environment);
    }

    @PostMapping(UserControllerURL.addUser)
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        System.out.println("In addUser");
        final UserService service = createUserService();
        final UserServiceIntPair result = service.createUser(newUser);

        System.out.println(result.toString());
        if(!result.getMessage().equals("")) {
            final HttpResponse<String> response = new HttpResponse<>("", result.getMessage());
            System.out.println(String.format("The response is %s", response.toString()));
            return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.NOT_FOUND);
        } else {
            final HttpResponse<String> response = new HttpResponse<>("", "");
            return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.CREATED);
        }
    }

    @PostMapping(UserControllerURL.loginUser)
    public ResponseEntity<String> loginUser(@RequestBody Login login) {
        System.out.println("In addUser");

        System.out.println("Logging in user " + login.toString());

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private UserService createUserService() {
        final Optional<Connection> connection = JobAppTrackerConnection.createConnection();
        final ProjectEnvironment appEnvironment = JobapplicationtrackerbackendApplication.environment;

        System.out.println(connection.get().toString());
        System.out.println("The app environment is " + appEnvironment);
        UserDAO dao = new UserDAO(connection.get(), appEnvironment);

        return new UserService(dao, 12);
    }
}
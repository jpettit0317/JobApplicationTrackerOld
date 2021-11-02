package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.JobapplicationtrackerbackendApplication;
import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.database.JobAppTrackerConnection;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.helpers.ProjectEnvironmentReader;
import com.jpettit.jobapplicationtrackerbackend.helpers.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.*;
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

    @PostMapping(UserControllerURL.addUser)
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        final UserService service = createUserService();
        final UserServiceResultPair<String> PAIR = service.createUser(newUser);

        if(!PAIR.getMessage().equals("")) {
            return new ResponseEntity<>(PAIR.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(PAIR.getValue(), HttpStatus.CREATED);
        }
    }

    @PostMapping(UserControllerURL.loginUser)
    public ResponseEntity<String> loginUser(@RequestBody Login login) {
        final UserService service = createUserService();
        final UserServiceResultPair<String> result = service.validateUserLogin(login);

        if (result.getMessage().equals("")) {
            return new ResponseEntity<>(result.getValue(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private UserService createUserService() {
        final Optional<Connection> connection = JobAppTrackerConnection.createConnection();
        final ProjectEnvironment appEnvironment = ProjectEnvironmentReader.getEnvironment(environment);
        final PasswordEncoder encoder = PasswordEncoder.createPasswordEncoder(12);

        UserDAO dao = new UserDAO(connection.get(), appEnvironment);

        return new UserService(dao, 12, encoder, new SessionManager());
    }
}
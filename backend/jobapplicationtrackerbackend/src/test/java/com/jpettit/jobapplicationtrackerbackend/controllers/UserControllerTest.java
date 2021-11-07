package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBootConfiguration
@TestPropertySource(properties = {
        "application.env=test",
        "application.encryption.rounds=12"
})
class UserControllerTest {
    private MockMvc mockMvc;

    @Value(AppProperties.appEnv)
    String environment;

    @Mock
    private UserController sut;

    @InjectMocks
    private UserService userService;


}
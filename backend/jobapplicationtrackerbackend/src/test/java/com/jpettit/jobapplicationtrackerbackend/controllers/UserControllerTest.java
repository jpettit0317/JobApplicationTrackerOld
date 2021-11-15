package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import testhelpers.helpervars.JSONHelper;
import testhelpers.helpers.UserControllerTestHelper;
import testhelpers.helpervars.UserControllerTestHelperVars;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = {
        "application.env=test",
        "application.encryption.rounds=12"
})
class UserControllerTest {
    private MockMvc mockMvc;

    @Value(AppProperties.appEnv)
    String environment;

    @InjectMocks
    private UserController sut;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUpBeforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @AfterEach
    void afterEach() {
        sut = null;
    }

    private HttpStatus SUCCESS = HttpStatus.OK;
    private HttpStatus CREATED = HttpStatus.CREATED;
    private HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

    @Test
    public void testCreateUser_whenUserIsSuccessfullyCreated_shouldReturnSessionIdAndNoErrorMessage() throws Exception {
        final User USER = UserControllerTestHelperVars.createDefaultUser();
        final Session SESSION = USER.getSession();
        final ResultPair<String> PAIR = new ResultPair<>(SESSION.getSessionName(), "");

        Mockito.when(userService.createUser(USER)).thenReturn(PAIR);

        try {
            final UserControllerTestHelper<ResultPair<String>> CONTENTS = new UserControllerTestHelper<>();
            CONTENTS.buildResultForCreateUser(mockMvc, USER, CREATED);
            final String RESPONSE = CONTENTS.getResponse();
            final ResultPair<String> ACTUAL = JSONHelper.convertJSONResponseToStringResultPair(RESPONSE);
            CONTENTS.setActualAndExpected(PAIR, ACTUAL);
            CONTENTS.verifyEqual();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assertions.fail(ex.getMessage());
        }
    }

    @Test
    public void testCreateUser_whenUserAlreadyExists_shouldReturnNoValueAndUsernameExists() throws Exception {
        final User USER = UserControllerTestHelperVars.createDefaultUser();
        final ResultPair<String> PAIR = new ResultPair<>("", userService.USERNAME_EXISTS);

        Mockito.when(userService.createUser(USER)).thenReturn(PAIR);

        try {
            final UserControllerTestHelper<ResultPair<String>> CONTENTS = new UserControllerTestHelper<>();
            CONTENTS.buildResultForCreateUser(mockMvc, USER, NOT_FOUND);
            final String RESPONSE = CONTENTS.getResponse();
            final ResultPair<String> ACTUAL = JSONHelper.convertJSONResponseToStringResultPair(RESPONSE);
            CONTENTS.setActualAndExpected(PAIR, ACTUAL);
            CONTENTS.verifyEqual();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assertions.fail(ex.getMessage());
        }
    }

    @Test
    public void testAddUser_whenUserCantBeInserted_shouldReturnNoValueAndUserNotCreated() {
        final User USER = UserControllerTestHelperVars.createDefaultUser();
        final ResultPair<String> PAIR = new ResultPair<>("", userService.USER_NOT_CREATED);

        Mockito.when(userService.createUser(USER)).thenReturn(PAIR);

        try {
            final UserControllerTestHelper<ResultPair<String>> CONTENTS = new UserControllerTestHelper<>();
            CONTENTS.buildResultForCreateUser(mockMvc, USER, NOT_FOUND);
            final String RESPONSE = CONTENTS.getResponse();
            final ResultPair<String> ACTUAL = JSONHelper.convertJSONResponseToStringResultPair(RESPONSE);
            CONTENTS.setActualAndExpected(PAIR, ACTUAL);
            CONTENTS.verifyEqual();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assertions.fail(ex.getMessage());
        }
    }

    // loginUser tests
    @Test
    public void testLoginUser_whenGivenValidLogin_shouldReturnToken() {
        final User USER = UserControllerTestHelperVars.createDefaultUser();
        final Session SESSION = USER.getSession();
        final Login LOGIN = Login.createLogin(USER.getUsername(), USER.getPassword());
        final ResultPair<String> PAIR = new ResultPair<>(SESSION.getSessionName(), "");

        Mockito.when(userService.validateUserLogin(Mockito.any(Login.class))).thenReturn(PAIR);

        try {
            final UserControllerTestHelper<ResultPair<String>> CONTENTS = new UserControllerTestHelper<>();
            CONTENTS.buildResultForLoginUser(mockMvc, LOGIN, SUCCESS);
            final String RESPONSE = CONTENTS.getResponse();
            final ResultPair<String> ACTUAL = JSONHelper.convertJSONResponseToStringResultPair(RESPONSE);
            CONTENTS.setActualAndExpected(PAIR, ACTUAL);
            CONTENTS.verifyEqual();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assertions.fail(ex.getMessage());
        }
    }

    @Test
    public void testLoginUser_whenGivenLoginWhereUsernameDoesntExist_shouldReturnNoValueAndUserDoesntExist() {
        final User USER = UserControllerTestHelperVars.createDefaultUser();
        final Login LOGIN = Login.createLogin(USER.getUsername(), USER.getPassword());
        final ResultPair<String> PAIR = new ResultPair<>("", "Username not found");

        Mockito.when(userService.validateUserLogin(Mockito.any(Login.class))).thenReturn(PAIR);

        try {
            final UserControllerTestHelper<ResultPair<String>> CONTENTS = new UserControllerTestHelper<>();
            CONTENTS.buildResultForLoginUser(mockMvc, LOGIN, NOT_FOUND);
            final String RESPONSE = CONTENTS.getResponse();
            final ResultPair<String> ACTUAL = JSONHelper.convertJSONResponseToStringResultPair(RESPONSE);
            CONTENTS.setActualAndExpected(PAIR, ACTUAL);
            CONTENTS.verifyEqual();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assertions.fail(ex.getMessage());
        }
    }
}
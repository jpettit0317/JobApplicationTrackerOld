package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.daos.JobApplicationDAO;
import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobApplicationControllerTestHelper;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobApplicationURLS;
import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.services.JobApplicationService;
import com.jpettit.jobapplicationtrackerbackend.services.UserService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@SpringBootConfiguration
@TestPropertySource(properties = {
        "application.env=test"
})
class JobApplicationControllerTest {
    private MockMvc mockMvc;

    @Value(AppProperties.appEnv)
    String environment;

    @InjectMocks
    private JobApplicationController sut;

    @Mock
    private UserService userService;

    @Mock
    private JobApplicationService jobAppService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JobApplicationDAO jobAppDAO;

    @BeforeEach
    void setUpBeforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @AfterEach
    void afterEach() {
        sut = null;
    }

    @Test
    public void testGetJobAppCards_withValidSessionId_shouldReturnListWithValuesNoErrorMessageAndHttpOk() throws Exception {
        final ArrayList<JobApplicationCard> CARDS = JobApplicationControllerTestHelper.getDefaultCards();
        final User user = JobApplicationControllerTestHelper.USER;
        final Session SESSION = user.getSession();
        final ResultPair<Optional<LocalDate>> SESSION_EXP_DATE = new ResultPair<>(Optional.of(SESSION.getExpirationDate()), "");

        Mockito.when(userDAO.getSessionExpDateBySessionId(Mockito.anyString()))
                .thenReturn(SESSION_EXP_DATE);
        Mockito.when(userService.hasSessionExpired(Mockito.any(LocalDate.class), Mockito.anyString()))
                .thenReturn(new ResultPair<>(false, ""));

        Mockito.when(userService.getUsernameBySessionId(Mockito.anyString()))
                .thenReturn(new ResultPair<>(user.getUsername(), ""));

        Mockito.when(jobAppService.getJobAppCardsBySessionId(Mockito.anyString()))
                .thenReturn(new ResultPair<>(CARDS, ""));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(JobApplicationURLS.getJobAppCard, SESSION.getSessionName())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
package com.jpettit.jobapplicationtrackerbackend.controllers;

import com.jpettit.jobapplicationtrackerbackend.daos.JobApplicationDAO;
import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import com.jpettit.jobapplicationtrackerbackend.helpers.JobApplicationControllerTestHelper;
import com.jpettit.jobapplicationtrackerbackend.enums.JobApplicationURLS;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.json.JSONArray;

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

    private final String SESSION_HAS_EXPIRED_MSG = "Session has expired.";
    private final String USERNAME_NOT_FOUND = "Couldn't find username";
    private final String INVALID_USERNAME = "Username can't be found.";

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
        final JSONArray CARDS_JSON = JobApplicationControllerTestHelper.convertCardsToJson(CARDS);
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED_PAIR = new ResultPair<>(CARDS, "");
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

        final String ACTUAL_RESPONSE = result.getResponse().getContentAsString();
        JobApplicationControllerTestHelper.assertResultsAreEqual(EXPECTED_PAIR, ACTUAL_RESPONSE);

        verifyHasSessionExpiredSessionTimesCalled(1);
        verifyGetUsernameBySessionIdTimesCalled(1);
        verifyGetJobAppCardsBySessionIdTimesCalled(1);
        verifyGetSessionByExpDateBySessionTimesCalled(0);
    }

    @Test
    public void testGetJobAppCards_withSessionIdThatHasExpired_shouldReturnNoValueErrorMessageOfSessionExpiredAndHttpNotFound() throws Exception {
        final ArrayList<JobApplicationCard> CARDS = new ArrayList<>();
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED_PAIR = new ResultPair<>(CARDS, SESSION_HAS_EXPIRED_MSG);
        final User user = JobApplicationControllerTestHelper.USER;
        final Session SESSION = user.getSession();
        final ResultPair<Optional<LocalDate>> SESSION_EXP_DATE = new ResultPair<>(Optional.of(SESSION.getExpirationDate()), "");

        Mockito.when(userService.hasSessionExpired(Mockito.any(LocalDate.class), Mockito.anyString()))
                .thenReturn(new ResultPair<>(true, ""));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(JobApplicationURLS.getJobAppCard, SESSION.getSessionName())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        final String ACTUAL_RESPONSE = result.getResponse().getContentAsString();
        JobApplicationControllerTestHelper.assertResultsAreEqual(EXPECTED_PAIR, ACTUAL_RESPONSE);

        verifyHasSessionExpiredSessionTimesCalled(1);
    }

    @Test
    public void testGetJobAppCards_withValidSessionIdAndCantFindUsername_shouldReturnNoValueErrorMessageOfSessionExpiredAndHttpNotFound() throws Exception {
        final ArrayList<JobApplicationCard> CARDS = JobApplicationControllerTestHelper.getEmptyCardList();
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED_PAIR = new ResultPair<>(CARDS, userDAO.USERNAME_NOT_FOUND);
        final ResultPair<String> USERNAME_PAIR = new ResultPair<>("", userDAO.USERNAME_NOT_FOUND);
        final User user = JobApplicationControllerTestHelper.USER;
        final Session SESSION = user.getSession();
        final ResultPair<Optional<LocalDate>> SESSION_EXP_DATE = new ResultPair<>(Optional.of(SESSION.getExpirationDate()), "");

        Mockito.when(userService.hasSessionExpired(Mockito.any(LocalDate.class), Mockito.anyString()))
                .thenReturn(new ResultPair<>(false, ""));
        Mockito.when(userService.getUsernameBySessionId(Mockito.anyString())).thenReturn(USERNAME_PAIR);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(JobApplicationURLS.getJobAppCard, SESSION.getSessionName())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        final String ACTUAL_RESPONSE = result.getResponse().getContentAsString();
        JobApplicationControllerTestHelper.assertResultsAreEqual(EXPECTED_PAIR, ACTUAL_RESPONSE);

        verifyHasSessionExpiredSessionTimesCalled(1);
        verifyGetUsernameBySessionIdTimesCalled(1);
    }

    @Test
    public void testGetJobAppCards_withValidSessionIdAndNonExistentUser_shouldReturnListWithValuesNoErrorMessageAndHttpOk() throws Exception {
        final ArrayList<JobApplicationCard> CARDS = JobApplicationControllerTestHelper.getEmptyCardList();
        final JSONArray CARDS_JSON = JobApplicationControllerTestHelper.convertCardsToJson(CARDS);
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED_PAIR = new ResultPair<>(CARDS, USERNAME_NOT_FOUND);
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
                .thenReturn(new ResultPair<>(new ArrayList<>(), USERNAME_NOT_FOUND));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(JobApplicationURLS.getJobAppCard, SESSION.getSessionName())
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        final String ACTUAL_RESPONSE = result.getResponse().getContentAsString();
        JobApplicationControllerTestHelper.assertResultsAreEqual(EXPECTED_PAIR, ACTUAL_RESPONSE);
    }

    private void verifyHasSessionExpiredSessionTimesCalled(final int EXPECTED_TIMES_CALLED) {
        Mockito.verify(userService, Mockito.times(EXPECTED_TIMES_CALLED))
                .hasSessionExpired(Mockito.any(LocalDate.class), Mockito.anyString());
    }

    private void verifyGetUsernameBySessionIdTimesCalled(final int EXPECTED_TIMES_CALLED) {
        Mockito.verify(userService, Mockito.times(EXPECTED_TIMES_CALLED))
                .getUsernameBySessionId(Mockito.anyString());
    }

    private void verifyGetJobAppCardsBySessionIdTimesCalled(final int EXPECTED_TIMES_CALLED) {
        Mockito.verify(jobAppService, Mockito.times(EXPECTED_TIMES_CALLED))
                .getJobAppCardsBySessionId(Mockito.anyString());
    }

    private void verifyGetSessionByExpDateBySessionTimesCalled(final int EXPECTED_TIMES_CALLED) {
        Mockito.verify(userDAO, Mockito.times(EXPECTED_TIMES_CALLED))
                .getSessionExpDateBySessionId(Mockito.anyString());
    }

    private void verifyMocksForController(final int MOCKS, final int EXPECTED_TIMES_CALLED) {
        final ArrayList<Integer> COUNTS = JobApplicationControllerTestHelper
                .initCalledTimesRepeating(MOCKS, EXPECTED_TIMES_CALLED);
        try {
            verifyHasSessionExpiredSessionTimesCalled(COUNTS.get(0));
            verifyGetUsernameBySessionIdTimesCalled(COUNTS.get(1));
            verifyGetJobAppCardsBySessionIdTimesCalled(COUNTS.get(2));
            verifyGetSessionByExpDateBySessionTimesCalled(COUNTS.get(3));
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
            Assertions.fail(ex.getMessage());
        }
    }
}
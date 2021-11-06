package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import helpers.JobApplicationDAOTestHelper;
import helpers.JobApplicationDAOTestHelperVars;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "application.env=test"
})
class JobApplicationDAOTest {

    @InjectMocks
    private JobApplicationDAO sut;

    @Mock
    private JobApplicationDaoInfoBuilder builder;

    private final String EXCEPTION_THROWN_MSG = "Exception was thrown.";
    private final String CREATE_CONN_ERR_MSG = "Couldn't create session.";
    private final String INVALID_USERNAME = "Username is empty";
    private final String INVALIDTABLENAME = "Table name is empty";
    // getJobAppCards tests

    @Test
    public void testGetJobAppCards_whenPassedInEmptyUsername_shouldReturnEmptyArrayListAndUsernameIsEmpty() {
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED_PAIR = new ResultPair<>(new ArrayList<>(), sut.EMPTY_USERNAME);
        final ResultPair<ArrayList<JobApplicationCard>> ACTUAL_PAIR = sut.getJobAppCards("");

        final JobApplicationDAOTestHelper<ResultPair<ArrayList<JobApplicationCard>>> HELPER = new JobApplicationDAOTestHelper<>(ACTUAL_PAIR, EXPECTED_PAIR);

        HELPER.verifyResultPairOfJobAppCardsAreEqual();
    }

    @Test
    public void testGetJobAppCards_whenPassedInUser1_ShouldReturnJobApplicationCardsAndNoErrorMessage() throws SQLException {
        final String USERNAME = JobApplicationDAOTestHelperVars.USER1_NAME;
        final ArrayList<JobApplicationCard> CARDS = JobApplicationDAOTestHelperVars.createDefaultCards();
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED = new ResultPair<>(CARDS, "");

        Mockito.when(builder.getJobAppCards(USERNAME))
                .thenReturn(EXPECTED);

        final ResultPair<ArrayList<JobApplicationCard>> ACTUAL = sut.getJobAppCards(USERNAME);

        final JobApplicationDAOTestHelper<ResultPair<ArrayList<JobApplicationCard>>> HELPER = new JobApplicationDAOTestHelper<>(ACTUAL, EXPECTED);

        HELPER.verifyResultPairOfJobAppCardsAreEqual();
        assertCallCountForGetJobAppCards(USERNAME, 1);
    }

    @Test
    public void testGetJobAppCards_whenConnectionCantBeMade_ShouldReturnNoCardsAndCouldntCreateSessionErrorMessage() throws SQLException {
        final String USERNAME = JobApplicationDAOTestHelperVars.USER1_NAME;
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED = new ResultPair<>(new ArrayList<>(), CREATE_CONN_ERR_MSG);

        Mockito.when(builder.getJobAppCards(USERNAME))
                .thenReturn(EXPECTED);

        final ResultPair<ArrayList<JobApplicationCard>> ACTUAL = sut.getJobAppCards(USERNAME);

        final JobApplicationDAOTestHelper<ResultPair<ArrayList<JobApplicationCard>>> HELPER = new JobApplicationDAOTestHelper<>(ACTUAL, EXPECTED);

        HELPER.verifyResultPairOfJobAppCardsAreEqual();
        assertCallCountForGetJobAppCards(USERNAME, 1);
    }
    // end of getJobAppCards tests

    private void assertCallCountForGetJobAppCards(final String USERNAME, final int EXPECTED_COUNT) throws SQLException {
        Mockito.verify(builder, Mockito.times(EXPECTED_COUNT)).getJobAppCards(USERNAME);
    }
}
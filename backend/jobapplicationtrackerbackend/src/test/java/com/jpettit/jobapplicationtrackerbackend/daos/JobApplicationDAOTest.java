package com.jpettit.jobapplicationtrackerbackend.daos;

import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import helpers.JobApplicationDAOTestHelper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "application.env=test"
})
class JobApplicationDAOTest {

    @InjectMocks
    private JobApplicationDAO sut;

    @Mock
    private JobApplicationDaoInfoBuilder builder;

    final String EXCEPTION_THROWN_MSG = "Exception was thrown.";

    @Test
    public void testGetJobAppCards_whenPassedInEmptyUsername_shouldReturnEmptyArrayListAndUsernameIsEmpty() {
        final ResultPair<ArrayList<JobApplicationCard>> EXPECTED_PAIR = new ResultPair<>(new ArrayList<>(), sut.EMPTY_USERNAME);
        final ResultPair<ArrayList<JobApplicationCard>> ACTUAL_PAIR = sut.getJobAppCards("");

        final JobApplicationDAOTestHelper<ResultPair<ArrayList<JobApplicationCard>>> HELPER = new JobApplicationDAOTestHelper<>(ACTUAL_PAIR, EXPECTED_PAIR);

        HELPER.verifyResultPairOfJobAppCardsAreEqual();
    }
}
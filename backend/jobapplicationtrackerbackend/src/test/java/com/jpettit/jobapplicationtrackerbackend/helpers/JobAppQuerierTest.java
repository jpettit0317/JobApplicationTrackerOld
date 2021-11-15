package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import testhelpers.helpers.JobAppQuerierTestHelpers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static testhelpers.helpers.JobAppQuerierTestHelpers.*;
import static testhelpers.helpervars.JobAppQuerierTestHelperVars.*;

class JobAppQuerierTest {
    private JobAppQuerier sut;

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    public void testGetAllJobAppCards_whenPassedInTestEnvAndUser1_shouldReturnQueryWithUser1AndDevEnv() {
        sut = createJobAppQuerier(TEST_ENV);

        final String EXPECTEDQUERY = getQuery(TEST_ENV, "", USER1);
        final ResultPair<String> EXPECTED_RESULT = new ResultPair<>(EXPECTEDQUERY, "");

        final ResultPair<String> ACTUAL_RESULT = sut.getAllJobAppCards(USER1);

        comparePairs(ACTUAL_RESULT, EXPECTED_RESULT);
    }

    @Test
    public void testGetAllJobAppCards_whenPassedInDevEnvAndUser1_shouldReturnQueryWithUser1AndDevEnv() {
        final String USER1 = "user1";
        sut = createJobAppQuerier(DEV_ENV);

        final String EXPECTEDQUERY = getQuery(DEV_ENV, "", USER1);
        final ResultPair<String> EXPECTED_RESULT = new ResultPair<>(EXPECTEDQUERY, "");

        final ResultPair<String> ACTUAL_RESULT = sut.getAllJobAppCards(USER1);

        comparePairs(ACTUAL_RESULT, EXPECTED_RESULT);
    }

    @Test
    public void testGetAllJobAppCards_whenPassedInProdEnvAndUser1_shouldReturnQueryWithUser1AndProdEnv() {
        sut = JobAppQuerierTestHelpers.createJobAppQuerier(PROD_ENV);

        final String EXPECTEDQUERY = JobAppQuerierTestHelpers.getQuery(PROD_ENV, "", USER1);
        final ResultPair<String> EXPECTED_RESULT = new ResultPair<>(EXPECTEDQUERY, "");

        final ResultPair<String> ACTUAL_RESULT = sut.getAllJobAppCards(USER1);

        JobAppQuerierTestHelpers.comparePairs(ACTUAL_RESULT, EXPECTED_RESULT);
    }

    @Test
    public void testGetAllJobAppCards_whenPassedInEmptyUsername_shouldReturnNoQueryAndInvalidUsername() {
        sut = JobAppQuerierTestHelpers.createJobAppQuerier(PROD_ENV);

        final ResultPair<String> EXPECTED_RESULT = new ResultPair<>("", INVALID_USERNAME);

        final ResultPair<String> ACTUAL_RESULT = sut.getAllJobAppCards("");

        JobAppQuerierTestHelpers.comparePairs(ACTUAL_RESULT, EXPECTED_RESULT);
    }
}
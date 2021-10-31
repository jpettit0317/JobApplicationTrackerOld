package com.jpettit.jobapplicationtrackerbackend.helpers;

import helpers.UserQuerierTestHelpers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserQuerierTest {
    UserQuerier sut;

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    public void testBuildGetUsernameBySessionId_whenPassedInOneAndDevEnvironment_shouldReturnSessionIdOfOne() {
        final String SESSION_ID = UserQuerierTestHelpers.user1Session.getSessionName();
        final ProjectEnvironment[] ENVS = {
                ProjectEnvironment.TEST,
                ProjectEnvironment.DEV,
                ProjectEnvironment.PROD
        };

        for (ProjectEnvironment env : ENVS) {
            sut = new UserQuerier(env);
            final String EXPECTED_QUERY = UserQuerierTestHelpers
                    .getExpectedGetUsernameBySessionIdQuery(env, SESSION_ID);

            final String ACTUAL_QUERY = sut.buildGetUsernameBySessionIdQuery(SESSION_ID);

            UserQuerierTestHelpers.assertQueriesAreEqual(ACTUAL_QUERY, EXPECTED_QUERY);
        }
    }

    @Test
    public void testBuildGetUsernameBySessionId_whenPassedInTwoAndEnvironments_shouldReturnSessionIdOfTwo() {
        final String SESSION_ID = "2";
        final ProjectEnvironment[] ENVS = {
                ProjectEnvironment.TEST,
                ProjectEnvironment.DEV,
                ProjectEnvironment.PROD
        };

        for (ProjectEnvironment env : ENVS) {
            sut = new UserQuerier(env);
            final String EXPECTED_QUERY = UserQuerierTestHelpers
                    .getExpectedGetUsernameBySessionIdQuery(env, SESSION_ID);

            final String ACTUAL_QUERY = sut.buildGetUsernameBySessionIdQuery(SESSION_ID);

            UserQuerierTestHelpers.assertQueriesAreEqual(ACTUAL_QUERY, EXPECTED_QUERY);
        }
    }
}
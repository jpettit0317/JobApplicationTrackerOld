package com.jpettit.jobapplicationtrackerbackend.helpers;

import testhelpers.UserQuerierTestHelpers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

class UserQuerierTest {
    private UserQuerier sut;
    private ArrayList<ProjectEnvironment> environments;

    @BeforeEach
    void setUp() {
        environments = getAllEnvironments();
    }

    @AfterEach
    void tearDown() {
        sut = null;
        destroyEnvironments();
    }

    private void destroyEnvironments() {
        environments.clear();
        environments = null;
    }

    private static ArrayList<ProjectEnvironment> getAllEnvironments() {
        final ProjectEnvironment[] ENVS = {
                ProjectEnvironment.TEST,
                ProjectEnvironment.DEV,
                ProjectEnvironment.PROD
        };
        ArrayList<ProjectEnvironment> environments = new ArrayList<>();

        Collections.addAll(environments, ENVS);

        return environments;
    }

    @Test
    public void testBuildGetUsernameBySessionId_whenPassedInOneAndDevEnvironment_shouldReturnSessionIdOfOne() {
        final String SESSION_ID = UserQuerierTestHelpers.user1Session.getSessionName();

        for (ProjectEnvironment env : environments) {
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

        for (ProjectEnvironment env : environments) {
            sut = new UserQuerier(env);
            final String EXPECTED_QUERY = UserQuerierTestHelpers
                    .getExpectedGetUsernameBySessionIdQuery(env, SESSION_ID);

            final String ACTUAL_QUERY = sut.buildGetUsernameBySessionIdQuery(SESSION_ID);

            UserQuerierTestHelpers.assertQueriesAreEqual(ACTUAL_QUERY, EXPECTED_QUERY);
        }
    }

    @Test
    public void testBuildGetSessionExpirationDateBySessionId_whenPassedInExistingSessionIDAndEnvironment_shouldReturnQueryWithExpDate() {
        final String SESSION_ID = "2";

        for (ProjectEnvironment env : environments) {
            sut = new UserQuerier(env);

            final String EXPECT_QUERY = UserQuerierTestHelpers.getExpectedGetExpDateBySessionId(env, SESSION_ID);
            final String ACTUAL_QUERY = sut.getSessionExpirationDateBySessionId(SESSION_ID);

            UserQuerierTestHelpers.assertQueriesAreEqual(ACTUAL_QUERY, EXPECT_QUERY);
        }
    }
}
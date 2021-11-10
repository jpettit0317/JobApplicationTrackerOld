package com.jpettit.jobapplicationtrackerbackend.helpers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testhelpers.ProjectEnvironmentReaderHelperVars;
import testhelpers.ProjectEnvironmentReaderTestHelper;

import static org.junit.jupiter.api.Assertions.*;

class ProjectEnvironmentReaderTest {
    private ProjectEnvironmentReaderTestHelper<ProjectEnvironment> helper;

    @BeforeEach
    void setUp() {
        helper = new ProjectEnvironmentReaderTestHelper<>(null, null);
    }

    @AfterEach
    void tearDown() {
        helper = null;
    }

    @Test
    public void testGetEnvironment_whenPassedInDevString_shouldReturnProjectEnvDev() {
        final String ENV_STR = ProjectEnvironmentReaderHelperVars.DEV_STR;
        final ProjectEnvironment EXPECTED = ProjectEnvironment.DEV;
        final  ProjectEnvironment ACTUAL = ProjectEnvironmentReader.getEnvironment(ENV_STR);

        helper.setActualValue(ACTUAL);
        helper.setExpectedValue(EXPECTED);

        helper.verifyEnvironmentsAreEqual();
    }

    @Test
    public void testGetEnvironment_whenPassedInProdString_shouldReturnProjectEnvProd() {
        final String ENV_STR = ProjectEnvironmentReaderHelperVars.PROD_STR;
        final ProjectEnvironment EXPECTED = ProjectEnvironment.PROD;
        final  ProjectEnvironment ACTUAL = ProjectEnvironmentReader.getEnvironment(ENV_STR);

        helper.setActualValue(ACTUAL);
        helper.setExpectedValue(EXPECTED);

        helper.verifyEnvironmentsAreEqual();
    }

    @Test
    public void testGetEnvironment_whenPassedInTestString_shouldReturnProjectEnvTest() {
        final String ENV_STR = ProjectEnvironmentReaderHelperVars.TEST_STR;
        final ProjectEnvironment EXPECTED = ProjectEnvironment.TEST;
        final  ProjectEnvironment ACTUAL = ProjectEnvironmentReader.getEnvironment(ENV_STR);

        helper.setActualValue(ACTUAL);
        helper.setExpectedValue(EXPECTED);

        helper.verifyEnvironmentsAreEqual();
    }

    @Test
    public void testGetEnvironment_whenPassedInEmptyString_shouldReturnProjectDevTest() {
        final String ENV_STR = "";
        final ProjectEnvironment EXPECTED = ProjectEnvironment.DEV;
        final  ProjectEnvironment ACTUAL = ProjectEnvironmentReader.getEnvironment(ENV_STR);

        helper.setActualValue(ACTUAL);
        helper.setExpectedValue(EXPECTED);

        helper.verifyEnvironmentsAreEqual();
    }
}
package com.jpettit.jobapplicationtrackerbackend.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import testhelpers.helpers.JobApplicationCardTestHelpers;
import testhelpers.helpervars.JobApplicationCardHelperVars;

class JobApplicationCardTest {

    @Test
    public void testCreateFromJobApp_whenInitedWithJobApp_gettersShouldReturnGivenJobAppValues() {
        final JobApplication JOB_APP = JobApplicationCardHelperVars.createDefaultJobApp();
        final JobApplicationCard CARD = JobApplicationCard.createFromJobApp(JOB_APP);

        JobApplicationCardTestHelpers.verifyIfJobAppCardAndJobAppAreEqual(CARD, JOB_APP);
    }
}
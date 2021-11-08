package com.jpettit.jobapplicationtrackerbackend.models;

import testhelpers.JobApplicationTestHelpers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

class JobApplicationTest {
    JobApplication sut;

    @AfterEach
    void tearDown() {
        sut = null;
    }


    @Test
    public void testGetInterviewAtIndex_whenPassedInOutofBoundsIndex_shouldReturnEmptyOptional() {
        sut = JobApplicationTestHelpers.createDefaultJobApp();
        final int INTERVIEW_COUNT = sut.getInterviews().size();
        final Integer[] INVALID_INDECES = { Integer.MIN_VALUE, Integer.MAX_VALUE, INTERVIEW_COUNT };

        for (Integer i : INVALID_INDECES) {
            final Optional<Interview> EXPECTED_INTERVIEW = Optional.empty();
            final Optional<Interview> ACTUAL_INTERVIEW = sut.getInterviewAtIndex(i);

            JobApplicationTestHelpers.validateGetInterviewAtIndex(i, ACTUAL_INTERVIEW, EXPECTED_INTERVIEW);
        }
    }

    @Test
    public void testGetInterviewAtIndex_whenPassedInInBoundIndex_shouldReturnInterview() {
        sut = JobApplicationTestHelpers.createDefaultJobApp();
        final ArrayList<Interview> INTERVIEWS = JobApplicationTestHelpers.createDefaultInterviews();

        for (int i = 0; i < INTERVIEWS.size(); i++) {
            final Optional<Interview> EXPECTED_INTERVIEW = Optional.of(INTERVIEWS.get(i));
            final Optional<Interview> ACTUAL_INTERVIEW = sut.getInterviewAtIndex(i);

            JobApplicationTestHelpers.validateGetInterviewAtIndex(i, ACTUAL_INTERVIEW, EXPECTED_INTERVIEW);
        }
    }


}
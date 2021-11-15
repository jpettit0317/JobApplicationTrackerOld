package com.jpettit.jobapplicationtrackerbackend.helpers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testhelpers.helpers.DateConverterTestHelper;
import testhelpers.helpervars.DateConverterTestVars;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

class DateConverterTest {
   private DateConverterTestHelper<LocalDate> localDateHelper;
   private DateConverterTestHelper<Date> dateHelper;

   private final Month JANUARY = Month.JANUARY;
   private final int YEARTWENTYTWENTY = 2020;
   private final int FIRST_DAY = 1;

    @BeforeEach
    void setUp() {
        localDateHelper = new DateConverterTestHelper<>(null, null);
        dateHelper = new DateConverterTestHelper<>(null, null);
    }

    @AfterEach
    void tearDown() {
        localDateHelper = null;
        dateHelper = null;
    }

    @Test
    public void testConvertDateToLocalDate_whenPassedInJan12020_shouldReturnJan12020AsLocalDate() {
        final Date EXPECTED = DateConverterTestVars.createDate(JANUARY, FIRST_DAY, YEARTWENTYTWENTY);
        final LocalDate ACTUAL = DateConverter.convertDateToLocalDate(EXPECTED);
        final LocalDate EXPECTED_LOCALDATE = EXPECTED.toLocalDate();

        localDateHelper.setActualValue(ACTUAL);
        localDateHelper.setExpectedValue(EXPECTED_LOCALDATE);

        localDateHelper.verifyDatesAreEqual();
    }

    @Test
    public void testConvertLocalDateToDate_whenPassedInJan12020_shouldReturnJan12020AsLocalDate() {
        final LocalDate EXPECTED = DateConverterTestVars.createLocalDate(JANUARY, FIRST_DAY, YEARTWENTYTWENTY);
        final Date ACTUAL = DateConverter.convertLocalDateToDate(EXPECTED);
        final Date EXPECTEDDATE_AS_DATE = Date.valueOf(EXPECTED);

        dateHelper.setExpectedValue(EXPECTEDDATE_AS_DATE);
        dateHelper.setActualValue(ACTUAL);

        dateHelper.verifyDatesAreEqual();
    }
}
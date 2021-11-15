package com.jpettit.jobapplicationtrackerbackend.helpers;

import java.sql.Date;
import java.time.LocalDate;

public class DateConverter {
    public static LocalDate convertDateToLocalDate(final Date DATE) {
        return DATE.toLocalDate();
    }

    public static Date convertLocalDateToDate(final LocalDate DATE) {
        return Date.valueOf(DATE);
    }
}

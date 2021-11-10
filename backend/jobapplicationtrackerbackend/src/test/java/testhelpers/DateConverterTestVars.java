package testhelpers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;

public class DateConverterTestVars {
    public static Date createDate(final Month MONTH, final int DAY, final int YEAR) {
        final String DATE = convertMonthDayYearToStringDate(MONTH, DAY, YEAR);

        return Date.valueOf(DATE);
    }

    public static Date transformLocalDateToDate(final LocalDate DATE) {
        return Date.valueOf(DATE);
    }

    public static LocalDate createLocalDate(final Month MONTH, final int DAY,
                                            final int YEAR) {
        return LocalDate.of(YEAR, MONTH.getValue(), DAY);
    }

    public static String convertMonthDayYearToStringDate(final Month MONTH,
                                                         final int DAY,
                                                         final int YEAR) {
        return String.format("%d-%d-%d",
                YEAR, MONTH.getValue(), DAY);
    }
}

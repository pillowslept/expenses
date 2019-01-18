package co.com.expenses.util;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import co.com.expenses.exception.ValidateException;

public class DateUtilities {

    private static final int FIRST_DAY_OF_MONTH = 1;
    private static final String DATE_FORMAT_NOT_VALID = "La fecha <%s> no se encuentra en el formato <%s>";
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";
    private static final Logger LOGGER = Logger.getLogger(DateUtilities.class.getName());

    private DateUtilities() {
    }

    public static String timestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(timestamp);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(date);
    }

    public static Timestamp getTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getActualDate(){
        return timestampToString(getTimestamp());
    }

    public static Timestamp toTimestamp(String dateInFormat){
        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        format.setLenient(Boolean.FALSE);

        Date date = null;
        try {
            date = format.parse(dateInFormat);
        } catch (ParseException e) {
            LOGGER.info(String.format(DATE_FORMAT_NOT_VALID, dateInFormat, DATE_FORMAT), e);
            throw new ValidateException(String.format(DATE_FORMAT_NOT_VALID, dateInFormat, DATE_FORMAT));
        }
        return new Timestamp(date.getTime());
    }

    public static void validateStringInFormat(String dateInFormat){
        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        format.setLenient(Boolean.FALSE);
        try {
            format.parse(dateInFormat);
        } catch (ParseException e) {
            LOGGER.info(String.format(DATE_FORMAT_NOT_VALID, dateInFormat, DATE_FORMAT), e);
            throw new ValidateException(String.format(DATE_FORMAT_NOT_VALID, dateInFormat, DATE_FORMAT));
        }
    }

    public static Date obtainBeginingOfDate(int month) {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.withMonth(month);
        return localDateFirstDayOfMonth(localDate);
    }

    public static Date obtainBeginingOfDate(int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, FIRST_DAY_OF_MONTH);
        return localDateFirstDayOfMonth(localDate);
    }

    public static Date obtainEndOfDate(int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, FIRST_DAY_OF_MONTH);
        return localDateLastDayOfMonth(localDate);
    }

    public static Date obtainEndOfDate(int month) {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.withMonth(month);
        return localDateLastDayOfMonth(localDate);
    }

    private static Date localDateFirstDayOfMonth(LocalDate localDate) {
        LocalDate start = localDate.with(firstDayOfMonth());
        return Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Date localDateLastDayOfMonth(LocalDate localDate) {
        LocalDate endDateOfMonth = localDate.with(lastDayOfMonth());
        LocalDateTime endDateOfMonthWithTime = endDateOfMonth.atTime(23, 59, 59, 999);
        return Date.from(endDateOfMonthWithTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}

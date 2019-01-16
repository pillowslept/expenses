package co.com.expenses.util;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import co.com.expenses.exception.ValidateException;

public class DateUtilities {

    public static final List<Integer> VALID_MONTHS = Arrays.asList(Calendar.JANUARY, Calendar.FEBRUARY,
            Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST,
            Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER);
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
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToCalendar(calendar, 0, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date obtainBeginingOfDate(int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        LocalDate start = localDate.with(firstDayOfMonth());
        return Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date obtainEndOfDate(int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        LocalDate endDate = localDate.with(lastDayOfMonth());
        LocalDateTime endDateWithTime = endDate.atTime(23, 59, 59, 999);
        return Date.from(endDateWithTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date obtainEndOfDate(int month) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToCalendar(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    private static void setTimeToCalendar(Calendar calendar, int hour, int minute, int second, int millisecond) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
    }

    public static boolean isValidMonth(int mont) {
        boolean isValid = true;
        try {
            Month.of(mont);
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

}

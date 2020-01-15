package co.com.expenses.component;

import static co.com.expenses.util.Constants.DEFAULT_DATE_FORMAT;
import static co.com.expenses.util.Constants.FIRST_DAY_OF_MONTH;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.expenses.exception.ValidateException;

@Component
public class DateUtilities {

    private static final Logger LOGGER = LogManager.getLogger(DateUtilities.class.getName());

    @Autowired
    Messages messages;

    public String timestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return dateFormat.format(timestamp);
    }

    public String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return dateFormat.format(date);
    }

    public Timestamp getTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public String getActualDate(){
        return timestampToString(getTimestamp());
    }

    public Timestamp toTimestamp(String dateInFormat){
        DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        format.setLenient(Boolean.FALSE);

        Date date = null;
        try {
            date = format.parse(dateInFormat);
        } catch (ParseException e) {
            String message = String.format(messages.get("date.format.not.valid"), dateInFormat, DEFAULT_DATE_FORMAT);
            LOGGER.info(message, e);
            throw new ValidateException(message);
        }

        return new Timestamp(date.getTime());
    }

    public Date obtainBeginingOfDate(int month) {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.withMonth(month);
        return localDateFirstDayOfMonth(localDate);
    }

    public Date obtainBeginingOfDate(int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, FIRST_DAY_OF_MONTH);
        return localDateFirstDayOfMonth(localDate);
    }

    public Date obtainEndOfDate(int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, FIRST_DAY_OF_MONTH);
        return localDateLastDayOfMonth(localDate);
    }

    public Date obtainEndOfDate(int month) {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.withMonth(month);
        return localDateLastDayOfMonth(localDate);
    }

    private Date localDateFirstDayOfMonth(LocalDate localDate) {
        LocalDate start = localDate.with(firstDayOfMonth());
        return Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date localDateLastDayOfMonth(LocalDate localDate) {
        LocalDate endDateOfMonth = localDate.with(lastDayOfMonth());
        LocalDateTime endDateOfMonthWithTime = endDateOfMonth.atTime(23, 59, 59, 999);
        return Date.from(endDateOfMonthWithTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}

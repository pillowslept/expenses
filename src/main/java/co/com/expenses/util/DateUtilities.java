package co.com.expenses.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import co.com.expenses.exception.ValidateException;

public class DateUtilities {

    private static final String DATE_FORMAT_NOT_VALID = "La fecha <%s> no se encuentra en el formato <%s>";
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";
    private static final Logger LOGGER = Logger.getLogger(DateUtilities.class.getName());

    private DateUtilities() {
    }

    public static String timestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(timestamp);
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

}

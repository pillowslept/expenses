package co.com.expenses.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtilities {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

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
}

package app.contacts.com.contacts.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public class Formats {

        public static final String DATE_FORMAT = "MM-dd-yyyy";
        public static final String CALENDAR_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
        public static final String DATE_TIME_FORMAT = "MM-dd-yyyy hh:mm:ss";
        public static final String DAY_DATE_TIME_FORMAT = "EEE dd MMM yyyy hh:mm:ss";
        public static final String CALL_DATE_FORMAT = "EEE MMM dd hh:mm:ss z yyyy";

        // Fri Mar 27 15:30:35 CDT 2015
    }

    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }
    public static String getCurrentDate() {
        return new SimpleDateFormat(Formats.DATE_FORMAT, Locale.getDefault()).format(new Date());
    }
    public static String getCurrentDateTime() {
        return new SimpleDateFormat(Formats.DATE_TIME_FORMAT, Locale.getDefault()).format(new Date());
    }

    public static Date stringToDate(String dateString, String format) {

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(dateString);

        } catch (ParseException e) {
            LogUtility.error("DateUtil", e.toString());
        }
        return null;
    }

    public static String formatDateString(String dateString, String originalFormat, String newFormat) {

        try {

            if (StringUtil.isNullOrEmpty(dateString))
                return "";

            else {
                SimpleDateFormat dateFormat = new SimpleDateFormat(originalFormat);
                return new SimpleDateFormat(newFormat).format(dateFormat.parse(dateString));
            }

        } catch (ParseException e) {
            LogUtility.error("DateUtil", e.toString());
        }
        return "";
    }

    public static String millisecondToReadableDate(long millisec, String format) {
        return new SimpleDateFormat(format).format(new Date(millisec)).toString();
    }

    public static String timeConversion(int totalSeconds) {

        if (totalSeconds != 0) {

            final int MINUTES_IN_AN_HOUR = 60;
            final int SECONDS_IN_A_MINUTE = 60;

            int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
            int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
            int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
            int hours = totalMinutes / MINUTES_IN_AN_HOUR;

            return hours + "h " + minutes + "m " + seconds + "s";
        }
        return "0s";
    }
}

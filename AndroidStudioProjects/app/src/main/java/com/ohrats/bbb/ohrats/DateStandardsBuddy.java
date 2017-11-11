package com.ohrats.bbb.ohrats;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mkrupczak on 10/18/2017.
 */

// Suppressing warnings about this being a malformed "Utility" class
//   Essentially, this class only deals with SimpleDateFormats, Strings, Dates, and Timezones
//   and we don't necessarily care that it doesn't follow strict OOD guidelines b/c it's really
//   just an abstraction to help programmers deal with date formatting and not a thing that needs to
//   exist as its own object
@SuppressWarnings({"UtilityClass", "DefaultFileTemplate"})
public final class DateStandardsBuddy {

    // Suppressing warnings which tell us that we're not respecting the date format of the user's
    // locale. i.e. I know what I'm doing
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat iso8601DF =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat garbageAmericanDF =
            new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat iso8601SansTime =
            new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat iso8601SansDay =
            new SimpleDateFormat("yyyy-MM");

    private static final TimeZone estTZ = TimeZone.getTimeZone("EST");

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat iso8601DFmax =
            new SimpleDateFormat("yyyy-MM-dd'T'23:59:59");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat iso8601DFmin =
            new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");

    /**
     * Private constructor: class can not be instantiated
     */
    private DateStandardsBuddy() {
    }

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     * Thanks Joshua and kirstopherjohnson
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss" where the time is EST
     */
    public static String getISO8601ESTStringForCurrentDate() {
        Date now = new Date();
        return getISO8601ESTStringForDate(now);

    }

    /**
     * Return an ISO 8601 EST combined date and time string for specified date/time
     *
     * @param targetDate
     *            Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss" where the time is EST
     */
    public static String getISO8601ESTStringForDate(Date targetDate) {
        if (targetDate == null) {
            return "";
        }
        TimeZone tz = TimeZone.getTimeZone("EST");
        DateFormat df = iso8601DF;
        df.setTimeZone(tz);
        return df.format(targetDate);
    }

    /**
     * Return an ISO 8601 EST String WITHOUT a time at the end
     * e.g. yyyy-MM-dd
     *
     * @param targetDate date object of which the output string will represent
     * @return String with format "yyyy-MM-dd" where the time is EST
     */
    @SuppressWarnings("WeakerAccess") // Keeping this weaker to keep consistency with other methods
    public static String getISO8601ESTSansTimeStringForDate(Date targetDate) {
        if (targetDate == null) {
            return "";
        }
        TimeZone tz = TimeZone.getTimeZone("EST");
        DateFormat df = iso8601SansTime;
        df.setTimeZone(tz);
        return df.format(targetDate);

    }

    /**
     * Return an ISO 8601 EST String WITHOUT a specific Day nor time at the end (year and month)
     *  e.g. yyyy-MM
     *
     * @param targetDate date object of which the output string will represent year and month of
     * @return String with the format "yyyy-MM" where the time alignment is EST
     */
    public static String getISO8601ESTSansDayStringForDate(Date targetDate) {
        if (targetDate == null) {
            return "";
        }
        TimeZone tz = TimeZone.getTimeZone("EST");
        DateFormat df = iso8601SansDay;
        df.setTimeZone(tz);
        return df.format(targetDate);
    }

    /**
     * Given a full ISO8601 String, returns a String without the time at the end
     *
     * @param input8601 A string in the format "yyyy-MM-ddTHH:mm:ss"
     * @return A truncated date string i.e. "yyyy-MM-dd"
     * @throws ParseException if the input string is not of the mentioned format
     */
    public static String truncateTimeFromISO8601ESTString(String input8601) throws ParseException {
        Date intermediaryDate = getDateFromISO8601ESTString(input8601);
        return getISO8601ESTSansTimeStringForDate(intermediaryDate);
    }

// --Commented out by Inspection START (11/11/2017 1:48 PM):
//    /**
//     * Given a full ISO8601 String, returns a String without the specific Day nor time at the end
//     *
//     * @param input8601 A string in the format "yyyy-MM-ddTHH:mm:ss"
//     * @return A truncated date string i.e. "yyyy-MM"
//     * @throws ParseException if the input string is not of the mentioned format
//     */
//    public static String truncateDayFromISO8601ESTString(String input8601) throws ParseException {
//        Date intermediaryDate = getDateFromISO8601ESTString(input8601);
//        return getISO8601ESTSansDayStringForDate(intermediaryDate);
//    }
// --Commented out by Inspection STOP (11/11/2017 1:48 PM)

    /**
     * Given a target date, returns the maximum ISO8601 EST Date/Time String
     *
     * e.g. given any time of a particular day, will give the maximum ISO8601 date time of the same
     * day as the input Date
     *
     * Beware that method will probably maximize in terms of EST, so be careful
     * @param targetDate the target date/time to maximize
     * @return the maximum ISO8601 date/time string for the given date e.g.
     * "yyyy-MM-dd'T'23:59:59"
     * returns an empty string if the input Date is null
     */
    public static String getISO8601MAXStringForDate(Date targetDate) {
        if (targetDate == null) {
            return "";
        }
        DateFormat df = iso8601DFmax;
        df.setTimeZone(estTZ);
        return df.format(targetDate);
    }

    /**
     * Given a target date, returns the minimum ISO8601 EST Date/Time String
     *
     * e.g. given any time of a particular day, will give the minimum ISO8601 date time of the same
     * day as the input Date
     *
     * Beware that method will probably minimize in terms of EST, so be careful
     * @param targetDate the target date/time to minimize
     * @return the minimum ISO8601 date/time string for the given date e.g.
     * "yyyy-MM-dd'T'00:00:00"
     * returns an empty string if the input Date is null
     */
    public static String getISO8601MINStringForDate(Date targetDate) {
        if (targetDate == null) {
            return "";
        }
        DateFormat df = iso8601DFmin;
        df.setTimeZone(estTZ);
        return df.format(targetDate);
    }

    /**
     * Returns a java date from a given ISO8601 string in EST
     * @param iso8601String input string in ISO8601, assumed to be EST
     * @return a java date object of the corresponding date and time
     * @throws ParseException throws an exception if the assumption of the date format is incorrect
     */
    public static Date getDateFromISO8601ESTString(String iso8601String) throws ParseException {
        DateFormat df = iso8601DF;
        df.setTimeZone(estTZ);
        return df.parse(iso8601String);
    }

    /**
     * Given a string in the garbage American format "MM/dd/yyyy hh:mm:ss aa" returns
     * one in the ISO8601 format "yyyy-MM-dd'T'HH:mm:ss" in EST
     *
     * If the input string is invalid or null, returns an empty string
     *
     * @param garbageAmericanString input String in the garbage American date format
     * @return a String representation of the date represented by the input, but in ISO8601
     * where the time is in eastern standard time
     *
     */
    public static String garbageAmericanStringToISO8601ESTString(String garbageAmericanString) {
        if ((garbageAmericanString == null) || garbageAmericanString.isEmpty()) {
            return "";
        } else {
            Date intermediaryDate;
            try {
                intermediaryDate = garbageAmericanDF.parse(garbageAmericanString);
            } catch (ParseException e) {
                // parsing failed, input was likely invalid
                return "";
            }
            return getISO8601ESTStringForDate(intermediaryDate);
        }
    }

// --Commented out by Inspection START (11/11/2017 1:50 PM):
//    /**
//     *
//     * @param garbageAmericanString input String in the garbage American date format
//     * @return a Date  in ISO8601 where the time is in eastern standard time
//     */
//    public static Date garbageAmericanStringToISO8601ESTDate(String garbageAmericanString) {
//        if ((garbageAmericanString == null) || garbageAmericanString.isEmpty()) {
//            return null;
//        } else {
//            Date date = null;
//            try {
//                date = garbageAmericanDF.parse(garbageAmericanString);
//            } catch (ParseException e) {
//                // parsing failed, input was likely invalid
//                return null;
//            }
//            return date;
//        }
//    }
// --Commented out by Inspection STOP (11/11/2017 1:50 PM)



// --Commented out by Inspection START (11/11/2017 1:50 PM):
//    /**
//     * Gets a date format representing ISO8601 in the format "yyyy-MM-dd'T'HH:mm:ss"
//     * @return iso8601DF SimpleDateFormat
//     */
//    public static DateFormat getIso8601DF() {
//        return iso8601DF;
//    }
// --Commented out by Inspection STOP (11/11/2017 1:50 PM)

// --Commented out by Inspection START (11/11/2017 1:51 PM):
//    /**
//     * Gets a date format representing the garbage american format
//     * i.e. "MM/dd/yyyy hh:mm:ss aa"
//     * @return garbageAmericanDF SimpleDateFormat
//     */
//    public static DateFormat getGarbageAmericanDF() {
//        return garbageAmericanDF;
//    }
// --Commented out by Inspection STOP (11/11/2017 1:51 PM)

// --Commented out by Inspection START (11/11/2017 1:51 PM):
//    /**
//     * Gets a date format representing ISO8601 without a time at the end
//     * i.e. "yyyy-MM-dd"
//     * @return iso8601SansTime SimpleDateFormat
//     */
//    public static DateFormat getIso8601SansTime() { return iso8601SansTime; }
// --Commented out by Inspection STOP (11/11/2017 1:51 PM)

// --Commented out by Inspection START (11/11/2017 1:51 PM):
//    /**
//     * Gets a date format representing the maximum ISO8601 date time for a given day
//     * i.e. "yyyy-MM-dd'T'23:59:59"
//     * @return iso8601DFmax date format which will format a date to maximum time in a ISO8601 day
//     */
//    public static DateFormat getIso8601DFmax() {
//        return iso8601DFmax;
//    }
// --Commented out by Inspection STOP (11/11/2017 1:51 PM)

// --Commented out by Inspection START (11/11/2017 1:51 PM):
//    /**
//     * Gets a date format representing the minimum ISO8601 date time for a given day
//     * i.e. "yyyy-MM-dd'T'00:00:00"
//     * @return iso8601DFmin date format which will format a date to minimum time in a ISO8601 day
//     */
//    public static DateFormat getIso8601DFmin() {
//        return iso8601DFmin;
//    }
// --Commented out by Inspection STOP (11/11/2017 1:51 PM)



}
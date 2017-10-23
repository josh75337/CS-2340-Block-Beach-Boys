package com.ohrats.bbb.ohrats;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by matthew krupczak on 10/18/2017.
 */

public class DateStandardsBuddy {

    private static DateFormat iso8601DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // Quoted "Z" to indicate UTC, no timezone offset
    private static DateFormat garbageAmericanDF = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

    private static TimeZone estTZ = TimeZone.getTimeZone("EST");

    private static DateFormat iso8601DFmax = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59");
    private static DateFormat iso8601DFmin = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");

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
        TimeZone tz = estTZ;
        DateFormat df = iso8601DFmax;
        df.setTimeZone(tz);
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
        TimeZone tz = estTZ;
        DateFormat df = iso8601DFmin;
        df.setTimeZone(tz);
        return df.format(targetDate);
    }

    /**
     * Returns a java date from a given ISO8601 string in EST
     * @param iso8601String input string in ISO8601, assumed to be EST
     * @return a java date object of the corresponding date and time
     */
    public static Date getDateFromISO8601ESTString(String iso8601String) throws ParseException {
        TimeZone tz = estTZ;
        DateFormat df = iso8601DF;
        df.setTimeZone(tz);
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
        if (garbageAmericanString == null || garbageAmericanString.isEmpty()) {
            return "";
        } else {
            Date intermediaryDate = null;
            try {
                intermediaryDate = garbageAmericanDF.parse(garbageAmericanString);
            } catch (ParseException e) {
                // parsing failed, input was likely invalid
                return "";
            }
            return getISO8601ESTStringForDate(intermediaryDate);
        }
    }



    /**
     * Gets a date format representing ISO8601 in the format "yyyy-MM-dd'T'HH:mm:ss"
     * @return iso8601DF SimpleDateFormat
     */
    public static DateFormat getIso8601DF() {
        return iso8601DF;
    }

    /**
     * Gets a date format representing the garbage american format
     * i.e. "MM/dd/yyyy hh:mm:ss aa"
     * @return garbageAmericanDF SimpleDateFormat
     */
    public static DateFormat getGarbageAmericanDF() {
        return garbageAmericanDF;
    }

    /**
     * Gets a date format representing the maximum ISO8601 date time for a given day
     * i.e. "yyyy-MM-dd'T'23:59:59"
     * @return iso8601DFmax date format which will format a date to maximum time in a ISO8601 day
     */
    public static DateFormat getIso8601DFmax() {
        return iso8601DFmax;
    }

    /**
     * Gets a date format representing the minimum ISO8601 date time for a given day
     * i.e. "yyyy-MM-dd'T'00:00:00"
     * @return iso8601DFmin date format which will format a date to minimum time in a ISO8601 day
     */
    public static DateFormat getIso8601DFmin() {
        return iso8601DFmin;
    }



}
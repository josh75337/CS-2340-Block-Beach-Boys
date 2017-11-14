package com.ohrats.bbb.ohrats;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Justin on 11/13/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class DateStandardsBuddyMinTest {
    private Date testDate;


    /**
     * tests empty string
     */
    //meant to be null
    @SuppressWarnings({"AssignmentToNull", "ConstantConditions"})
    @Test
    public void testEmptyString() {
        testDate = null;
        String testResult = DateStandardsBuddy.getISO8601MINStringForDate(testDate);
        assertEquals("", testResult);
    }

    /**
     * tests normal inputs
     */
    @Test
    public void testMinDateFormat() {
        testDate = new Date(0);
        String stringResult  = "1969-12-31T00:00:00";
        assertEquals(DateStandardsBuddy.getISO8601MINStringForDate(testDate), stringResult);
    }

//    /**
//     * should fail
//     */
//    @Test
//    public void testIncorrectDate() {
//        testDate = new Date(0);
//        String stringResult = "12342353434";
//        assertEquals(DateStandardsBuddy.getISO8601MINStringForDate(testDate), stringResult);
//    }
}

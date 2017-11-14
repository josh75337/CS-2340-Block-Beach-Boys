package com.ohrats.bbb.ohrats;



import java.util.Date;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Josh on 11/11/2017.
 */

public class DateStandardsBuddyMaxTest {
    public Date testDate;

    @Test
    public void testEmptyString() {
        testDate = null;
        String result = DateStandardsBuddy.getISO8601MAXStringForDate(testDate);
        assertEquals("", result);
    }
    @Test
    public void testRegularDate() {
        //this is the epoch
        testDate = new Date(0);
        String result2 = testDate.toString();
        //epoch local time and date
        String result  = "1969-12-31T23:59:59";

        assertEquals(DateStandardsBuddy.getISO8601MAXStringForDate(testDate), result);
    }
}

package com.ohrats.bbb.ohrats;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Elijah on 11/10/2017.
 */
public class DateStandardsBuddyTest {

    private Date curDate1;

    private String expected1;

    @Before
    public void setUp() throws Exception {

        //date is the epoch
        curDate1 = new Date(0);

        //epoch local time and date
        expected1 = "1969-12-31T19:00:00";
    }

    @Test
    public void getISO8601ESTStringForDate() throws Exception {
        String date = DateStandardsBuddy.getISO8601ESTStringForDate(curDate1);
        assertEquals(expected1, date);
    }

}
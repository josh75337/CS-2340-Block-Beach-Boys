package com.ohrats.bbb.ohrats;

import android.support.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test for toString() method of Rat Sighting class
 * Created by Matt on 11/13/2017.
 */

public class RatSightingToStringTest {

    @Nullable
    private RatSighting nullSighting;
    private RatSighting emptySighting;
    private RatSighting goodSighting;
    private String nullString;
    private String notAvailableString;
    private String goodString;

    /**
     * set up test
     * @throws Exception if assert fails
     */
    @Before
    public void setUp() throws Exception {
        nullSighting = new RatSighting();
        emptySighting = new RatSighting("", "", "", "",
                "", "", "", 0, 0);
        goodSighting = new RatSighting("1234567890", "11/13/2017 8:36:10 PM",
                "Test Location Type", "Test Zip", "Test Address",
                "Test City", "Test Borough", 0, 0);
        nullString = "      null |       null | null";
        notAvailableString = "       N/A |        N/A | N/A";
        goodString = "1234567890 | 11/13/2017 | Test Address";
    }

    /**
     * tests if given a null sighting
     * @throws Exception if assert fails
     */
    @Test
    public void toStringTestNullSighting() throws Exception {
        String testString = nullSighting.toString();
        assertEquals(nullString, testString);
    }

    /**
     * tests with sighting with empty variables
     * @throws Exception if assert fails
     */
    @Test
    public void toStringTestEmptySighting() throws Exception {
        String testString = emptySighting.toString();
        assertEquals(notAvailableString, testString);
    }

    /**
     * tests with a sighting containing filled variables
     * @throws Exception if assert fails
     */
    @Test
    public void toStringTestGoodSighting() throws Exception {
        String testString = goodSighting.toString();
        assertEquals(goodString, testString);
    }

}

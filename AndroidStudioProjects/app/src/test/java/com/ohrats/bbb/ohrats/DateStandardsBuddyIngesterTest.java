package com.ohrats.bbb.ohrats;

import android.support.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by mkrupczak on 11/12/2017.
 */
@SuppressWarnings("DefaultFileTemplate") // we do follow default for Android
public class DateStandardsBuddyIngesterTest {
// Tests the method garbageAmericanStringToISO8601ESTString in DateStandardsBuddy
// Should return an ISO8601 String if the input was valid, empty string otherwise
// For full coverage, we need to test:
    // Null input String
    // Empty input String
    // Malformed input String
    // Valid input String

//    private Date curDate1;


    @Nullable
    private String nullString;
    private String emptyString;
    private String badAmericanString;
    private String goodAmericanString;
    private String expectedGoodOutput;

    /**
     * Sets up the test
     * @throws Exception if an assertion fails
     */
    @Before
    public void setUp() throws Exception {

        nullString = null;
        emptyString = "";
        badAmericanString = "08232017123723PM"; // a string with bad data
        goodAmericanString = "08/23/2017 12:37:23 PM"; // assuming data given is in ET (EDT or EST)
        expectedGoodOutput = "2017-08-23T11:37:23"; // output should always be in EST

    }

    /**
     * Test with null input, expected empty String out
     * @throws Exception if an assertion fails
     */
    @Test
    public void americanConverterNullInput() throws Exception {
        String iso8601Output =
                DateStandardsBuddy.garbageAmericanStringToISO8601ESTString(nullString);
        assertEquals(emptyString, iso8601Output);
    }


    /**
     * Test with empty String input, expected empty String out
     * @throws Exception if an assertion fails
     */
    @Test
    public void americanConverterEmptyInput() throws Exception {
        String iso8601Output =
                DateStandardsBuddy.garbageAmericanStringToISO8601ESTString(emptyString);
        assertEquals(emptyString, iso8601Output);
    }

    /**
     * Test with malformed input
     * @throws Exception if an assertion fails
     */
    @Test
    public void americanConverterBadInput() throws Exception {
        String iso8601Output =
                DateStandardsBuddy.garbageAmericanStringToISO8601ESTString(badAmericanString);
        assertEquals(emptyString, iso8601Output);
    }

    /**
     * Test with good input
     * @throws Exception if an assertion fails
     */
    @Test
    public void americanConverterGoodInput() throws Exception {
        String iso8601Output =
                DateStandardsBuddy.garbageAmericanStringToISO8601ESTString(goodAmericanString);
        assertEquals(expectedGoodOutput, iso8601Output);
    }
}
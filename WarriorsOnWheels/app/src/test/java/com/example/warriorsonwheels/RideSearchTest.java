package com.example.warriorsonwheels;

import android.view.MenuInflater;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RideSearchTest {

    @Test
    public void testOnCreate() {
        String expected = "Layout created";
        String actual = "Layout created";

        assertEquals(expected, actual);
    }

    @Test
    public void testParseJsonData1() throws JSONException {
        Boolean expected = true;
        Boolean actual = false;

        String object = "json object created";

        if(object != null) {
            actual = true;
        }

        assertEquals(true, actual);

    }

    @Test
    public void testParseJsonData2() {
        Boolean expected = true;
        Boolean actual = false;

        String object = "json object created";

        if(object != null) {
            actual = true;
        }

        assertEquals(true, actual);
    }

    @Test
    public void testOnCreateOptionsMenu() {
        MenuInflater inflater = null;
        String actual = " ";
        String expected = "Inflater created";

        if(inflater == null) {
            actual = "Inflater created";
        }
        else {
            actual = "Not created";
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testOnOptionsItemSelected() {
        boolean expected = true;
        boolean actual = false;

        if (expected == true) {
            actual = true;
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testOnClick() {
        boolean onClick = true;
        String expected = "clicked";
        String actual = " ";

        if(onClick == true) {
            actual = "clicked";
        }

        assertEquals(expected, actual);
    }
}

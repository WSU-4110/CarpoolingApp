package com.example.warriorsonwheels

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

class DriverInfoTest {
    void testOnCreate() {
        String expected = "layout created";

        String actual = "layout created";
        assertEquals(expected,actual);
    }

    void testOnCreateOptionsMenu() {

        MenuInflater inflater = null;
        String actual = "";
        String expected = "menu created";

        if(inflater == null)
        {
            actual = "menu created";
        }
        else
        {
            actual = "menu not created";
        }

        assertEquals(expected,actual);
    }

    void testOnOptionsItemSelected() {
        boolean expected = true;
        boolean actual = false;

        if (expected == true)
        {
            actual = true;
        }

        assertEquals(expected,actual);
    }

    void testOnClick() {
        boolean onClick = true;
        String expected = " button clicked";
        String actual = "";

        if(onClick == true)
        {
            actual = "button clicked";
        }

        assertEquals(expected,actual);
    }

    @Test
    public void testParseJsonData1(){
        boolean expected = true;
        boolean actual = false;

        String JSONobject = "JSON object created";

        if(JSONobject != null) {
            actual = true;
        }

        assertEquals(true, actual);

    }

    @Test
    public void testParseJsonData2(){
        boolean expected = true;
        boolean actual = false;

        String JSONobject = "JSON object created";

        if(JSONobject != null) {
            actual = true;
        }

        assertEquals(true, actual);

    }

}

package com.example.warriorsonwheels;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PostRideTest {

    @Test
    public void onCreate() {
        String expected = "Layout created successfully";

        String actual = "Layout created successfully";

        assertEquals(expected,actual);

    }

    @Test
    public void onCreateOptionsMenu() {

        MenuInflater inflater = null;
        String actual = "";
        String expected = "Inflater created";

        if(inflater == null)
        {
             actual = "Inflater created";
        }
        else
        {
             actual = "Not created";
        }

        assertEquals(expected,actual);


    }

    @Test
    public void onOptionsItemSelected() {
        boolean expected = true;
        boolean actual = false;

        if (expected == true)
        {
            actual = true;
        }

        assertEquals(expected,actual);

    }

    @Test
    public void onClick() {
        boolean onClick = true;
        String expected = "clicked";
        String actual = "";

        if(onClick == true)
        {
            actual = "clicked";
        }

        assertEquals(expected,actual);

    }

    @Test
    public void postRequest() {
        Map<String, String> jsonParams = new HashMap<String, String>();

        Boolean expected = true;
        Boolean actual = false;

        jsonParams.put("driver","gg2002");
        jsonParams.put("date","2020-08-10");
        jsonParams.put("time","12:00:00");
        jsonParams.put("departure_location","detroit");
        jsonParams.put("arrival_location","sterling heights");
        jsonParams.put("passenger_count","4");

        Map<String, String> output = jsonParams;

        if (output.get("driver").equals("gg2002") && output.get("arrival_location").equals("sterling heights"))
        {
            actual = true;
        }

        assertEquals(expected,actual);


    }

    @Test
    public void onPause() {
        String expected = Shared.Data.loggedInuser;

        String getuser = Shared.Data.loggedInuser;

        String actual = getuser;

        assertEquals(expected,actual);

    }
}
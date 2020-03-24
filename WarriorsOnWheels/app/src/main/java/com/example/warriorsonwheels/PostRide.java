package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PostRide extends AppCompatActivity implements View.OnClickListener{

    //Variables
    private EditText departureText, arrivalText;
    private EditText passengerCount;
    private Button shareRideButton;
    private Toolbar tbrMain;

    //private Button leaveDateButton, leaveTimeButton;
    //private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar calendar;
    private TimePicker leaveTime;
    private EditText leaveDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postride);

        //Toolbar
        tbrMain =  findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Set Variables
        departureText = findViewById(R.id.departureText);
        arrivalText = findViewById(R.id.arrivalText);
        passengerCount = findViewById(R.id.passengerCount);
        shareRideButton = findViewById(R.id.shareRideButton);

        leaveDate = findViewById(R.id.leaveDate);
        leaveTime = findViewById(R.id.leaveTimePicker);
        leaveTime.setIs24HourView(true);

    }

    //Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflowmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    //Menu Options
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homePage:
                Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent1);
                return true;

            case R.id.userProfilePage:
                Intent intent2 = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onClick (View v) {
        switch(v.getId())
        {
            //Go to FindPassengers.java
            case R.id.shareRideButton:
                postRequest();

                Intent intent = new Intent(getApplicationContext(), RatePassenger.class);
                startActivity(intent);
        }
    }

    public void postRequest()
    {
        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride";

        Map<String, String> jsonParams = new HashMap<String, String>();

        Integer hour = leaveTime.getHour();
        Integer min = leaveTime.getMinute();


        String time = hour.toString() + ":" + min.toString() + ":00";

        jsonParams.put("driver",Shared.Data.driverAccessID);
        jsonParams.put("date",leaveDate.getText().toString());
        jsonParams.put("time",time);
        jsonParams.put("departure_location",departureText.getText().toString());
        jsonParams.put("arrival_location",arrivalText.getText().toString());
        jsonParams.put("passenger_count",passengerCount.getText().toString());


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                // Name.setText(response.toString());
                Log.i("POST",response.toString());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR,"ERROR:","Volley Error");


                    }
                });
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);

    }
}

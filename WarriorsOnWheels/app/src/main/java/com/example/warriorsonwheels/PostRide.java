package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;

import android.text.format.DateFormat;
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

import java.math.BigDecimal;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class PostRide extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "PostRide";

    //Variables
    private EditText departureText, arrivalText;
    private EditText passengerCount;
    private Button shareRideButton, dateBtn, timeBtn;
    private Toolbar tbrMain;

    private Calendar calendar;
    private EditText leaveDate, leaveTime;

    String currentRideID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postride);

        //Toolbar
        tbrMain =  findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Set Variables
        departureText = (EditText) findViewById(R.id.departureText);
        arrivalText = (EditText)findViewById(R.id.arrivalText);
        passengerCount = (EditText)findViewById(R.id.passengerCount);
        shareRideButton = (Button) findViewById(R.id.shareRideButton);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        timeBtn = (Button) findViewById(R.id.timeBtn);

        leaveDate = (EditText) findViewById(R.id.leaveDate);
        leaveTime = (EditText) findViewById(R.id.leaveTime);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("yyyy-MM-dd", calendar1).toString();

                leaveDate.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("hh:mm:00", calendar1).toString();
                leaveTime.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

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

            case R.id.userLoginPage:
                Intent intent3 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent3);
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
                Intent intent = new Intent(getApplicationContext(), FindPassengers.class);
                startActivity(intent);
        }
    }

    public void postRequest()
    {
        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride";

        Map<String, String> jsonParams = new HashMap<String, String>();

        //Integer hour = leaveTimePicker.getHour();
        //Integer min = leaveTimePicker.getMinute();


        //String time = hour.toString() + ":" + min.toString() + ":00";

        jsonParams.put("driver",Shared.Data.loggedInuser);
        jsonParams.put("date",leaveDate.getText().toString());
        jsonParams.put("time",leaveTime.getText().toString());
        jsonParams.put("departure_location",departureText.getText().toString());
        jsonParams.put("arrival_location",arrivalText.getText().toString());
        jsonParams.put("passenger_count",passengerCount.getText().toString());


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                Log.i("POST",response.toString());
                try {
                    JSONObject obj = response.getJSONObject("data");
                    Shared.Data.currentRideId = obj.getInt("id");
                    Log.i("Ride ID", String.valueOf(Shared.Data.currentRideId));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR,"ERROR:","Volley Error " + error.toString());
                        Toast.makeText(PostRide.this, "Must be driver to post ride", Toast.LENGTH_SHORT).show();


                    }
                }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", Shared.Data.token);
                    return headers;
                }
        };
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();
        String sendCity = arrivalText.getText().toString();
        Shared.Data.arrival = sendCity;

        String sendLeaveCity = departureText.getText().toString();
        Shared.Data.departure = sendLeaveCity;

        String sendCurrentId = currentRideID;
    }

}

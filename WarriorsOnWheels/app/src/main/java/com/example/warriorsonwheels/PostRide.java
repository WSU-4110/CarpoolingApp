package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.widget.Toolbar;

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

public class PostRide extends AppCompatActivity {

    //Variables
    private EditText departureText;
    private EditText arrivalText;
    private TimePicker leaveTimePicker;
    private Spinner passengerCount;
    private Button shareRideButton;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postride);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Set Variables
        departureText = findViewById(R.id.departureText);
        arrivalText = findViewById(R.id.arrivalText);
        leaveTimePicker = findViewById(R.id.leaveTimePicker);
        passengerCount = findViewById(R.id.passengerCount);
        shareRideButton = findViewById(R.id.shareRideButton);

        //Set Time Picker to 24 hours
        leaveTimePicker.setIs24HourView(true);

    }

    public void onClick (View v) {
        switch(v.getId())
        {
            //Go to FindPassengers.java
            case R.id.findRideButton:
                postRequest();
                Intent intent = new Intent(getApplicationContext(), FindPassengers.class);
                startActivity(intent);
        }
    }

    public void postRequest()
    {
        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride";

        Map<String, String> jsonParams = new HashMap<String, String>();

        Integer hour = leaveTimePicker.getHour();
        Integer min = leaveTimePicker.getMinute();

        String time = hour.toString() + min.toString();


        jsonParams.put("driver",Shared.Data.driverAccessID);
        jsonParams.put("date","");
        jsonParams.put("time",time);
        jsonParams.put("departure_location",departureText.getText().toString());
        jsonParams.put("arrival_location",arrivalText.getText().toString());
        jsonParams.put("passenger_count",passengerCount.getSelectedItem().toString());


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

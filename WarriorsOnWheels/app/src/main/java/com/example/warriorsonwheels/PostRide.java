package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;

public class PostRide extends AppCompatActivity {

    //Variables
    private EditText departureText;
    private EditText arrivalText;
    private TimePicker leaveTimePicker;
    private Spinner passengerCount;
    private Button shareRideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createridelayout);

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
                Intent intent = new Intent(getApplicationContext(), FindPassengers.class);
                startActivity(intent);
        }
    }
}

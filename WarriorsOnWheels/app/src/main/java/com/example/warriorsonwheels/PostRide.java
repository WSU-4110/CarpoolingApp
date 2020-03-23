package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;

public class PostRide extends AppCompatActivity implements View.OnClickListener{

    //Variables
    private EditText departureText, arrivalText;
    private Spinner passengerCount;
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
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Set Variables
        departureText = findViewById(R.id.departureText);
        arrivalText = findViewById(R.id.arrivalText);
        passengerCount = findViewById(R.id.passengerCount);
        shareRideButton = findViewById(R.id.shareRideButton);

        leaveDate = findViewById(R.id.leaveDate);
        leaveTime = findViewById(R.id.leaveTimePicker);
    }

    //Create Menu
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
            case R.id.findRideButton:
                Intent intent = new Intent(getApplicationContext(), FindPassengers.class);
                startActivity(intent);
        }
    }
}

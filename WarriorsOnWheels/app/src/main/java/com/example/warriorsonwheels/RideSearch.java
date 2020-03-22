package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RideSearch extends AppCompatActivity{

    private LinearLayout Ride1,Ride2;
    private Button confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridesearch);

        //Buttons
        confirmButton = findViewById(R.id.confirmbutton);
        Ride1 = findViewById(R.id.FirstRide);
        Ride2 = findViewById(R.id.SecondRide);
    }

    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.FirstRide:
                confirmButton.setClickable(true);
                Shared.Data.selectedRide = 1;
                Intent intent1 = new Intent(getApplicationContext(), RiderInfo.class);
                startActivity(intent1);
                break;
            case R.id.SecondRide:
                confirmButton.setClickable(true);
                Shared.Data.selectedRide = 2;
                Intent intent2 = new Intent(getApplicationContext(), RiderInfo.class);
                startActivity(intent2);
                break;
            case R.id.confirmbutton:
        }
    }



}
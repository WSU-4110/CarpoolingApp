package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class RideSearch extends AppCompatActivity{

    private LinearLayout Ride1,Ride2;
    private Button confirmButton;
    private Toolbar tbrMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridesearch);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

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
                break;
            case R.id.SecondRide:
                confirmButton.setClickable(true);
                Shared.Data.selectedRide = 2;
                break;
            case R.id.confirmbutton:
                Intent intent3 = new Intent(getApplicationContext(), DriverInfo.class);
                startActivity(intent3);
        }
    }

}
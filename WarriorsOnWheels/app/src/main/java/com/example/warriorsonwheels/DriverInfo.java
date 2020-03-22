package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DriverInfo extends AppCompatActivity{

    private Button OptOutOfRide;
    private ImageView CarImage;
    private TextView driverName, driverPhone, driverRating,placeInLine,ArrivalTime, MakeModeYear, Color, LicensePlate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverinfo);

        //Buttons
        OptOutOfRide = findViewById(R.id.OptOutOfRide);
        driverName = findViewById(R.id.DriverName);
        driverPhone = findViewById(R.id.DriverPhone);
        driverRating = findViewById(R.id.DriverRating);
        placeInLine = findViewById(R.id.PassPlaceInLine);
        ArrivalTime = findViewById(R.id.ArrivalTime);
        MakeModeYear = findViewById(R.id.makeModelYear);
        Color = findViewById(R.id.Carcolor);
        LicensePlate = findViewById(R.id.CarLicense);
        //Initialize carImage
    }

    public void onClick (View v) {
        switch(v.getId())
        {
            //Go to FindPassengers.java
            case R.id.OptOutOfRide:
                //cancel ride
        }
    }
}

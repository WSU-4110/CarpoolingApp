package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class RateDriver extends AppCompatActivity{

    private Button Rate;
    private RatingBar RateDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverinfo);

        //Buttons
        RateDriver = findViewById(R.id.driverRatingBar);
        Rate = findViewById(R.id.RateDriver);
        //Initialize DriverImage
    }

    public void onClick(View v) {
        //add rating to DataBase
    }

}

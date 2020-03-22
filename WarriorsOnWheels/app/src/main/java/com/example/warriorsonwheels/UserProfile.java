package com.example.warriorsonwheels;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    //Pass Prof Var
    private TextView name, accessID, phNum, primLoc, passRating;

    //Driver Prof Var
    private TextView carMake, carModel, carYear, carColor, licensePlate, drivRating;

    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //TextViews
        name = findViewById(R.id.name);
        accessID = findViewById(R.id.accessID);
        phNum = findViewById(R.id.phNum);
        primLoc = findViewById(R.id.primLoc);
        passRating = findViewById(R.id.passRating);

        carMake = findViewById(R.id.carMake);
        carYear = findViewById(R.id.carYear);
        carModel = findViewById(R.id.carModel);
        carColor = findViewById(R.id.carColor);
        licensePlate = findViewById(R.id.licensePlate);
        drivRating = findViewById(R.id.drivRating);

    }



}

package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DriverProfile extends AppCompatActivity {

    private Button finishDriverProf;
    private EditText location, time, make, model, year, color, licensePlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengerprofile);

        //Buttons
        finishDriverProf = (Button)findViewById(R.id.finishDriver);

        //EditText
        location = findViewById(R.id.Loc);
        time = findViewById(R.id.time1);
        make = findViewById(R.id.make);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        color = findViewById(R.id.color);
        licensePlate = findViewById(R.id.license);

    }

    public void onClick(View v) {
        Intent intent1 = new Intent(getApplicationContext(), CreateRide.class);
        startActivity(intent1);
    }

}


package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity{

    private Button findRideButton;
    private Button postRideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createridelayout);

        //Button Ids
        findRideButton = (Button)findViewById(R.id.findRideButton);
        postRideButton = (Button)findViewById(R.id.postRideButton);

    }

    public void onClick(View v) {
        switch(v.getId())
        {
            //Go to RideSearch.java
            case R.id.findRideButton:
                Intent intent1 = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(intent1);

                //Go to PostRide.java
            case R.id.postRideButton:
                Intent intent2 = new Intent(getApplicationContext(), PostRide.class);
                startActivity(intent2);
        }
    }
}

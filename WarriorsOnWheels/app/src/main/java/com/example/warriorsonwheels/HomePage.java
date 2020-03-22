package com.example.warriorsonwheels;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity implements View.OnClickListener{

    private Button findRideButton;
    private Button postRideButton;
    private boolean isDriverHome;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //Button Ids
        findRideButton = findViewById(R.id.findRideButton);
        postRideButton = findViewById(R.id.postRideButton);

    }


    @Override
    protected void onResume() {
        super.onResume();
        isDriverHome = (Shared.Data.isDriver);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            //Go to RideSearch.java
            case R.id.findRideButton:
                Intent intent1 = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(intent1);

                //Go to PostRide.java
            case R.id.postRideButton:
                if (isDriverHome = false) {
                    //Toast message asking to go back and create a driver profile
                    // Define basic toast button
                    //postRideButton = (Button) findViewById(R.id.postRideButton);
                    postRideButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            toast = Toast.makeText(getApplicationContext(),
                                    "You must create a Driver Profile to post rides", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.END, 0, 0);
                            toast.show();
                        }
                    });
                }
                else {
                    Intent intent2 = new Intent(getApplicationContext(), PostRide.class);
                    startActivity(intent2);
                }
        }
    }
}

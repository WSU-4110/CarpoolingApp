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
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity implements View.OnClickListener{

    private Button findRideButton;
    private Button postRideButton;
    private boolean isDriverHome = false;
    private Toast toast;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Button Ids
        findRideButton = (Button) findViewById(R.id.findRideButton);
        postRideButton = (Button) findViewById(R.id.postRideButton);
    }

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
                if (Shared.Data.isDriverCheck = true) {
                    Intent intent2 = new Intent(getApplicationContext(), PostRide.class);
                    startActivity(intent2);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error:");
                    builder.setMessage("Driver Profile must be made to share rides.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
        }
    }
}

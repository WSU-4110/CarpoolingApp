package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        confirmButton = (Button) findViewById(R.id.confirmbutton);
        Ride1 = findViewById(R.id.FirstRide);
        Ride2 = findViewById(R.id.SecondRide);
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

            case R.id.userLoginPage:
                Intent intent3 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
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
                Intent intent3 = new Intent(getApplicationContext(), RateDriver.class);
                startActivity(intent3);
        }
    }

}
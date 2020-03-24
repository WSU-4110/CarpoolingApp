package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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



}

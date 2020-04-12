package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        setContentView(R.layout.ratedriver);

        //Buttons
        RateDriver = (RatingBar) findViewById(R.id.driverRatingBar);
        Rate = (Button) findViewById(R.id.rateDriver);
        //Initialize DriverImage
    }

    public void onClick(View v) {
        //add rating to DataBase
        switch(v.getId())
        {
            case R.id.rateDriver:
                Intent intent3 = new Intent(getApplicationContext(), Payment.class);
                startActivity(intent3);
                break;
        }
        
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

}

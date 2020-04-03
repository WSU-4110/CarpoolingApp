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
public class RatePassenger extends AppCompatActivity{

    private Button Rate;
    private RatingBar RatePassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratepassenger);

        //Buttons
        RatePassenger =(RatingBar) findViewById(R.id.passengerRatingBar);
        Rate = (Button) findViewById(R.id.RatePassenger);
        //Initialize DriverImage
    }

    public void onClick(View v) {
        //add rating to DataBase
        Intent intent3 = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent3);
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

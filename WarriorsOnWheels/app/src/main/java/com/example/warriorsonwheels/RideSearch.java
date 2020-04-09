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
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class RideSearch extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Ride1,Ride2, Ride3, Ride4, Ride5, Ride6, Ride7, Ride8;
    LinearLayout layout[] = {Ride1,Ride2, Ride3, Ride4, Ride5, Ride6, Ride7, Ride8};
    int layoutId[] = {R.id.FirstRide, R.id.SecondRide, R.id.ThirdRide, R.id.FourthRide, R.id.FifthRide, R.id.SixthRide, R.id.SeventhRide, R.id.EigthRide};
    private Button confirmButton;
    private Toolbar tbrMain;
    private RadioButton r1, r2, r3, r4, r5, r6, r7,r8;
    private TextView loc1, loc2, loc3, loc4, loc5, loc6, loc7, loc8, time1, time2, time3, time4, time5, time6, time7, time8, driver1, driver2, driver3, driver4, driver5, driver6, driver7, driver8;
    TextView locations[] = {loc1, loc2, loc3, loc4, loc5, loc6, loc7, loc8};
    int locationId[] = {R.id.left1,R.id.left2,R.id.left3,R.id.left4,R.id.left5,R.id.left6,R.id.left7,R.id.left8};
    TextView times[] = {time1, time2, time3, time4, time5, time6, time7, time8};
    int timeId[] = {R.id.depart1,R.id.depart2,R.id.depart3,R.id.depart4,R.id.depart5,R.id.depart6,R.id.depart7,R.id.depart8};
    TextView drivers[] = {driver1, driver2, driver3, driver4, driver5, driver6, driver7, driver8};
    int driverId[] = {R.id.driverName1,R.id.driverName2,R.id.driverName3,R.id.driverName4,R.id.driverName5,R.id.driverName6,R.id.driverName7,R.id.driverName8};
    RadioButton rides[] = {r1, r2, r3, r4, r5, r6, r7,r8};
    int rideId[] = {R.id.radioButton1,R.id.radioButton2,R.id.radioButton3,R.id.radioButton4,R.id.radioButton5,R.id.radioButton6,R.id.radioButton7,R.id.radioButton8};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridesearch);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Buttons
        confirmButton = (Button) findViewById(R.id.confirmbutton);
        confirmButton.setClickable(false);

        for(int i = 0; i < rides.length; i++)
        {
            rides[i] = findViewById(rideId[i]);
            rides[i].setOnClickListener(this);
            layout[i] = findViewById(layoutId[i]);
            locations[i] = findViewById(locationId[i]);
            times[i] = findViewById(timeId[i]);
            drivers[i] = findViewById(driverId[i]);
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

    public void onClick(View v) {

        for(int i = 0; i < rides.length; i++)
        {
            if(v.getId() == rideId[i])
            {
                confirmButton.setClickable(true);
            }
        }
        switch(v.getId())
        {
            case R.id.confirmbutton:
                Intent intent3 = new Intent(getApplicationContext(), RateDriver.class);
                startActivity(intent3);
                break;
        }
    }

    public void getRequest()
    {
        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride";
        Map<String, String> jsonParams = new HashMap<String, String>();

        String driverName = jsonParams.get("driver");
        String driverid = jsonParams.get("driver_id");
        String time = jsonParams.get("time");
        String location = jsonParams.get("location");

    }


}
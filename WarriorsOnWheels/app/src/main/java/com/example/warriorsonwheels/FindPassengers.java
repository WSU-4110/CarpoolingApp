package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FindPassengers extends AppCompatActivity implements View.OnClickListener {

    //Vars
    private Toolbar tbrMain;
    private TextView pass1, pass2, pass3, pass4, pass5, pass6;
    private Button refresh, start, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassengers);

        //Toolbar
        tbrMain =  findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Vars
        pass1 = (TextView) findViewById(R.id.pass1);
        pass2 = (TextView) findViewById(R.id.pass2);
        pass3 = (TextView) findViewById(R.id.pass3);
        pass4 = (TextView) findViewById(R.id.pass4);
        pass5 = (TextView) findViewById(R.id.pass5);
        pass6 = (TextView) findViewById(R.id.pass6);

        refresh = (Button) findViewById(R.id.refresh);
        start = (Button) findViewById(R.id.start);
        cancel = (Button) findViewById(R.id.cancel);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.refresh:
                Intent intent1 = new Intent(getApplicationContext(), FindPassengers.class);
                startActivity(intent1);
                break;
            case R.id.start:
                Intent intent2 = new Intent(getApplicationContext(), RideStarted.class);
                startActivity(intent2);
                break;
            case R.id.cancel:
                cancel();
                break;

        }
    }

    private void cancel() {
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
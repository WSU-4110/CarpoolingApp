package com.example.warriorsonwheels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Payment extends AppCompatActivity implements View.OnClickListener {
    public EditText nameOnCard, creditCardNum, expDate, cvv, zipCode;

    private Toolbar tbrMain;
    private Button payButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Vars
        nameOnCard = (EditText)findViewById(R.id.nameOnCard);
        creditCardNum = (EditText)findViewById(R.id.creditCardNum);
        expDate = (EditText)findViewById(R.id.expDate);
        cvv = (EditText)findViewById(R.id.securityCode);
        zipCode = (EditText)findViewById(R.id.zipCode);

        payButton = (Button) findViewById(R.id.payButton);

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

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.payButton:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(v.getContext());
                builder.setTitle("RIDE OVER");
                builder.setMessage("Thank you for using Warriors on Wheels!");
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(
                                    DialogInterface dialog, int option)
                            {
                                Intent intent4 = new Intent(getApplicationContext(), RateDriver.class);
                                startActivity(intent4);
                            }
                        });
        }
    }
}
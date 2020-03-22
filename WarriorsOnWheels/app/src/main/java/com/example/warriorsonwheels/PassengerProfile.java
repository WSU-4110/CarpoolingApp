package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;


public class PassengerProfile extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout NameLayout;
    private LinearLayout AccessLayout;
    private LinearLayout NumberLayout;
    private LinearLayout LocationLayout;
    private Button CreateDriveProf;
    private Button finishPassProf;
    private TextView Name,accessId, phoneNumber, location;
    private EditText nameInp, idInput, numberInput, locationInput;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengerprofile);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Buttons
        finishPassProf = findViewById(R.id.finishPassProf);
        CreateDriveProf = findViewById(R.id.createDrivProf);

        //Textview
        Name = findViewById(R.id.askName);
        accessId = findViewById(R.id.askAccessId);
        phoneNumber = findViewById(R.id.askPhoneNumber);
        location = findViewById(R.id.askLocation);

        //EditText
        nameInp = findViewById(R.id.Name);
        String sendNameInp = nameInp.getText().toString();

        idInput = findViewById(R.id.accessID);
        String sendidInput = idInput.getText().toString();

        numberInput = findViewById(R.id.PhoneNumber);
        String sendNumInp = numberInput.getText().toString();

        locationInput = findViewById(R.id.Location);
        String sendLocInput = locationInput.getText().toString();


    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.createDrivProf:
                Intent intent2 = new Intent(getApplicationContext(), DriverProfile.class);
                startActivity(intent2);
                break;
            case R.id.finishPassProf:
                Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //sends sign in info to userprofile.java


    }



}
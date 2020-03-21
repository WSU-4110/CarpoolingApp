package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PassengerProfile extends AppCompatActivity {

    private LinearLayout NameLayout;
    private LinearLayout AccessLayout;
    private LinearLayout NumberLayout;
    private LinearLayout LocationLayout;
    private Button CreateDriveProf;
    private Button finishPassProf;
    private TextView Name,accessId, phoneNumber, location;
    private EditText nameInp, idInput, numberInput, locationInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengerprofile);

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
        idInput = findViewById(R.id.accessID);
        numberInput = findViewById(R.id.PhoneNumber);
        locationInput = findViewById(R.id.Location);

    }

    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.createDrivProf:
                Intent intent = new Intent(getApplicationContext(), DriverProfile.class);
                startActivity(intent);
                break;
            case R.id.finishPassProf:
                Intent intent1 = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(intent1);
                break;
        }
    }

}


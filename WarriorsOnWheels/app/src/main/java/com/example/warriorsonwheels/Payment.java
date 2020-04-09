package com.example.warriorsonwheels;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Payment extends AppCompatActivity implements View.OnClickListener {
    EditText nameOnCard, creditCardNum, expDate, cvv, zipCode;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        nameOnCard = (EditText)findViewById(R.id.nameOnCard);
        creditCardNum = (EditText)findViewById(R.id.creditCardNum);
        expDate = (EditText)findViewById(R.id.expDate);
        cvv = (EditText)findViewById(R.id.securityCode);
        zipCode = (EditText)findViewById(R.id.zipCode);

    }


    @Override
    public void onClick(View v) {

    }
}
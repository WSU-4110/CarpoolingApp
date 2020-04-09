package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    EditText firstName, lastName, phoneNumber, email, pw, confirmPW;
    Button confirm;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        email = (EditText)findViewById(R.id.email);
        pw = (EditText)findViewById(R.id.pw);
        confirmPW = (EditText)findViewById(R.id.confirmpw);

        confirm = (Button) findViewById(R.id.confirmRegistration);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(String.valueOf(pw.getText()).equals(String.valueOf(confirmPW.getText())))
                {
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }
                else
                {

                    Toast toast = Toast.makeText(getApplicationContext(), "Password did not match",Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });




    }



}
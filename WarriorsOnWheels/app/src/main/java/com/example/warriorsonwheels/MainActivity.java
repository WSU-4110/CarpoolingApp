package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout profileSection;
    private Button signOut;
    private Button passProf;
    private SignInButton signIn;
    private TextView Name,Email;
    private ImageView profilePic;
    private Toolbar tbrMain;
    private Button loginButton,registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);


        //Bypass to Home Button
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        if(Shared.Data.loggedInuser != null)
        {
            Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent1);
        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.loginButton:

                Intent intent = new Intent (getApplicationContext(), Login.class);
                startActivity(intent);
                break;

            case R.id.registerButton:
                Intent intent1 = new Intent (getApplicationContext(), Registration.class);
                startActivity(intent1);
                break;


        }
    }


}
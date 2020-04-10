package com.example.warriorsonwheels;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

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
                Intent intent1 = new Intent (getApplicationContext(), PassengerProfile.class);
                startActivity(intent1);
                break;


        }
    }

}
package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText accessID, pw;
    Button confirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accessID = (EditText)findViewById(R.id.idInput);
        pw = (EditText)findViewById(R.id.pwDisplay);

        confirm = (Button) findViewById(R.id.submitLoginButton);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.submitLoginButton:
                Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent1);

        }
    }

}
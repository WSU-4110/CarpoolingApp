package com.example.warriorsonwheels;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.CountDownTimer;

public class FindPassengers extends AppCompatActivity {

    //Vars
    private Toolbar tbrMain;
    private TextView timer;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassengers);

        //Toolbar
        tbrMain =  findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);
        timer = (TextView) findViewById(R.id.timer);

        new CountDownTimer(50000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(counter));
                counter++;
            }
            @Override
            public void onFinish() {
                timer.setText("Finished");
            }
        }.start();

    }
}
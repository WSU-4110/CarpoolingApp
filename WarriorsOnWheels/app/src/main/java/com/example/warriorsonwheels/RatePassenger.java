package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RatePassenger extends AppCompatActivity{

    private Button Rate;
    private RatingBar RatePassenger;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratepassenger);

        //Buttons
        RatePassenger = findViewById(R.id.passengerRatingBar);
        Rate = findViewById(R.id.RatePassenger);
        imageView = findViewById(R.id.imageView);

        Glide.with(this).load(Shared.Data.imgURL).into(imageView);

        RatePassenger.setNumStars(5);
        //Initialize DriverImage
    }

    public void onClick(View v) {
        //add rating to DataBase
        switch (v.getId())
        {
            case R.id.RatePassenger:
                postRequest();
                Intent intent1 = new Intent(getApplicationContext(), FindPassengers.class);
                startActivity(intent1);


        }

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

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void postRequest()
    {

        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/rating/"+Shared.Data.driverAccessID;

        Map<String, String> jsonParams = new HashMap<String, String>();





        jsonParams.put("accessId",Shared.Data.driverAccessID);
        jsonParams.put("rating",String.valueOf(RatePassenger.getNumStars()));
        jsonParams.put("isDriver","true");


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                // Name.setText(response.toString());
                Log.i("POST",response.toString());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR,"ERROR:","Volley Error");


                    }
                });
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);

    }

}
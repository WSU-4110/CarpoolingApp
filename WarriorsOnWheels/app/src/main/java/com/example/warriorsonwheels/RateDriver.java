package com.example.warriorsonwheels;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RateDriver extends AppCompatActivity{

    private Button Rate;
    private RatingBar RateDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratedriver);

        //Buttons
        RateDriver = (RatingBar) findViewById(R.id.driverRatingBar);
        Rate = (Button) findViewById(R.id.rateDriver);
        //Initialize DriverImage
    }

    public void onClick(View v) {
        //add rating to DataBase
        switch(v.getId())
        {
            case R.id.rateDriver:
                Shared.Data.isPassenger = false;
                //postRequest();
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
                                Intent intent4 = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intent4);
                            }
                        });
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

            case R.id.userLoginPage:
                Intent intent3 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void postRequest()
    {

        String url = Shared.Data.url + "rating/" + Shared.Data.AccessIdDriver;

        Map<String, String> jsonParams = new HashMap<String, String>();


        jsonParams.put("rating",String.valueOf(RateDriver.getNumStars()));
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
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }

        };
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);

    }

}

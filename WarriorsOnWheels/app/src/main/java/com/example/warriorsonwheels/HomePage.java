package com.example.warriorsonwheels;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity implements View.OnClickListener{

    private Button findRideButton;
    private Button postRideButton;
    private Button createDrivProf;
    private TextView drivProfTitle;
    private boolean isDriverHome;
    private Toast toast;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        drivProfTitle = (TextView) findViewById(R.id.drivProfTitle);

        //Button Ids
        findRideButton = (Button) findViewById(R.id.findRideButton);
        postRideButton = (Button) findViewById(R.id.postRideButton);
        createDrivProf = (Button) findViewById(R.id.createDrivProf);

        checkDriver();
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

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            //Go to RideSearch.java
            case R.id.findRideButton:
                Intent intent1 = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(intent1);
                break;

            case R.id.createDrivProf:
                Intent intent2 = new Intent(getApplicationContext(), DriverProfile.class);
                startActivity(intent2);
                break;
                //Go to PostRide.java
            case R.id.postRideButton:
                    Intent intent3 = new Intent(getApplicationContext(), PostRide.class);
                    startActivity(intent3);
                break;
        }
    }

    public void checkDriver()
    {
        String url = Shared.Data.url + "driver/" + Shared.Data.loggedInuser;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Driver Profile Created",response.toString());

                            JSONObject dataobj = response.getJSONObject("data");

                            if (!dataobj.getString("id").equals("null"))
                            {
                                createDrivProf.setVisibility(View.INVISIBLE);
                                drivProfTitle.setVisibility(View.INVISIBLE);
                            }


                        } catch (JSONException e) {
                            Log.i("JSONException ERROR", e.toString()); }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.println(Log.ERROR,"ERROR:","Volley Error " + error.toString());

                            }
                        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}

package com.example.warriorsonwheels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    //Pass Prof Var
    private TextView name, accessID, phNum, primLoc, passRating;

    //Driver Prof Var
    private TextView carMake, drivRating,licensePlate;

    private Toolbar tbrMain;
    private LinearLayout driverProfLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //TextViews
        name = (TextView) findViewById(R.id.name);
        accessID = (TextView)findViewById(R.id.accessID);
        phNum = (TextView)findViewById(R.id.phNum);
        primLoc = (TextView)findViewById(R.id.primLoc);
        passRating = (TextView)findViewById(R.id.passRating);

        carMake = (TextView)findViewById(R.id.carMake);
        //licensePlate = (TextView)findViewById(R.id.licensePlate);
        drivRating = (TextView)findViewById(R.id.drivRating);

        driverProfLayout = (LinearLayout) findViewById(R.id.driverProfLayout);
        driverProfLayout.setVisibility(View.INVISIBLE);


        getUserData();

        if(Shared.Data.isDriverCheck = true)
        {
            driverProfLayout.setVisibility(View.VISIBLE);
            getDriverData();
        }


        Log.i("LOGGED IN USER",Shared.Data.loggedInuser);

    }

    public void getUserData()
    {
        String url = Shared.Data.url + "user/" + Shared.Data.loggedInuser;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("User Data:",response.toString());

                            JSONObject dataobj = response.getJSONObject("data");


                            name.setText(dataobj.getString("name"));
                            accessID.setText(dataobj.getString("access_id"));
                            phNum.setText(dataobj.getString("phone_number"));
                            primLoc.setText(dataobj.getString("street") + " " + dataobj.getString("city") + " " + dataobj.getString("state") + " " + dataobj.getString("zip"));
                            passRating.setText(dataobj.getString("rating"));




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

    public void getDriverData()
    {
        String url = Shared.Data.url + "driver/" + Shared.Data.loggedInuser;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("User Data:",response.toString());

                            JSONObject dataobj = response.getJSONObject("data");
                            carMake.setText(dataobj.getString("car"));
                            drivRating.setText(dataobj.getString("rating"));



                        } catch (JSONException e) {
                            Log.i("JSONException ERROR", e.toString()); }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //carMake.setText("NOT A DRIVER");
                                driverProfLayout.setVisibility(View.INVISIBLE);
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
                Shared.Data.token = null;
                Intent intent3 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }



}

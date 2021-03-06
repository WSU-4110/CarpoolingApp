package com.example.warriorsonwheels;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverInfo extends AppCompatActivity{

    private Button OptOutOfRide, goToMap;
    private ImageView CarImage;
    private Toolbar tbrMain;
    private TextView driverName, driverPhone, driverRating,placeInLine,ArrivalTime, MakeModeYear, Color, LicensePlate, Date;
    String url1 = Shared.Data.url +  "ride/" + Shared.Data.selectedRideId;
    String url2 = Shared.Data.url + "driver/" + Shared.Data.AccessIdDriver;
    String url3 = Shared.Data.url + "rating/" + Shared.Data.AccessIdDriver;
    ProgressDialog dialog;
    String driver, car, phone, rating, time, date;
    boolean rideStarted = false;
    ArrayList<String> accessIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverinfo);

        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Buttons
        OptOutOfRide = (Button) findViewById(R.id.OptOutOfRide);
        goToMap = (Button) findViewById(R.id.goToMap);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        driverName = (TextView) findViewById(R.id.DriverName);
        driverPhone = (TextView) findViewById(R.id.DriverPhone);
        driverRating = (TextView) findViewById(R.id.DriverRating);
        //placeInLine = (TextView) findViewById(R.id.PassPlaceInLine);
        ArrivalTime = (TextView) findViewById(R.id.ArrivalTime);
        MakeModeYear = (TextView) findViewById(R.id.makeModelYear);
        Date = findViewById(R.id.Date);
        //Color = (TextView) findViewById(R.id.Carcolor);
        //LicensePlate = (TextView) findViewById(R.id.CarLicense);
        //Initialize carImage

        getRequest1();
        getRequest2();
        getRequest3();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflowmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(getApplicationContext(), "To go back, you have to opt out of ride.", Toast.LENGTH_SHORT).show();
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
                Shared.Data.loggedInuser = null;
                Intent intent3 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onClick (View v) {
        switch(v.getId())
        {
            //Go to FindPassengers.java
            case R.id.OptOutOfRide:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setTitle("END RIDE");
                builder.setMessage("Are you sure you want to opt out of this ride?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(
                                    DialogInterface dialog, int option)
                            {

                                if(rideStarted)
                                {
                                    Toast.makeText(getApplicationContext(), "The Driver has already started the ride. You can no longer opt out.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    optOutOfRide();

                                    Intent intent3 = new Intent(getApplicationContext(), RideSearch.class);
                                    startActivity(intent3);
                                }
                            }
                        });
                builder.setNegativeButton("No", null);
                builder.show();
                break;
            case R.id.goToMap:
                getRiders();
                Shared.Data.currentRidePassengerIds  = accessIds;
                Intent intent2 = new Intent(getApplicationContext(), RideMap.class);
                startActivity(intent2);
                break;
        }
    }

    void parseJsonData1(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject data = object.getJSONObject("data");
            System.out.println("the ride info: " + data.toString());
            car = data.getString("car");
            String dateTime = data.getString("date");
            time = dateTime.substring(dateTime.lastIndexOf('T') + 1 , dateTime.length() - 8);
            date = dateTime.substring(0, dateTime.lastIndexOf('T'));
            rideStarted = !data.getBoolean("pending");
            System.out.println("driver details: " + driver + date + phone);
            Date.setText(date);
            MakeModeYear.setText(car);
            ArrivalTime.setText(time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }

    void parseJsonData2(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject data = object.getJSONObject("data");
            System.out.println("the driver info: " + data.toString());
            driver = data.getString("name");
            phone = data.getString("phone_number");
            //rating = data.getString("rating");

            driverName.setText(driver);
            driverPhone.setText(phone);
            //driverRating.setText(rating);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }

    void parseJsonData3(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject data = object.getJSONObject("data");
            Log.i("POST",data.toString());
            if(data.getJSONObject("driver").length() != 0) {
                JSONObject driver = data.getJSONObject("driver");
                rating = driver.getString("average");
                driverRating.setText(rating);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }
    void parseJsonData4(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");

            for(int i = 0; i < ridesArray.length(); ++i) {
                JSONObject dataobj = ridesArray.getJSONObject(i);
                //if(!dataobj.toString().equals("{}")) {
                //passengers.add(dataobj.getString("name"));
                accessIds.add(dataobj.getString("access_id"));
                Shared.Data.AccessIdPass = dataobj.getString("access_id");
                //}
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getRequest1()
    {
        StringRequest request = new StringRequest(url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData1(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast toast = Toast.makeText(getApplicationContext(), volleyError.toString(),Toast.LENGTH_LONG);
                toast.show();
                dialog.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }

        };
        RequestQueue rQueue = Volley.newRequestQueue(DriverInfo.this);
        rQueue.add(request);
    }

    public void getRequest2()
    {
        StringRequest request = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData2(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast toast = Toast.makeText(getApplicationContext(), volleyError.toString(),Toast.LENGTH_LONG);
                toast.show();
                dialog.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }

        };
        RequestQueue rQueue = Volley.newRequestQueue(DriverInfo.this);
        rQueue.add(request);
    }

    public void getRequest3()
    {
        StringRequest request = new StringRequest(url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData3(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast toast = Toast.makeText(getApplicationContext(), volleyError.toString(),Toast.LENGTH_LONG);
                toast.show();
                dialog.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }

        };
        RequestQueue rQueue = Volley.newRequestQueue(DriverInfo.this);
        rQueue.add(request);
    }

    public void optOutOfRide()
    {
        String url = Shared.Data.url +  "ride/" + Shared.Data.selectedRideId + "/users/" + Shared.Data.loggedInuser;
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("type","0");


        JsonObjectRequest delRequest = new JsonObjectRequest(Request.Method.DELETE, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                Log.i("DELETE",response.toString());

            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR,"ERROR:","Volley Error " + error.toString());
                        rideStarted = true;
                        Toast toast = Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG);
                        toast.show();

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }
        };
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(delRequest);

    }

    public void getRiders()
    {
        String url = Shared.Data.url + "ride/" + Shared.Data.selectedRideId + "/users";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData4(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast toast = Toast.makeText(getApplicationContext(), volleyError.toString(),Toast.LENGTH_LONG);
                toast.show();
                //dialog.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }

        };
        RequestQueue rQueue = Volley.newRequestQueue(DriverInfo.this);
        rQueue.add(request);
    }
}

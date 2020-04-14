package com.example.warriorsonwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverInfo extends AppCompatActivity{

    private Button OptOutOfRide;
    private ImageView CarImage;
    private TextView driverName, driverPhone, driverRating,placeInLine,ArrivalTime, MakeModeYear, Color, LicensePlate, Date;
    String url1 = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride/" + Shared.Data.selectedRideId;
    String url2 = "https://carpool-api-r64g2xh4xa-uc.a.run.app/driver/" + Shared.Data.AccessIdDriver;
    String url3 = "https://carpool-api-r64g2xh4xa-uc.a.run.app/rating/" + Shared.Data.AccessIdDriver;
    ProgressDialog dialog;
    String driver, car, phone, rating, time, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverinfo);

        //Buttons
        OptOutOfRide = (Button) findViewById(R.id.OptOutOfRide);

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
        //getRequest3();


    }

    public void onClick (View v) {
        switch(v.getId())
        {
            //Go to FindPassengers.java
            case R.id.OptOutOfRide:
                //cancel ride
                break;
            case R.id.DriverRatingButton:
                Intent intent2 = new Intent(getApplicationContext(), DuringRide.class);
                startActivity(intent2);
                break;
        }
    }

    void parseJsonData1(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject data = object.getJSONObject("data");
            System.out.println("the ride ifo: " + data.toString());
            car = data.getString("car");
            String dateTime = data.getString("date");
            time = dateTime.substring(dateTime.lastIndexOf('T') + 1 , dateTime.length() - 8);
            date = dateTime.substring(0, dateTime.lastIndexOf('T'));
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
            System.out.println("the driver ifo: " + data.toString());
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
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
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
}

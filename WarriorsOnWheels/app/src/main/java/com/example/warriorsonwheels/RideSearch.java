package com.example.warriorsonwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class RideSearch extends AppCompatActivity implements View.OnClickListener{

    private Button confirmButton;
    private Toolbar tbrMain;
    private ListView rideList;
    ArrayList<String> departs = new ArrayList<String>();
    ArrayList<String> times= new ArrayList<String>();
    ArrayList<String> arrives = new ArrayList<String>();
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<String> passengers = new ArrayList<String>();
    ArrayList<Integer> rideId = new ArrayList<Integer>();
    ArrayList<Integer> driverId = new ArrayList<Integer>();
    ArrayList<String> riders = new ArrayList<String>();

    ArrayList<String> drivers = new ArrayList<String>();
    String url1 = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride";
    String url2 = "";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridesearch);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        //Buttons
        confirmButton = (Button) findViewById(R.id.confirmbutton);
        confirmButton.setClickable(false);

        rideList = (ListView) findViewById(R.id.rideList);
        rideList.setSelector(R.drawable.list_item_selector);

        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                confirmButton.setClickable(true);
                confirmButton.setOnClickListener(RideSearch.this);
                Shared.Data.selectedDriverId = driverId.get(position);
                Shared.Data.selectedRideId = rideId.get(position);
                Shared.Data.AccessIdDriver = drivers.get(position);
                view.getFocusables(position);
                view.setSelected(true);
            }
        });

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
        RequestQueue rQueue = Volley.newRequestQueue(RideSearch.this);
        rQueue.add(request);


    }

    void parseJsonData1(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");

            for(int i = 0; i < ridesArray.length(); ++i) {
                JSONObject dataobj = ridesArray.getJSONObject(i);

                //if(!dataobj.toString().equals("{}")) {
                    departs.add(dataobj.getString("departure_location"));
                    String dateTime = dataobj.getString("date");
                    arrives.add(dataobj.getString("arrival_location"));
                    dates.add(dateTime.substring(0,dateTime.lastIndexOf('T')));
                    times.add(dateTime.substring(dateTime.lastIndexOf('T') + 1 , dateTime.length() - 8));
                    passengers.add(dataobj.getString("passenger_count"));
                    rideId.add(dataobj.getInt("id"));
                    driverId.add(dataobj.getInt("driverId"));
                    drivers.add(dataobj.getString("access_id"));
                //}

            }

            CustomListAdapter whatever = new CustomListAdapter(this, times, arrives, dates);
            rideList.setAdapter(whatever);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    void parseJsonData2(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");

            for(int i = 0; i < ridesArray.length(); ++i) {
                JSONObject dataobj = ridesArray.getJSONObject(i);
                //if(!dataobj.toString().equals("{}")) {
                riders.add(dataobj.getString("access_id"));
                //}

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
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

    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.confirmbutton:

                url2 = url1 + "/" + Shared.Data.selectedRideId + "/users";
                getRiders();
                riders.add(Shared.Data.loggedInuser);
                for(int i = 0; i < riders.size(); i++)
                {
                    Log.i("----------------------riders: ", riders.get(i));
                }
                postRequest();
                Intent intent3 = new Intent(getApplicationContext(), DriverInfo.class);
                startActivity(intent3);
                //Intent intent2 = new Intent(getApplicationContext(), RateDriver.class);
                //startActivity(intent2);
                break;
        }
    }

    public void postRequest()
    {
        Map<String, ArrayList<String>> jsonParams = new HashMap<>();

        jsonParams.put("users", riders);
        Log.i("after putting",jsonParams.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url2, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                Log.i("POST",response.toString());


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
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);

    }

    public void getRiders()
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
        RequestQueue rQueue = Volley.newRequestQueue(RideSearch.this);
        rQueue.add(request);
    };

}
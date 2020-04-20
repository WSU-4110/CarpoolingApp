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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class FindPassengers extends AppCompatActivity implements View.OnClickListener {

    //Vars
    private Toolbar tbrMain;
    //private TextView pass1, pass2, pass3, pass4, pass5, pass6;
    private Button refresh, start, cancel;
    private ListView passList;
    ArrayList<String> passengers = new ArrayList<String>();
    ArrayList<String> accessIds = new ArrayList<String>();
    //int rideId = Shared.Data.currentRideId;

    String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride/";
    //String newUrl = " ";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassengers);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Vars
        refresh = (Button) findViewById(R.id.refresh);
        start = (Button) findViewById(R.id.start);
        cancel = (Button) findViewById(R.id.cancel);
        passList = (ListView) findViewById(R.id.passList);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        // --------------------------------------------------------------
        //
        //                  POST ALL RIDE IDS
        //
        // --------------------------------------------------------------
        passList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                refresh.setClickable(true);
                refresh.setOnClickListener(FindPassengers.this);
                passengers.get(position);
                accessIds.get(position);
            }
        });
    }

    // --------------------------------------------------------------
    //
    //                  DISPLAY USERS WHO HAVE CHOSEN RIDE
    //
    // --------------------------------------------------------------
    void parseJsonData2(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");

            for(int i = 0; i < ridesArray.length(); ++i) {
                JSONObject dataobj = ridesArray.getJSONObject(i);
                //if(!dataobj.toString().equals("{}")) {
                passengers.add(dataobj.getString("name"));
                accessIds.add(dataobj.getString("access_id"));
                //}
            }

            PassengerListAdapter whatever = new PassengerListAdapter(this, passengers, accessIds);
            passList.setAdapter(whatever);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    public void getRiders()
    {
        url = url + Shared.Data.currentRideId + "/users";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
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
        RequestQueue rQueue = Volley.newRequestQueue(FindPassengers.this);
        rQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.refresh:
                getRiders();
//                Intent intent1 = new Intent(getApplicationContext(), FindPassengers.class);
//                startActivity(intent1);
                break;

            case R.id.start:
                postRideEvent();
                Shared.Data.currentRidePassengerIds  = accessIds;
                Intent intent2 = new Intent(getApplicationContext(), RideMap.class);
                startActivity(intent2);
                break;

            case R.id.cancel:
                cancel(v);
                break;

        }
    }

    private void cancel (View v) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(v.getContext());
        builder.setTitle("END RIDE");
        builder.setMessage("Are you sure you want to cancel your ride?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(
                            DialogInterface dialog, int option)
                    {

                        //TO DO
                        // ADD ABILITY TO CANCEL RIDE

                        Intent intent3 = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent3);
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(
                            DialogInterface dialog, int option)
                    {
                        System.out.println("Ride will not be cancelled.");
                    }
                });
        builder.show();
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


    public void postRideEvent()
    {
        String url = Shared.Data.url + "/ride/" + Shared.Data.currentRideId + "/events";

        Map<String, String> jsonParams = new HashMap<String, String>();


        jsonParams.put("type","0");


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
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

}
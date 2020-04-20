package com.example.warriorsonwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RideMap extends FragmentActivity implements OnMapReadyCallback {

    private Toolbar tbrMain;
    private GoogleMap mMap;
    private Button endRide, passengerUpdate;
    private TextView address;
    ProgressDialog dialog;
    String street, city, state, zip;
    //String passengerid;
    ArrayList<String> passengerIds = Shared.Data.currentRidePassengerIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridemap);

        //Toolbar
        tbrMain =  findViewById(R.id.tbrMain);
        //setSupportActionBar(tbrMain);
        endRide = (Button) findViewById(R.id.endRide);
        passengerUpdate = (Button) findViewById(R.id.passengerUpdate);
        address = (TextView) findViewById(R.id.address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.onResume(); // needed to get the map to display immediately

        if (Shared.Data.isDriverCheck = false) {
            passengerUpdate.setVisibility(View.INVISIBLE);
        }

        for(int i = 0; i < passengerIds.size(); i++) {
            String id = passengerIds.get(i);
            getAddress(id);

        }

        endRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.endRide:
                        if(Shared.Data.isDriverCheck)
                        {
                            endRide();
                            Intent intent1 = new Intent(getApplicationContext(), RatePassenger.class);
                            startActivity(intent1);
                            break;
                        }
                        else
                        {
                            Intent intent2 = new Intent(getApplicationContext(), Payment.class);
                            startActivity(intent2);
                            break;
                        }

                    }
            }
        });

        passengerUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRideEvent();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker
    }

    public void getAddress(String id) {

    }

    public void endRide()
    {
        String url = Shared.Data.url + "/ride/" + Shared.Data.currentRideId + "/events";

        Map<String, String> jsonParams = new HashMap<String, String>();

        //jsonParams.put("access_id","gh4683");
        jsonParams.put("type","2");

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

    public void updateRideEvent() {
        String url = Shared.Data.url + "/ride/" + Shared.Data.currentRideId + "/events";

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("type","1");

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

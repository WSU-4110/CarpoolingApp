package com.example.warriorsonwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    ArrayList<String> addresses = new ArrayList<String>();
    String url;
    String [] setLatLng;
    float latitude, longitude;

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

        if (!Shared.Data.isPassenger) {
            passengerUpdate.setVisibility(View.VISIBLE);
            endRide.setVisibility(View.VISIBLE);
        }
        else if (passengerIds != null) {
            for(int i = 0; i < passengerIds.size(); i++) {
                String id = passengerIds.get(i);
                getAddress(id);
            }

            for(int i = 0; i < addresses.size(); i++) {
                String pickUpLocation = addresses.get(i);
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(pickUpLocation,
                        getApplicationContext(), new GeocoderHandler());
            }
        }
        /*else if (passengerIds.isEmpty()) {
            //set default marker
            LatLng wayne = new LatLng(42.357184, -83.069852);
            mMap.addMarker(new MarkerOptions().position(wayne).title("Default Location: WSU"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne));
        }*/

        endRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.endRide:
                        if(!Shared.Data.isPassenger)
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

        // Add default marker at wsu
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Set Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            setLatLng =  locationAddress.split(",");
            latitude = Float.parseFloat(setLatLng[0]);
            longitude = Float.parseFloat(setLatLng[1]);
        }
    }

    public void getAddress(String id) {
        url = Shared.Data.url + "users/" + id;
        getRequest();
    }

    public void parseAddress(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject data = object.getJSONObject("data");
            String street = data.getString("street");
            String city = data.getString("city");
            String state = data.getString("state");
            String zip = data.getString("zip");
            String address = street + " " + city + " " + state + " " + zip;
            System.out.println("address: " + street + " " + city + " " + state + " " + zip);

            addresses.add(address);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }

    public void getRequest() {
        {
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {
                    parseAddress(string);
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
            RequestQueue rQueue = Volley.newRequestQueue(RideMap.this);
            rQueue.add(request);
        }
    }

    public void endRide()
    {

        String url = Shared.Data.url + "ride/" + Shared.Data.mySelectedRideId + "/events";



        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("type","2");

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                Log.i("POST",response.toString());
                deleteRide();
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

            String url = Shared.Data.url + "ride/" + Shared.Data.mySelectedRideId + "/events";



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

    public void deleteRide()
    {
        String url = Shared.Data.url + "ride/" + Shared.Data.mySelectedRideId;

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

}

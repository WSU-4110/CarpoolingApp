package com.example.warriorsonwheels;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DuringRide extends FragmentActivity implements OnMapReadyCallback {

    private Toolbar tbrMain;
    private GoogleMap mMap;
    private Button endRide;
    private TextView city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duringride);

        //Toolbar
        tbrMain =  findViewById(R.id.tbrMain);
        //setSupportActionBar(tbrMain);
        endRide = (Button) findViewById(R.id.endRide);

        city = (TextView) findViewById(R.id.city);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.onResume(); // needed to get the map to display immediately

        endRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.endRide:
                        if(Shared.Data.isDriverCheck)
                        {
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker
        //city.setText(Shared.Data.arrival);
        LatLng address = new LatLng(42.358694, -83.070194);
        mMap.addMarker(new MarkerOptions().position(address).title("Marker is Placed"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
    }


}

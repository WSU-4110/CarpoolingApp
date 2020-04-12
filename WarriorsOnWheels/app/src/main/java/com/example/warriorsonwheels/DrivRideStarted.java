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

public class DrivRideStarted extends FragmentActivity implements OnMapReadyCallback {

    private Toolbar tbrMain;
    private GoogleMap mMap;
    private Button endRide;
    private TextView city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driv_ridestarted);

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
                        Intent intent1 = new Intent(getApplicationContext(), RatePassenger.class);
                        startActivity(intent1);
                        break;
                    }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        city.setText(Shared.Data.city);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker
        place_marker();
    }

    private void place_marker() {
        if (Shared.Data.city == "Detroit") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.358694, -83.070194);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Wayne State"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Troy") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.584797, -83.145991);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Troy"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Canton") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.303633, -83.481893);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Canton"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Dearborn") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(442.314976, -83.209805);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Dearborn"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Sterling Heights") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.583302, -83.018528);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Sterling Heights"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Warren") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.509041, -83.015191);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Warren"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Rochester") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.682856, -83.134941);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Rochester"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Auburn Hills") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.684264, -83.235335);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Auburn Hills"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Plymouth") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.368875, -83.470572);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Plymouth"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
        else if (Shared.Data.city == "Northville") {
            city.setText(Shared.Data.city);
            LatLng wayne_state = new LatLng(42.428048, -83.482788);
            mMap.addMarker(new MarkerOptions().position(wayne_state).title("Marker in Northville"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wayne_state));
        }
    }


}

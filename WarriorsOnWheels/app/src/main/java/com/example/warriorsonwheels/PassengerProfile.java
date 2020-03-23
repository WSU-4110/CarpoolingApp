package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PassengerProfile extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout NameLayout;
    private LinearLayout AccessLayout;
    private LinearLayout NumberLayout;
    private LinearLayout LocationLayout;
    private Button CreateDriveProf;
    private Button finishPassProf;
   // private TextView Name,accessId, phoneNumber, location;
    private EditText nameInp, idInput, numberInput, locationInput;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengerprofile);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Buttons
        finishPassProf = findViewById(R.id.finishPassProf);
        CreateDriveProf = findViewById(R.id.createDrivProf);



        //EditText
        nameInp = findViewById(R.id.Name);
        //String sendNameInp = nameInp.getText().toString();

        idInput = findViewById(R.id.accessID);
        //String sendidInput = idInput.getText().toString();

        numberInput = findViewById(R.id.PhoneNumber);
        //String sendNumInp = numberInput.getText().toString();

        locationInput = findViewById(R.id.Location);
        //String sendLocInput = locationInput.getText().toString();


    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.createDrivProf:
                postRequest();
                Intent intent2 = new Intent(getApplicationContext(), DriverProfile.class);
                startActivity(intent2);
                break;
            case R.id.finishPassProf:
                postRequest();
                Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //sends sign in info to userprofile.java
        String sendNameInp = nameInp.getText().toString();
        Shared.Data.userName = sendNameInp;

        String sendidInput = idInput.getText().toString();
        Shared.Data.userId = sendidInput;

        String sendNumInp = numberInput.getText().toString();
        Shared.Data.phNumber = sendNumInp;

        String sendLocInput = locationInput.getText().toString();
        Shared.Data.userLoc = sendLocInput;
    }

    public void postRequest()
    {
        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/user";

        Map<String, String> jsonParams = new HashMap<String, String>();



        //Need to add Date, Departure Location, Arrival Location
        jsonParams.put("name",nameInp.getText().toString());
        jsonParams.put("phone_number",numberInput.getText().toString());
        jsonParams.put("location",locationInput.getText().toString());
        jsonParams.put("access_id",idInput.getText().toString());


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                // Name.setText(response.toString());
                Log.i("POST",response.toString());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR,"ERROR:","Volley Error");


                    }
                });
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);


    }



}
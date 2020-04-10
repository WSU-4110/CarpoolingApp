package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    //private Button CreateDriveProf;
    private Button finishPassProf;
   // private TextView Name,accessId, phoneNumber, location;
    private EditText nameInp, idInput, numberInput, pw, confirmPW;
    private EditText street, city, state, zipCode;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengerprofile);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Buttons
        finishPassProf = (Button) findViewById(R.id.finishPassProf);
        //CreateDriveProf = (Button) findViewById(R.id.createDrivProf);

        //EditText
        nameInp = (EditText) findViewById(R.id.Name);
        idInput = (EditText) findViewById(R.id.accessID);
        numberInput = (EditText) findViewById(R.id.PhoneNumber);

        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zipCode = (EditText) findViewById(R.id.zipCode);

        pw = (EditText)findViewById(R.id.pw);
        confirmPW = (EditText)findViewById(R.id.confirmpw);

        finishPassProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(String.valueOf(pw.getText()).equals(String.valueOf(confirmPW.getText())))
                {
                    postRequest();
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }
                else
                {

                    Toast toast = Toast.makeText(getApplicationContext(), "Password did not match",Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.finishPassProf:
                postRequest();
                Intent intent1 = new Intent(getApplicationContext(), Login.class);
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

        String sendLocInput = city.getText().toString();
        Shared.Data.userLoc = sendLocInput;
    }

    public void postRequest()
    {
        String address = street.toString() + " " + city.toString() + " " + state.toString() + " " + zipCode.toString();

        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/user";

        Map<String, String> jsonParams = new HashMap<String, String>();

        //Need to add Date, Departure Location, Arrival Location
        jsonParams.put("name",nameInp.getText().toString());
        jsonParams.put("phone_number",numberInput.getText().toString());
        jsonParams.put("location", address);
        jsonParams.put("access_id",idInput.getText().toString());
        jsonParams.put("password",pw.getText().toString());


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
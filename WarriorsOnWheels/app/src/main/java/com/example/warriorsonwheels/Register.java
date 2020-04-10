package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText firstName, lastName, phoneNumber, accessID, pw, confirmPW,location;
    Button confirm;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        accessID = (EditText)findViewById(R.id.accessId);
        pw = (EditText)findViewById(R.id.pw);
        confirmPW = (EditText)findViewById(R.id.confirmpw);
        location = (EditText)findViewById(R.id.userLocation);
        phoneNumber = (EditText)findViewById(R.id.PhoneNumber);

        confirm = (Button) findViewById(R.id.confirmRegistration);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(String.valueOf(pw.getText()).equals(String.valueOf(confirmPW.getText())))
                {
                    createUser();
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

    public void createUser()
    {
        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/user";

        Map<String, String> jsonParams = new HashMap<String, String>();

        String name = firstName.getText().toString() + " " + lastName.getText().toString();


        jsonParams.put("name",name);
        jsonParams.put("access_id",accessID.getText().toString());
        jsonParams.put("password",pw.getText().toString());
        jsonParams.put("phone_number",phoneNumber.getText().toString());
        jsonParams.put("location",location.getText().toString());



        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                Log.i("POST", response.toString());

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
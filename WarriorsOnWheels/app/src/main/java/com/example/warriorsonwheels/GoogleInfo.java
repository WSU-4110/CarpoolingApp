package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

public class GoogleInfo extends AppCompatActivity implements View.OnClickListener  {

    private RequestQueue queue;

    private LinearLayout profileSection;
    private Button signOut;
    private Button passProf;
    private SignInButton signIn;
    private TextView Name,Email;
    private ImageView profilePic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_info);

        queue = Volley.newRequestQueue(this);

        Name = (TextView)findViewById(R.id.nameDisplay);
        Email = (TextView)findViewById(R.id.emailDisplay);
        profilePic = (ImageView)findViewById(R.id.prof_pic);
        signOut = (Button)findViewById(R.id.bn_signOut);
        passProf = (Button)findViewById(R.id.passProf);

        Name = findViewById(R.id.nameDisplay);
        Email = findViewById(R.id.emailDisplay);
        profilePic = findViewById(R.id.prof_pic);
        signOut = findViewById(R.id.bn_signOut);
        passProf = findViewById(R.id.passProf);

        signOut.setOnClickListener(this);
        passProf.setOnClickListener(this);


        Name.setText(((sharedVars) this.getApplication()).name);
        Email.setText(((sharedVars) this.getApplication()).email);
        Glide.with(this).load(((sharedVars) this.getApplication()).imgURL).into(profilePic);

        String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/rating/ab1234";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                Name.setText(response.toString());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR,"ERROR:","Volley Error");


                    }
                });

        //Makes API Call
        RequestQueue queue = MySingleton.getInstance(this).getRequestQueue();
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }

    //Send profile pic image to UserProfile.java
    @Override
    protected void onPause() {
        super.onPause();

        //sends sign in info to userprofile.java
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bn_signOut:
                signOut();
                break;
            case R.id.passProf:
                Intent intent1 = new Intent(getApplicationContext(), PassengerProfile.class);
                startActivity(intent1);
                break;
        }
    }

    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });

        Intent signOut = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(signOut);
    }



}
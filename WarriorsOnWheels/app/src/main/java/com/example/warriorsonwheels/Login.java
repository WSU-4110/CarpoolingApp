package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText accessID, pw;
    Button confirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accessID = (EditText) findViewById(R.id.idInput);
        pw = (EditText) findViewById(R.id.pwDisplay);

        confirm = (Button) findViewById(R.id.submitLoginButton);

        FirebaseApp.initializeApp(this);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(getApplicationContext(), "Processing...", Toast.LENGTH_LONG);
                toast.show();


                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("FB INSTANCE", "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d("FB INSTANCE", msg);
                                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });







                loginPost();
            }
        });
    }


        public void loginSuccess (boolean success)
        {
            if (success) {
                Shared.Data.loggedInuser = accessID.getText().toString();
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Unsuccessful Login", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        public void loginPost ()
        {
            String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/user/auth";

            Map<String, String> jsonParams = new HashMap<String, String>();


            jsonParams.put("access_id", accessID.getText().toString());
            jsonParams.put("password", pw.getText().toString());


            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject data = response.getJSONObject("data");
                        Shared.Data.token = (String) data.get("token");

                        Log.i("Token", Shared.Data.token);
                        loginSuccess(true);


                    } catch (JSONException e) {
                        Log.i("JSONException ERROR", e.toString());

                    }
                    //runs when API called from RestQueue/MySingleton
                    Log.i("POST", response.toString());

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.println(Log.ERROR, "ERROR:", "Volley Error");
                            loginSuccess(false);


                        }
                    });


//
//        //Makes API Call
            MySingleton.getInstance(this).addToRequestQueue(postRequest);


        }
}


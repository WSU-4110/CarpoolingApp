package com.example.warriorsonwheels;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


//NOT USED, copied and pasted into Google Info onCreate
public class HTTPRequest extends Application{

    String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/rating/ab1234";

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

        }
    },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.println(Log.ERROR,"ERROR:","Volley Error");


            }
        });

        RequestQueue queue = MySingleton.getInstance(this).getRequestQueue();

    }

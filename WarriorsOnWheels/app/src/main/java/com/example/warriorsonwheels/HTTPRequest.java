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

    public HTTPRequest(){}

    public String apiCall(String URL_TAIL)
    {
        String url = BuildConfig.API_URL + URL_TAIL;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                final String value = response.toString();

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

        //won't provide right result
        return jsonObjectRequest.toString();
    }
}

package com.example.warriorsonwheels;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindPassengers extends AppCompatActivity implements View.OnClickListener {

    //Vars
    private Toolbar tbrMain;
    private TextView pass1, pass2, pass3, pass4, pass5, pass6;
    private Button refresh, start, cancel;

    String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride";
    Dialog dialog;
    ArrayList<String> passengers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassengers);

        //Toolbar
        tbrMain =  findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);


        //Vars
        pass1 = (TextView) findViewById(R.id.pass1);
        pass2 = (TextView) findViewById(R.id.pass2);
        pass3 = (TextView) findViewById(R.id.pass3);
        pass4 = (TextView) findViewById(R.id.pass4);
        pass5 = (TextView) findViewById(R.id.pass5);
        pass6 = (TextView) findViewById(R.id.pass6);

        refresh = (Button) findViewById(R.id.refresh);
        start = (Button) findViewById(R.id.start);
        cancel = (Button) findViewById(R.id.cancel);

//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String string) {
//                parseJsonData(string);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", Shared.Data.token);
//                return headers;
//            }
//
//        };
//        RequestQueue rQueue = Volley.newRequestQueue(FindPassengers.this);
//        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");

            for(int i = 0; i < ridesArray.length(); ++i) {
                JSONObject dataobj = ridesArray.getJSONObject(i);

                passengers.add(dataobj.getString("id"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.refresh:
                Intent intent1 = new Intent(getApplicationContext(), FindPassengers.class);
                startActivity(intent1);
                break;

            case R.id.start:
                Intent intent2 = new Intent(getApplicationContext(), RideStarted.class);
                startActivity(intent2);
                break;

            case R.id.cancel:
                cancel();
                break;

        }
    }

    private void cancel() {
        //TO DO
        // ADD ABILITY TO CANCEL RIDE
    }

    //Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflowmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Menu Options
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homePage:
                Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent1);
                return true;

            case R.id.userProfilePage:
                Intent intent2 = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent2);
                return true;

            case R.id.userLoginPage:
                Intent intent3 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
package com.example.warriorsonwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RideSearch extends AppCompatActivity implements View.OnClickListener{

    private Button confirmButton;
    private Toolbar tbrMain;
    private ListView rideList;
    ArrayList<String> locations;
    ArrayList<String> times;
    ArrayList<String> drivers;
    String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app/ride";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridesearch);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        //Buttons
        confirmButton = (Button) findViewById(R.id.confirmbutton);
        confirmButton.setClickable(false);

        rideList = (ListView) findViewById(R.id.rideList);


        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                confirmButton.setClickable(true);
                confirmButton.setOnClickListener(RideSearch.this);
            }
        });

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(RideSearch.this);
        rQueue.add(request);

    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");
            ArrayList al = new ArrayList();

            for(int i = 0; i < ridesArray.length(); ++i) {
                al.add(ridesArray.getString(i));
                JSONObject dataobj = ridesArray.getJSONObject(i);
                locations.add(dataobj.getString("loaction"));
                times.add(dataobj.getString("time"));
                drivers.add(dataobj.getString("name"));
            }

            CustomListAdapter whatever = new CustomListAdapter(this, locations, times, drivers);
            rideList.setAdapter(whatever);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
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

    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.confirmbutton:
                Intent intent3 = new Intent(getApplicationContext(), RateDriver.class);
                startActivity(intent3);
                break;
        }
    }


}
package com.example.warriorsonwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RideSearch extends AppCompatActivity  {

    private Toolbar tbrMain;
    private ListView rideList;
    ArrayList<String> times= new ArrayList<String>();
    ArrayList<String> arrives = new ArrayList<String>();
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<Integer> rideId = new ArrayList<Integer>();
    ArrayList<Integer> driverId = new ArrayList<Integer>();

    ArrayList<String> drivers = new ArrayList<String>();
    String url1 = Shared.Data.url + "ride";
    String url2 = "";
    ProgressDialog dialog;
    TextView noRide;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ridesearch);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        noRide = findViewById(R.id.noride);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                noRide.setVisibility(View.INVISIBLE);
                times.clear();
                arrives.clear();
                dates.clear();
                rideId.clear();
                driverId.clear();
                drivers.clear();
                getRides();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });
        rideList = (ListView) findViewById(R.id.rideList);
        rideList.setSelector(R.drawable.list_item_selector);

        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Shared.Data.selectedDriverId = driverId.get(position);
                Shared.Data.selectedRideId = rideId.get(position);
                Shared.Data.AccessIdDriver = drivers.get(position);
                view.getFocusables(position);
                view.setSelected(true);
                Intent intent3 = new Intent(getApplicationContext(), RideConfirm.class);
                startActivity(intent3);
            }
        });

        getRides();
    }

    public void getRides()
    {
        StringRequest request = new StringRequest(url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData1(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast toast = Toast.makeText(getApplicationContext(), volleyError.toString(),Toast.LENGTH_LONG);
                toast.show();
                dialog.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }

        };
        RequestQueue rQueue = Volley.newRequestQueue(RideSearch.this);
        rQueue.add(request);
    }


    void parseJsonData1(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");

            for(int i = 0; i < ridesArray.length(); ++i) {
                JSONObject dataobj = ridesArray.getJSONObject(i);

                if(dataobj.getBoolean("pending"))
                {
                    String dateTime = dataobj.getString("date");
                    arrives.add(dataobj.getString("arrival_location"));
                    dates.add(dateTime.substring(0,dateTime.lastIndexOf('T')));
                    times.add(dateTime.substring(dateTime.lastIndexOf('T') + 1 , dateTime.length() - 8));
                    rideId.add(dataobj.getInt("id"));
                    driverId.add(dataobj.getInt("driverId"));
                    drivers.add(dataobj.getString("access_id"));
                }

            }

            CustomListAdapter whatever = new CustomListAdapter(this, times, arrives, dates);
            rideList.setAdapter(whatever);
            if(times.size() == 0)
            {
                noRide.setVisibility(View.VISIBLE);
            }
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
                Shared.Data.token = null;
                Intent intent3 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
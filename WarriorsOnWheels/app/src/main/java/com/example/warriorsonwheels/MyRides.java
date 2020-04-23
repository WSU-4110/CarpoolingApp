package com.example.warriorsonwheels;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
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

public class MyRides extends AppCompatActivity implements View.OnClickListener{

    private Toolbar tbrMain;
    private ListView rideList;
    ArrayList<String> times= new ArrayList<String>();
    ArrayList<String> arrives = new ArrayList<String>();
    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<Integer> rideId = new ArrayList<Integer>();

    String url1 = Shared.Data.url + "ride";
    String url2 = "";
    ProgressDialog dialog;
    TextView noRide;
    SwipeRefreshLayout mySwipeRefreshLayout;
    Button postRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrides);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        noRide = findViewById(R.id.nomyride);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh3);
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
                getRides();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });
        postRide = findViewById(R.id.postRide);

        rideList = (ListView) findViewById(R.id.myRideList);
        rideList.setSelector(R.drawable.list_item_selector);

        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Shared.Data.mySelectedRideId = rideId.get(position);
                view.getFocusables(position);
                view.setSelected(true);
                confirm(view);

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
        RequestQueue rQueue = Volley.newRequestQueue(MyRides.this);
        rQueue.add(request);
    }

    void parseJsonData1(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray ridesArray = object.getJSONArray("data");

            for(int i = 0; i < ridesArray.length(); ++i) {
                JSONObject dataobj = ridesArray.getJSONObject(i);

                if(dataobj.getInt("driverId") == Shared.Data.currentDriver) {
                String dateTime = dataobj.getString("date");
                arrives.add(dataobj.getString("arrival_location"));
                dates.add(dateTime.substring(0,dateTime.lastIndexOf('T')));
                times.add(dateTime.substring(dateTime.lastIndexOf('T') + 1 , dateTime.length() - 8));
                rideId.add(dataobj.getInt("id"));
                }

            }

            MyRideListAdapter whatever = new MyRideListAdapter(this, times, arrives, dates);
            rideList.setAdapter(whatever);
            if(times.size() == 0)
            {
                noRide.setVisibility(View.VISIBLE);
                postRide.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    public void onClick (View v) {
        switch(v.getId())
        {
            case R.id.postRide:
                Intent intent3 = new Intent(getApplicationContext(), PostRide.class);
                startActivity(intent3);
                break;
        }
    }


    //Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflowmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void  confirm(View v) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Confirm this ride?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(
                            DialogInterface dialog, int option)
                    {

                        Intent intent3 = new Intent(getApplicationContext(), FindPassengers.class);
                        startActivity(intent3);
                    }
                });
        builder.setNegativeButton("No", null);
        builder.show();
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
package com.example.warriorsonwheels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RatePassenger extends AppCompatActivity{

    private Button rateBtn;
    private RatingBar RatePassenger;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratepassenger);

        //Buttons
        RatePassenger = findViewById(R.id.passengerRatingBar);
        rateBtn = findViewById(R.id.rateBtn);
        imageView = findViewById(R.id.imageView);

        Glide.with(this).load(Shared.Data.imgURL).into(imageView);

        RatePassenger.setNumStars(5);

        //Initialize DriverImage

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(v.getContext());
                builder.setTitle("RIDE OVER");
                builder.setMessage("Thank you for using Warriors on Wheels!");
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(
                                    DialogInterface dialog, int option)
                            {
                                Intent intent4 = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intent4);
                            }
                        });

                builder.show();
            }
        });
    }

    /*public void onClick(View v) {
        switch(v.getId())
        {
        }

    }*/

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

    public void postRequest()
    {
        //accessId SHOULD BE PASSENGERS
        String url = Shared.Data.url + "rating/" + Shared.Data.AccessIdPass;

        Map<String, String> jsonParams = new HashMap<String, String>();




        jsonParams.put("rating",String.valueOf(RatePassenger.getProgress()/2));
        jsonParams.put("is_driver","false");


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
                        Toast toast = Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG);
                        toast.show();


                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Shared.Data.token);
                return headers;
            }

        };
//
//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);

    }

}

package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

        signOut.setOnClickListener(this);
        passProf.setOnClickListener(this);


        Name.setText(((sharedVars) this.getApplication()).name);
        Email.setText(((sharedVars) this.getApplication()).email);
        Glide.with(this).load(((sharedVars) this.getApplication()).imgURL).into(profilePic);

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

    private StringRequest searchNameStringRequest(String nameSearch) {
        final String API = "&api_key=<<YOUR_API_KEY_HERE>>";
//        final String NAME_SEARCH = "&q=";
//        final String DATA_SOURCE = "&ds=Standard Reference";
//        final String FOOD_GROUP = "&fg=";
//        final String SORT = "&sort=r";
//        final String MAX_ROWS = "&max=25";
//        final String BEGINNING_ROW = "&offset=0";
        final String URL_PREFIX = "https://carpool-api-r64g2xh4xa-uc.a.run.app/rating/ab1234";

 //       String url = URL_PREFIX + API + NAME_SEARCH + nameSearch + DATA_SOURCE + FOOD_GROUP + SORT + MAX_ROWS + BEGINNING_ROW;
        String url = URL_PREFIX;

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result = new JSONObject(response).getJSONObject("");
                            int maxItems = result.getInt("user_id");
                            JSONArray resultList = result.getJSONArray("");

                            Name.setText(result.toString());






                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(GoogleInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(GoogleInfo.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                });
    }


}
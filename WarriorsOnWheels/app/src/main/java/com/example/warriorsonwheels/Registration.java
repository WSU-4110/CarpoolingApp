package com.example.warriorsonwheels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Registration extends AppCompatActivity {

    private LinearLayout NameLayout;
    private LinearLayout AccessLayout;
    private LinearLayout NumberLayout;
    private LinearLayout LocationLayout;
    //private Button CreateDriveProf;
    private Button finishPassProf;
    // private TextView Name,accessId, phoneNumber, location;
    private EditText nameInp, idInput, numberInput, pw, confirmPW, address,city,state,zip;
    private Toolbar tbrMain;
    private ArrayList<EditText> inputs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengerprofile);



        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Buttons
        finishPassProf = (Button) findViewById(R.id.finishPassProf);
        //CreateDriveProf = (Button) findViewById(R.id.createDrivProf);

        //EditText
        nameInp = (EditText) findViewById(R.id.Name);
        idInput = (EditText) findViewById(R.id.accessID);
        numberInput = (EditText) findViewById(R.id.PhoneNumber);

        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zip = (EditText) findViewById(R.id.zip);
        pw = (EditText)findViewById(R.id.pw);
        confirmPW = (EditText)findViewById(R.id.confirmpw);



        finishPassProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fillCheck();


            }
        });

    }

    public void fillCheck()
    {
        boolean allFilled = true;

        if(nameInp.getText().toString().trim().length() == 0)
        {
            nameInp.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(idInput.getText().toString().trim().length() < 6)
        {
            idInput.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(numberInput.getText().toString().trim().length() < 10)
        {
            numberInput.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(address.getText().toString().trim().length() == 0)
        {
            address.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(city.getText().toString().trim().length() == 0)
        {
            city.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(state.getText().toString().trim().length() < 2)
        {
            state.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(zip.getText().toString().trim().length() < 5)
        {
            zip.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(pw.getText().toString().trim().length() == 0)
        {
            pw.setBackgroundResource(R.color.error);
            allFilled = false;

        }

        if(allFilled)
        {
            checkPW();
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "All fields must be filled in properly",Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void checkPW()
    {
        if(String.valueOf(pw.getText()).equals(String.valueOf(confirmPW.getText())))
        {
            createUser();
        }
        else
        {

            Toast toast = Toast.makeText(getApplicationContext(), "Password did not match",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void createUser()
    {
        String url = Shared.Data.url + "user";

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("name",nameInp.getText().toString());
        jsonParams.put("access_id",idInput.getText().toString());
        jsonParams.put("password",pw.getText().toString());
        jsonParams.put("phone_number",numberInput.getText().toString());
        jsonParams.put("street",address.getText().toString());
        jsonParams.put("city",city.getText().toString());
        jsonParams.put("state",state.getText().toString());
        jsonParams.put("zip",zip.getText().toString());


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //runs when API called from RestQueue/MySingleton
                Log.i("POST",response.toString());
                successfulReg(true);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR,"ERROR:","Volley Error");
                        successfulReg(false);

                    }
                });

//        //Makes API Call
        MySingleton.getInstance(this).addToRequestQueue(postRequest);


    }

    public void successfulReg (boolean success)
    {
        if(success)
        {

            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "API ERROR",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
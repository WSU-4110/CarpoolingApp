package com.example.warriorsonwheels;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverProfile extends AppCompatActivity implements View.OnClickListener{

    private Button finishDriverProf;
    private EditText location, time, make, model, year, color, license_plate, accessId;
    private ImageButton carImage;
    private boolean isDriver = false;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverprofile);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        setSupportActionBar(tbrMain);

        //Buttons
        finishDriverProf = (Button) findViewById(R.id.finishDriver);

        //EditText
//        location = (EditText) findViewById(R.id.Loc);
//        accessId = (EditText) findViewById(R.id.accessIDask);
        make = (EditText) findViewById(R.id.make);
        model = (EditText) findViewById(R.id.model);
        year = (EditText) findViewById(R.id.year);
        color = (EditText) findViewById(R.id.color);
        license_plate = (EditText)findViewById(R.id.license);
        carImage = (ImageButton) findViewById(R.id.carImage);
        carImage.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //sends sign in info to userprofile.java
        String sendMake = make.getText().toString();
        Shared.Data.userName = sendMake;

        String sendModel = model.getText().toString();
        Shared.Data.userId = sendModel;

        String sendYear = year.getText().toString();
        Shared.Data.phNumber = sendYear;

        String sendColor = color.getText().toString();
        Shared.Data.userLoc = sendColor;

        String sendPlateNum = license_plate.getText().toString();
        Shared.Data.userLoc = sendPlateNum;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.carImage:
                selectImage(DriverProfile.this);
                break;
            case R.id.finishDriver:

                sendRequest();
                Shared.Data.isDriverCheck = true;
                Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent1);
                break;
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        carImage.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                carImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    public void sendRequest()
    {
        String url = Shared.Data.url + "driver";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("access_id",accessId.getText().toString());
       jsonParams.put("car",year.getText().toString() + " " + make.getText().toString() + " " + model.getText().toString());

       Shared.Data.driverAccessID = accessId.getText().toString();

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
                    }
                }) {

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
package com.example.warriorsonwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import org.w3c.dom.Text;

public class GoogleInfo extends AppCompatActivity implements View.OnClickListener  {

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

        Name = findViewById(R.id.nameDisplay);
        Email = findViewById(R.id.emailDisplay);
        profilePic = findViewById(R.id.prof_pic);
        signOut = findViewById(R.id.bn_signOut);
        passProf = findViewById(R.id.passProf);

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


}
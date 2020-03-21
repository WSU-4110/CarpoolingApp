package com.example.warriorsonwheels;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout profileSection;
    private Button signOut;
    private Button passProf;
    private SignInButton signIn;
    private TextView Name,Email;
    private ImageView profilePic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private Toolbar tbrMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        tbrMain = findViewById(R.id.tbrMain);
        //setSupportActionBar(tbrMain);

        //intialize vars
        //profileSection = (LinearLayout)findViewById(R.id.prof_section);
       // signOut = (Button)findViewById(R.id.bn_signOut);
        signIn = findViewById(R.id.sign_in_button);
       // passProf = (Button)findViewById(R.id.passProf);
       // Name = (TextView)findViewById(R.id.nameDisplay);
       // Email = (TextView)findViewById(R.id.emailDisplay);
        //profilePic = (ImageView)findViewById(R.id.prof_pic);

        //set onClick Listener
        signIn.setOnClickListener(this);
       // signOut.setOnClickListener(this);
        //passProf.setOnClickListener(this);
        //profileSection.setVisibility(View.GONE);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.sign_in_button:
                signIn();
                break;
//            case R.id.bn_signOut:
//                signOut();
//                break;
//            case R.id.passProf:
//                Intent intent1 = new Intent(getApplicationContext(), PassengerProfile.class);
//                startActivity(intent1);
//                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    private void signIn()
    {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);



    }

//    private void signOut()
//    {
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                //updateUI(false);
//            }
//        });
//    }

    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            //GoogleSignInAccount account = result.getSignInAccount();
//            String name = account.getDisplayName();
//            String email = account.getEmail();
//            String imgURL = account.getPhotoUrl().toString();
//            Name.setText(name);
//            Email.setText(email);
//            Glide.with(this).load(imgURL).into(profilePic);
//            updateUI(true);

            ((sharedVars)this.getApplication()).setGoogleAccount(result.getSignInAccount());

            Intent showGoogleInfo = new Intent(getApplicationContext(),GoogleInfo.class);
            startActivity(showGoogleInfo);

        }
        else
        {
           // updateUI(false);
        }
    }

//    private void updateUI(boolean isLogin)
//    {
//        if(isLogin)
//        {
////            Intent showGoogleInfo = new Intent(getApplicationContext(),GoogleInfo.class);
////            startActivity(showGoogleInfo);
//
////            profileSection.setVisibility(View.VISIBLE);
////            signIn.setVisibility(View.GONE);
//
//
//        }
//        else
//        {
//            profileSection.setVisibility(View.GONE);
//            signIn.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
package com.example.warriorsonwheels;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class sharedVars extends Application {

    GoogleSignInAccount googleAccount;

    String name, email, imgURL;


    public void setGoogleAccount(GoogleSignInAccount account)
    {
        googleAccount = account;

        name = account.getDisplayName();
        email = account.getEmail();
        imgURL = account.getPhotoUrl().toString();



    }

    public GoogleSignInAccount getGoogleAccount()
    {
        return googleAccount;
    }
}

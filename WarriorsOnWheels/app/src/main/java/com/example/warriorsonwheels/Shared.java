package com.example.warriorsonwheels;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public enum Shared {
    //initiate enum shared variables
    Data;
    public int selectedRide = 0;
    public boolean isDriver = false;
    public int profPic;
    public String userName, userId, userLoc, phNumber,driverAccessID;

    GoogleSignInAccount googleAccount;

    String name, email, imgURL;

    //Singleton used for Google Account to be used throughout the App
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


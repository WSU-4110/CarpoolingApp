package com.example.warriorsonwheels;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public enum Shared {
    //initiate enum shared variables
    Data;

    public int selectedRideId = 0;
    public int selectedDriverId = 0;

    public String rideId;
    public String userRideId;

    public boolean isDriverCheck = false;
    public int profPic;

    public String userName, userId, userLoc, phNumber,driverAccessID, token;
    public String userCarMake, userCarModel, userCarYear, userCarColor, userLicPlate;

    public String city;


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


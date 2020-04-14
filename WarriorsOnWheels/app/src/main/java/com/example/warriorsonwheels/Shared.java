package com.example.warriorsonwheels;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static android.provider.Settings.System.getString;

public enum Shared {
    //initiate enum shared variables
    Data;

    public int selectedRideId = 0;
    public int selectedDriverId = 0;
    public String AccessIdDriver = "";
    public String rideId;
    public String userRideId;

    public boolean isDriverCheck = false;
    public int profPic;

    public String userName, userId, userLoc, phNumber,driverAccessID, token, loggedInuser;
    public String userCarMake, userCarModel, userCarYear, userCarColor, userLicPlate;

    public String arrival, departure;

    public String firebaseToken;

    GoogleSignInAccount googleAccount;
    String name, email, imgURL;

    String url = "https://carpool-api-r64g2xh4xa-uc.a.run.app";


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


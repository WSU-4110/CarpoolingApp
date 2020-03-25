/*
 * //////////////////////////////////////////////////////////////////////
 *
 *           Assignment 5: DESIGN PATTERNS
 *       Nidhi Nadig     gh6210      3/25/2020
 *   Functional Requirement: Shared class sends Google Information to be displayed
 *       in the app
 *
 *   Below is the Shared.java file from our android application, modified
 *   to fit the Singleton design pattern.
 *
 * //////////////////////////////////////////////////////////////////////
 */

package com.example.warriorsonwheels;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public enum Shared {

    //initiate enum shared variables
    Data;
    public int selectedRide = 0;
    public boolean isDriver = false;
    public int profPic = 0;
    public String userName, userId, userLoc, phNumber,driverAccessID;
    GoogleSignInAccount googleAccount;
    String name, email, imgURL;

    public void setGoogleAccount(GoogleSignInAccount account)
    {
        private  googleAccount instance = null;

        private setGoogleAccount() {

        accountInfo = googleAccount(name, email, imgURL)
    }
    }

    public GoogleSignInAccount getGoogleAccount()
    {
        if (instance = null) {

            instance = new setGoogleAccount();
        }

        return googleAccount;
    }
}

    Shared accountInfo = Shared.getGoogleAccount();


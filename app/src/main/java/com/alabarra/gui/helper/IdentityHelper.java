package com.alabarra.gui.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.alabarra.SignInHandler;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobilehelper.auth.IdentityManager;
import com.amazonaws.mobilehelper.auth.user.IdentityProfile;

/**
 * Created by rodrigoarias on 7/10/17.
 */

public class IdentityHelper {

    private static IdentityHelper mInstance;

    private IdentityManager mIdentityManager;
    private IdentityProfile mIdentityProfile;


    private IdentityHelper() {
        mIdentityManager = AWSMobileClient.defaultMobileClient().getIdentityManager();
        mIdentityProfile = mIdentityManager.getIdentityProfile();
    }

    public static IdentityHelper getInstance() {
        if (mInstance == null) {
            mInstance = new IdentityHelper();
        }
        return mInstance;
    }

    public AWSCredentialsProvider getProvider() {
        return mIdentityManager.getCredentialsProvider();
    }

    public String getName() {
        return mIdentityProfile.getUserName();
    }

    public String getEmail() {
        return "";
    }

    public String getUsername() {
        return "";
    }

    public Bitmap getUserImage() {
        return mIdentityProfile.getUserImage();
    }

    public void signOut(@Nullable Activity parentActivity) {
        mIdentityManager.signOut();
        if (parentActivity != null) {
            mIdentityManager.signInOrSignUp(parentActivity, new SignInHandler());
            parentActivity.finish();
        }
    }

    public String getFirstName() {
        return getName().trim().split(" ")[0];
    }
}

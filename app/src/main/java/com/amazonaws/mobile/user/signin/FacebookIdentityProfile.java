package com.amazonaws.mobile.user.signin;
//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.18
//

import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobilehelper.auth.IdentityProvider;
import com.amazonaws.mobilehelper.auth.exceptions.ProfileRetrievalException;
import com.amazonaws.mobilehelper.auth.user.AbstractIdentityProfile;
import com.amazonaws.mobilehelper.auth.user.IdentityProfile;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Identity Profile for Facebook Provider.
 */
public class FacebookIdentityProfile extends AbstractIdentityProfile {
    /**
     * Log tag.
     */
    private static final String LOG_TAG = FacebookIdentityProfile.class.getSimpleName();

    @Override
    public IdentityProfile loadProfileInfo(final IdentityProvider provider) throws ProfileRetrievalException {
        if (!provider.refreshUserSignInState()) {
            throw new ProfileRetrievalException(
                    "Can't load user info, due to no longer signed in with " + provider.getDisplayName());
        }

        final Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,id,picture.type(large)");
        final GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "me");
        graphRequest.setParameters(parameters);
        final GraphResponse response = graphRequest.executeAndWait();
        final JSONObject json = response.getJSONObject();
        try {
            userName = json.getString("name");
            userImageUrl = json.getJSONObject("picture")
                    .getJSONObject("data")
                    .getString("url");
            userId = json.getString("id");
            userEmail = json.optString("email");

        } catch (final JSONException jsonException) {
            Log.e(LOG_TAG,
                    "Unable to getEmail Facebook user info. " + jsonException.getMessage() + "\n" + response, jsonException);
            throw new ProfileRetrievalException(
                    "Failed loading Facebook user info from Facebook API.", jsonException);
        }
        return this;
    }
}

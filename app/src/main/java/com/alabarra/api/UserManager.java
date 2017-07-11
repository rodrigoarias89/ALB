package com.alabarra.api;

import android.support.annotation.Nullable;

import ar.com.alabarra.clientsdk.model.SignUpOutputModel;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class UserManager {

    private static UserManager mInstance;

    private String mUserId;

    public static UserManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserManager();
        }
        return mInstance;
    }

    public void signUpOrSignInAsync(String email, String name, String username, @Nullable final BackgroundTaskListener<Void> listener) {
        ApiHelper.getInstance().signUpOrIn(email, name, username, new ApiHelper.OnApiResponse<SignUpOutputModel>() {
            @Override
            public void onSuccess(SignUpOutputModel response) {
                mUserId = response.getUserId();
            }

            @Override
            public void onFailed(Exception exception) {
                if (listener != null) {
                    listener.onFailed(exception);
                }
            }
        });
    }

    public String getUserId() {
        return mUserId;
    }


}

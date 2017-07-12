package com.alabarra.api;

import android.support.annotation.Nullable;

import ar.com.alabarra.clientsdk.model.UserModel;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class UserManager {

    private static UserManager mInstance;

    public static UserManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserManager();
        }
        return mInstance;
    }

    public void signUpOrSignInAsync(String email, String name, String username, @Nullable final BackgroundTaskListener<Void> listener) {
        ApiHelper.getInstance().signUpOrIn(email, name, username, new ApiHelper.OnApiResponse<UserModel>() {
            @Override
            public void onSuccess(UserModel response) {
                if (listener != null) {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onFailed(Exception exception) {
                if (listener != null) {
                    listener.onFailed(exception);
                }
            }
        });
    }

}

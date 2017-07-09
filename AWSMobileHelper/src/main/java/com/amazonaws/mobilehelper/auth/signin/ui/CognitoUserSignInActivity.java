package com.amazonaws.mobilehelper.auth.signin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.mobilehelper.R;

/**
 * Created by rodrigoarias on 7/9/17.
 */

public class CognitoUserSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cognito_sign_in);
    }
}

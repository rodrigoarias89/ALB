package com.amazonaws.mobilehelper.auth.signin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amazonaws.mobilehelper.R;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.SignUpActivity;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.UserPoolSignInView;

/**
 * Created by rodrigoarias on 7/9/17.
 */

public class CognitoUserSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cognito_sign_in);

        UserPoolSignInView signInView = (UserPoolSignInView) findViewById(R.id.user_pool_sign_in_view_id);
        signInView.getSignUpTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CognitoUserSignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}

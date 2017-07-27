package com.amazonaws.mobilehelper.auth.signin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobilehelper.R;
import com.amazonaws.mobilehelper.auth.IdentityProviderType;
import com.amazonaws.mobilehelper.auth.signin.SignInManager;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.SignUpActivity;

/**
 * Created by rodrigoarias on 7/9/17.
 */

public class CognitoUserSignInActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private View signUpTextView;
    private View forgotPasswordTextView;

    private boolean isInitialized;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognito_sign_in);

        userNameEditText = (EditText) findViewById(R.id.sign_in_username);
        passwordEditText = (EditText) findViewById(R.id.sign_in_password);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        signUpTextView = findViewById(R.id.sign_in_go_to_sign_up);
        forgotPasswordTextView = findViewById(R.id.sign_in_forgot);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CognitoUserSignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        initializeIfNecessary();
    }

    private void initializeIfNecessary() {
        if (isInitialized) {
            return;
        }
        isInitialized = true;

        final SignInManager signInManager = SignInManager.getInstance();
        signInManager.initializeSignInButton(this, IdentityProviderType.COGNITO_USER_POOL,
                signInButton);
    }

    public View getForgotPasswordTextView() {
        return forgotPasswordTextView;
    }

    public String getEnteredUserName() {
        return userNameEditText.getText().toString();
    }

    public String getEnteredPassword() {
        return passwordEditText.getText().toString();
    }
}

package com.amazonaws.mobilehelper.auth.signin.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobilehelper.R;
import com.amazonaws.mobilehelper.auth.IdentityManager;
import com.amazonaws.mobilehelper.auth.IdentityProvider;
import com.amazonaws.mobilehelper.auth.IdentityProviderType;
import com.amazonaws.mobilehelper.auth.SignInResultHandler;
import com.amazonaws.mobilehelper.auth.signin.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobilehelper.auth.signin.SignInManager;
import com.amazonaws.mobilehelper.auth.signin.SignInProviderResultHandler;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.SignUpActivity;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.SignUpConfirmActivity;
import com.amazonaws.mobilehelper.util.ViewHelper;
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException;

import ar.com.alabarra.guicommons.gui.utils.CustomAlertDialogBuilder;

/**
 * Created by rodrigoarias on 7/9/17.
 */

public class CognitoUserSignInActivity extends AppCompatActivity implements SignInProviderResultHandler {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private View signUpTextView;
    private View forgotPasswordTextView;
    private Dialog mProgressDialog;

    private SignInManager signInManager;

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

        signInManager = SignInManager.getInstance();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                signInManager.initSignIn(IdentityProviderType.COGNITO_USER_POOL, getEnteredUserName(), getEnteredPassword(), CognitoUserSignInActivity.this);
            }
        });
        signInManager.initializeForgotPasswordButton(IdentityProviderType.COGNITO_USER_POOL, this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignInManager.getInstance().handleActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onSuccess(final IdentityProvider provider) {
        // The sign-in manager is no longer needed once signed in.
        SignInManager.dispose();

        final IdentityManager identityManager = signInManager.getIdentityManager();
        final SignInResultHandler signInResultsHandler = signInManager.getResultHandler();

        // Load user name and image.
        identityManager.loadUserIdentityProfile(provider, new Runnable() {
            @Override
            public void run() {
                stopProgressDialog();
                signInResultsHandler.onSuccessInitApp(CognitoUserSignInActivity.this, provider);
                finish();
            }
        });
    }

    @Override
    public void onCancel(IdentityProvider provider) {
        stopProgressDialog();
    }

    @Override
    public void onError(IdentityProvider provider, Exception exception) {
        stopProgressDialog();

        if (exception instanceof UserNotConfirmedException) {
            startVerificationActivity();
            return;
        }

        //TODO por ahi pueden ser otro tipo de errores, no solo usuario y contraseña.
        ViewHelper.showDialog(this, null, this.getString(R.string.login_failed));
    }

    private void startVerificationActivity() {
        final Intent intent = new Intent(this, SignUpConfirmActivity.class);
        intent.putExtra(CognitoUserPoolsSignInProvider.AttributeKeys.USERNAME, getEnteredUserName());
        this.startActivityForResult(intent, CognitoUserPoolsSignInProvider.VERIFICATION_REQUEST_CODE);
    }

    private void showProgressDialog() {
        mProgressDialog = new CustomAlertDialogBuilder(this).createProgressDialog();
        mProgressDialog.show();
    }

    private void stopProgressDialog() {
        if (mProgressDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.cancel();
                    mProgressDialog = null;
                }
            });
        }
    }
}

package com.amazonaws.mobilehelper.auth.signin.ui.userpools;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobilehelper.R;
import com.amazonaws.mobilehelper.auth.signin.SignInManager;
import com.amazonaws.services.cognitoidentityprovider.model.InvalidParameterException;
import com.amazonaws.services.cognitoidentityprovider.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidentityprovider.model.UsernameExistsException;

import ar.com.alabarra.guicommons.gui.utils.CustomAlertDialogBuilder;


public class SignUpActivity extends Activity {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText givenNameEditText;
    private EditText emailEditText;

    private Dialog mProgressDialog;

    private SignUpHandler handler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            stopProgressDialog();
            if (signUpConfirmationState) {
                showSuccess(R.string.user_created);
            } else {
                showSuccess(R.string.user_created_confirm);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            stopProgressDialog();

            if (exception instanceof UsernameExistsException) {
                showError(R.string.error_repeated_username);
                return;
            }

            if (exception instanceof InvalidPasswordException) {
                showError(R.string.error_invalid_password);
                return;
            }

            if (exception instanceof InvalidParameterException) {
                showError(R.string.error_invalid_param);
                return;
            }
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userNameEditText = (EditText) findViewById(R.id.sign_up_username);
        passwordEditText = (EditText) findViewById(R.id.sign_up_password);
        givenNameEditText = (EditText) findViewById(R.id.sign_up_name);
        emailEditText = (EditText) findViewById(R.id.sign_up_email);
    }

    /**
     * Retrieve input and return to caller.
     *
     * @param view the Android View
     */
    public void signUp(final View view) {

        final String username = userNameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String givenName = givenNameEditText.getText().toString();
        final String email = emailEditText.getText().toString();

        if (checkValidFields()) {
            showProgressDialog();
            SignInManager.getInstance().performCognitoSignUp(username, password, givenName, email, handler);
        }
    }

    private boolean checkValidFields() {
        //Check all fields completed
        if (userNameEditText.getText().length() == 0) {
            userNameEditText.setError(getString(R.string.incomplete_field));
            userNameEditText.requestFocus();
            return false;
        }

        if (givenNameEditText.getText().length() == 0) {
            givenNameEditText.setError(getString(R.string.incomplete_field));
            givenNameEditText.requestFocus();
            return false;
        }

        if (emailEditText.getText().length() == 0) {
            emailEditText.setError(getString(R.string.incomplete_field));
            emailEditText.requestFocus();
            return false;
        }

        if (passwordEditText.getText().length() < 6) {
            passwordEditText.setError(getString(R.string.invalid_password));
            passwordEditText.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()) {
            emailEditText.setError(getString(R.string.invalid_email));
            emailEditText.requestFocus();
            return false;
        }
        return true;
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

    private void showSuccess(final int stringId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CustomAlertDialogBuilder(SignUpActivity.this)
                        .setMessage(stringId)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SignUpActivity.this.finish();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void showError(final int errorStringId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CustomAlertDialogBuilder(SignUpActivity.this)
                        .setMessage(errorStringId)
                        .setPositiveButton(R.string.ok, null)
                        .create()
                        .show();
            }
        });
    }

}

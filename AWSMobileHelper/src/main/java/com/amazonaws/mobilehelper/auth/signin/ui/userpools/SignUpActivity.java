package com.amazonaws.mobilehelper.auth.signin.ui.userpools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobilehelper.R;
import com.amazonaws.mobilehelper.auth.signin.SignInManager;


public class SignUpActivity extends Activity {

    private SignUpHandler handler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            if (signUpConfirmationState) {
                //TODO sign up confirmed
            } else {
                //TODO sign up has to verificate
            }
        }

        @Override
        public void onFailure(Exception exception) {
            //TODO sign up failed
        }
    };


    private SignUpView signUpView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpView = (SignUpView) findViewById(R.id.signup_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Retrieve input and return to caller.
     *
     * @param view the Android View
     */
    public void signUp(final View view) {

        final String username = signUpView.getUserName();
        final String password = signUpView.getPassword();
        final String givenName = signUpView.getGivenName();
        final String email = signUpView.getEmail();

        SignInManager.getInstance().performCognitoSignUp(username, password, givenName, email, handler);
        finish();
    }
}

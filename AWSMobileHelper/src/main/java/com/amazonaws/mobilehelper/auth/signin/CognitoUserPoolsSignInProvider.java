package com.amazonaws.mobilehelper.auth.signin;
//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.18
//

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.amazonaws.mobilehelper.R;
import com.amazonaws.mobilehelper.auth.IdentityProviderType;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.ForgotPasswordActivity;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.MFAActivity;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.SignUpConfirmActivity;
import com.amazonaws.mobilehelper.auth.signin.ui.userpools.UserPoolSignInView;
import com.amazonaws.mobilehelper.auth.user.IdentityProfile;
import com.amazonaws.mobilehelper.config.AWSMobileHelperConfiguration;
import com.amazonaws.mobilehelper.util.ViewHelper;
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages sign-in using Cognito User Pools.
 */
public class CognitoUserPoolsSignInProvider implements SignInProvider {
    /**
     * Cognito User Pools attributes.
     */
    public final class AttributeKeys {


        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String VERIFICATION_CODE = "verification_code";
        public static final String GIVEN_NAME = "given_name";
        public static final String EMAIL_ADDRESS = "email";

    }

    private static final String LOG_TAG = CognitoUserPoolsSignInProvider.class.getSimpleName();

    /**
     * Start of Intent request codes owned by the Cognito User Pools app.
     */
    private static final int REQUEST_CODE_START = 0x2970;

    /**
     * Request code for password reset Intent.
     */
    private static final int FORGOT_PASSWORD_REQUEST_CODE = REQUEST_CODE_START + 42;

    /**
     * Request code for account registration Intent.
     */
    private static final int SIGN_UP_REQUEST_CODE = REQUEST_CODE_START + 43;

    /**
     * Request code for MFA Intent.
     */
    private static final int MFA_REQUEST_CODE = REQUEST_CODE_START + 44;

    /**
     * Request code for account verification Intent.
     */
    private static final int VERIFICATION_REQUEST_CODE = REQUEST_CODE_START + 45;

    /**
     * Request codes that the Cognito User Pools can handle.
     */
    private static final Set<Integer> REQUEST_CODES = new HashSet<Integer>() {{
        add(FORGOT_PASSWORD_REQUEST_CODE);
        add(SIGN_UP_REQUEST_CODE);
        add(MFA_REQUEST_CODE);
        add(VERIFICATION_REQUEST_CODE);
    }};

    /**
     * The sign-in results adapter from the SignInManager.
     */
    private SignInProviderResultHandler resultsHandler;

    /**
     * Forgot Password processing provided by the Cognito User Pools SDK.
     */
    private ForgotPasswordContinuation forgotPasswordContinuation;

    /**
     * MFA processing provided by the Cognito User Pools SDK.
     */
    private MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation;

    /**
     * Android context.
     */
    private Context context;

    /**
     * Invoking Android Activity.
     */
    private Activity activity;

    /**
     * Sign-in username.
     */
    private String username;

    /**
     * Sign-in password.
     */
    private String password;

    /**
     * Sign-in verification code.
     */
    private String verificationCode;

    private String cognitoLoginKey;

    /**
     * Active Cognito User Pool.
     */
    private CognitoUserPool cognitoUserPool;

    /**
     * Active Cognito User Pools session.
     */
    private CognitoUserSession cognitoUserSession;

    /**
     * Handle callbacks from the Forgot Password flow.
     */
    private ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            Log.d(LOG_TAG, "Password change succeeded.");
            ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_forgot_password),
                    activity.getString(R.string.password_change_success));
        }

        @Override
        public void getResetCode(final ForgotPasswordContinuation continuation) {
            forgotPasswordContinuation = continuation;

            final Intent intent = new Intent(context, ForgotPasswordActivity.class);
            activity.startActivityForResult(intent, FORGOT_PASSWORD_REQUEST_CODE);
        }

        @Override
        public void onFailure(final Exception exception) {
            Log.e(LOG_TAG, "Password change failed.", exception);
            ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_forgot_password),
                    activity.getString(R.string.password_change_failed) + " " + exception);
        }
    };

    private void startVerificationActivity() {
        final Intent intent = new Intent(context, SignUpConfirmActivity.class);
        intent.putExtra(AttributeKeys.USERNAME, username);
        activity.startActivityForResult(intent, VERIFICATION_REQUEST_CODE);
    }


    /**
     * Handle callbacks from the Sign Up - Confirm Account flow.
     */
    private GenericHandler signUpConfirmationHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.i(LOG_TAG, "Confirmed.");
            ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_sign_up_confirm),
                    activity.getString(R.string.sign_up_confirm_success));
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e(LOG_TAG, "Failed to confirm user.", exception);
            ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_sign_up_confirm),
                    activity.getString(R.string.sign_up_confirm_failed) + " " + exception);
        }
    };

    private void resendConfirmationCode() {
        final CognitoUser cognitoUser = cognitoUserPool.getUser(username);
        cognitoUser.resendConfirmationCodeInBackground(new VerificationHandler() {
            @Override
            public void onSuccess(final CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
                startVerificationActivity();
            }

            @Override
            public void onFailure(final Exception exception) {
                if (null != resultsHandler) {
                    ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_sign_in),
                            activity.getString(R.string.login_failed)
                                    + "\nUser was not verified and resending confirmation code failed.\n"
                                    + exception);

                    resultsHandler.onError(CognitoUserPoolsSignInProvider.this, exception);
                }
            }
        });
    }

    /**
     * Handle callbacks from the Authentication flow. Includes MFA handling.
     */
    private AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(final CognitoUserSession userSession, final CognitoDevice newDevice) {
            Log.i(LOG_TAG, "Logged in. " + userSession.getIdToken());

            cognitoUserSession = userSession;

            if (null != resultsHandler) {
                ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_sign_in),
                        activity.getString(R.string.login_success) + " " + userSession.getIdToken());

                resultsHandler.onSuccess(CognitoUserPoolsSignInProvider.this);
            }
        }

        @Override
        public void getAuthenticationDetails(
                final AuthenticationContinuation authenticationContinuation, final String userId) {

            if (null != username && null != password) {
                final AuthenticationDetails authenticationDetails = new AuthenticationDetails(
                        username,
                        password,
                        null);

                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();
            }
        }

        @Override
        public void getMFACode(final MultiFactorAuthenticationContinuation continuation) {
            multiFactorAuthenticationContinuation = continuation;

            final Intent intent = new Intent(context, MFAActivity.class);
            activity.startActivityForResult(intent, MFA_REQUEST_CODE);
        }

        @Override
        public void authenticationChallenge(final ChallengeContinuation continuation) {
            throw new UnsupportedOperationException("Not supported in this sample.");
        }

        @Override
        public void onFailure(final Exception exception) {
            Log.e(LOG_TAG, "Failed to login.", exception);

            // UserNotConfirmedException will only happen once in the sign-in flow in the case
            // that the user attempting to sign in had not confirmed their account by entering
            // the correct verification code. A different exception is thrown if the code
            // is invalid, so this will not create an continuous confirmation loop if the
            // user enters the wrong code.
            if (exception instanceof UserNotConfirmedException) {
                resendConfirmationCode();
                return;
            }

            if (null != resultsHandler) {
                ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_sign_in),
                        activity.getString(R.string.login_failed) + " " + exception);

                resultsHandler.onError(CognitoUserPoolsSignInProvider.this, exception);
            }
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final Context context, final AWSMobileHelperConfiguration configuration) {
        this.context = context;

        this.cognitoUserPool = new CognitoUserPool(context,
                configuration.getCognitoUserPoolId(),
                configuration.getCognitoUserPoolClientId(),
                configuration.getCognitoUserPoolClientSecret(),
                configuration.getCognitoRegion());

        cognitoLoginKey = "cognito-idp." + configuration.getCognitoRegion().getName()
                + ".amazonaws.com/" + configuration.getCognitoUserPoolId();
        Log.d(LOG_TAG, "CognitoLoginKey: " + cognitoLoginKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRequestCodeOurs(final int requestCode) {
        return REQUEST_CODES.contains(requestCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleActivityResult(final int requestCode,
                                     final int resultCode,
                                     final Intent data) {

        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case FORGOT_PASSWORD_REQUEST_CODE:
                    password = data.getStringExtra(CognitoUserPoolsSignInProvider.AttributeKeys.PASSWORD);
                    verificationCode = data.getStringExtra(CognitoUserPoolsSignInProvider.AttributeKeys.VERIFICATION_CODE);

                    Log.d(LOG_TAG, "verificationCode = " + verificationCode);

                    forgotPasswordContinuation.setPassword(password);
                    forgotPasswordContinuation.setVerificationCode(verificationCode);
                    forgotPasswordContinuation.continueTask();
                    break;
                case MFA_REQUEST_CODE:
                    verificationCode = data.getStringExtra(CognitoUserPoolsSignInProvider.AttributeKeys.VERIFICATION_CODE);

                    Log.d(LOG_TAG, "verificationCode = " + verificationCode);

                    multiFactorAuthenticationContinuation.setMfaCode(verificationCode);
                    multiFactorAuthenticationContinuation.continueTask();

                    break;
                case VERIFICATION_REQUEST_CODE:
                    username = data.getStringExtra(CognitoUserPoolsSignInProvider.AttributeKeys.USERNAME);
                    verificationCode = data.getStringExtra(CognitoUserPoolsSignInProvider.AttributeKeys.VERIFICATION_CODE);

                    Log.d(LOG_TAG, "username = " + username);
                    Log.d(LOG_TAG, "verificationCode = " + verificationCode);

                    final CognitoUser cognitoUser = cognitoUserPool.getUser(username);

                    cognitoUser.confirmSignUpInBackground(verificationCode, true, signUpConfirmationHandler);

                    break;
            }
        }

    }

    public void signUp(String username, String password, String givenName, String email, SignUpHandler handler) {
        Log.d(LOG_TAG, "username = " + username);
        Log.d(LOG_TAG, "given_name = " + givenName);
        Log.d(LOG_TAG, "email = " + email);

        final CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute(CognitoUserPoolsSignInProvider.AttributeKeys.GIVEN_NAME, givenName);
        userAttributes.addAttribute(CognitoUserPoolsSignInProvider.AttributeKeys.EMAIL_ADDRESS, email);

        cognitoUserPool.signUpInBackground(username, password, userAttributes,
                null, handler);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View.OnClickListener initializeSignInButton(final Activity signInActivity,
                                                       final View buttonView,
                                                       final SignInProviderResultHandler resultsHandler) {
        this.activity = signInActivity;
        this.resultsHandler = resultsHandler;

        final UserPoolSignInView userPoolSignInView =
                (UserPoolSignInView) activity.findViewById(R.id.user_pool_sign_in_view_id);

        final TextView forgotPasswordTextView = userPoolSignInView.getForgotPasswordTextView();
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = userPoolSignInView.getEnteredUserName();
                if (username.length() < 1) {
                    Log.w(LOG_TAG, "Missing username.");
                    ViewHelper.showDialog(activity, activity.getString(R.string.title_activity_sign_in), "Missing username.");
                } else {
                    final CognitoUser cognitoUser = cognitoUserPool.getUser(username);

                    cognitoUser.forgotPasswordInBackground(forgotPasswordHandler);
                }
            }
        });

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = userPoolSignInView.getEnteredUserName();
                password = userPoolSignInView.getEnteredPassword();

                final CognitoUser cognitoUser = cognitoUserPool.getUser(username);

                cognitoUser.getSessionInBackground(authenticationHandler);
            }
        };

        buttonView.setOnClickListener(listener);
        return listener;
    }

    @Override
    public IdentityProviderType getProviderType() {
        return IdentityProviderType.COGNITO_USER_POOL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return "Amazon Cognito Your User Pools";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCognitoLoginKey() {
        return cognitoLoginKey;
    }

    /**
     * Authentication handler for handling token refresh.
     */
    private class RefreshSessionAuthenticationHandler implements AuthenticationHandler {
        private CognitoUserSession userSession = null;

        private CognitoUserSession getUserSession() {
            return userSession;
        }

        @Override
        public void onSuccess(final CognitoUserSession userSession, final CognitoDevice newDevice) {
            this.userSession = userSession;
        }

        @Override
        public void getAuthenticationDetails(final AuthenticationContinuation authenticationContinuation,
                                             final String UserId) {
            Log.d(LOG_TAG, "Can't refresh the session silently, due to authentication details needed.");
        }

        @Override
        public void getMFACode(final MultiFactorAuthenticationContinuation continuation) {
            Log.wtf(LOG_TAG, "Refresh flow can not trigger request for MFA code.");
        }

        @Override
        public void authenticationChallenge(final ChallengeContinuation continuation) {
            Log.wtf(LOG_TAG, "Refresh flow can not trigger request for authentication challenge.");
        }

        @Override
        public void onFailure(final Exception exception) {
            Log.e(LOG_TAG, "Can't refresh session.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean refreshUserSignInState() {
        if (null != cognitoUserSession && cognitoUserSession.isValid()) {
            return true;
        }

        final RefreshSessionAuthenticationHandler refreshSessionAuthenticationHandler
                = new RefreshSessionAuthenticationHandler();

        cognitoUserPool.getCurrentUser().getSession(refreshSessionAuthenticationHandler);
        if (null != refreshSessionAuthenticationHandler.getUserSession()) {
            cognitoUserSession = refreshSessionAuthenticationHandler.getUserSession();
            Log.i(LOG_TAG, "refreshUserSignInState: Signed in with Cognito.");
            return true;
        }

        Log.i(LOG_TAG, "refreshUserSignInState: Not signed in with Cognito.");
        cognitoUserSession = null;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToken() {
        return null == cognitoUserSession ? null : cognitoUserSession.getIdToken().getJWTToken();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String refreshToken() {
        // If there is a session, but the credentials are expired rendering the session not valid.
        if ((cognitoUserSession != null) && !cognitoUserSession.isValid()) {
            // Attempt to refresh the credentials.
            final RefreshSessionAuthenticationHandler refreshSessionAuthenticationHandler
                    = new RefreshSessionAuthenticationHandler();

            // Cognito User Pools SDK will attempt to refresh the token upon calling getSession().
            cognitoUserPool.getCurrentUser().getSession(refreshSessionAuthenticationHandler);

            if (null != refreshSessionAuthenticationHandler.getUserSession()) {
                cognitoUserSession = refreshSessionAuthenticationHandler.getUserSession();
            } else {
                Log.e(LOG_TAG, "Could not refresh the Cognito User Pool Token.");
            }
        }

        return getToken();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signOut() {
        if (null != cognitoUserPool && null != cognitoUserPool.getCurrentUser()) {
            cognitoUserPool.getCurrentUser().signOut();

            cognitoUserSession = null;
            username = null;
            password = null;
        }
    }

    @Override
    public Class<? extends IdentityProfile> getIdentityProfileClass() {
        return CognitoUserPoolsIdentityProfile.class;
    }

    /**
     * @return the Cognito User Pool.
     */
    public CognitoUserPool getCognitoUserPool() {
        return cognitoUserPool;
    }

}

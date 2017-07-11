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
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.util.SparseArray;
import android.view.View;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobilehelper.auth.IdentityManager;
import com.amazonaws.mobilehelper.auth.IdentityProvider;
import com.amazonaws.mobilehelper.auth.IdentityProviderType;
import com.amazonaws.mobilehelper.auth.SignInResultHandler;
import com.amazonaws.mobilehelper.util.ThreadUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The SignInManager is a singleton component, which orchestrates sign-in and sign-up flows. It is responsible for
 * discovering the previously signed-in provider and that provider's credentials, as well as initializing sign-in
 * buttons with the providers.
 */
public class SignInManager {
    private static final String LOG_TAG = SignInManager.class.getSimpleName();
    private final Map<IdentityProviderType, SignInProvider> signInProviders = new HashMap<>();
    private volatile static SignInManager singleton = null;
    private final IdentityManager identityManager;
    private volatile SignInResultHandler signInResultHandler;
    private final SparseArray<SignInPermissionsHandler> providersHandlingPermissions = new SparseArray<>();

    /**
     * Constructor.
     */
    private SignInManager(final Context context, final IdentityManager identityManager) {
        if (BuildConfig.DEBUG && singleton != null) {
            throw new AssertionError();
        }

        this.identityManager = identityManager;
        for (Class<? extends SignInProvider> providerClass : identityManager.getSignInProviderClasses()) {
            final SignInProvider provider;
            try {
                provider = providerClass.newInstance();
            } catch (final IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (final InstantiationException ex) {
                throw new RuntimeException(ex);
            }
            provider.initialize(context, identityManager.getHelperConfiguration());

            addSignInProvider(provider);
            if (provider instanceof SignInPermissionsHandler) {
                final SignInPermissionsHandler handler = (SignInPermissionsHandler) provider;
                providersHandlingPermissions.put(handler.getPermissionRequestCode(), handler);
            }
        }

        singleton = this;
    }

    /**
     * Gets the singleton instance of this class.
     *
     * @return instance
     */
    public synchronized static SignInManager getInstance() {
        return singleton;
    }

    /**
     * Gets the singleton instance of this class.
     *
     * @return instance
     */
    public synchronized static SignInManager getInstance(final Context context, @NonNull final IdentityManager identityManager) {
        if (singleton == null) {
            singleton = new SignInManager(context, identityManager);
        }
        return singleton;
    }

    public void setResultHandler(final SignInResultHandler signInResultHandler) {
        this.signInResultHandler = signInResultHandler;
    }

    public SignInResultHandler getResultHandler() {
        return signInResultHandler;
    }

    /**
     * @return the identityManager used by this SignInManager.
     */
    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    public synchronized static void dispose() {
        singleton = null;
    }

    /**
     * Adds a sign-in identity provider.
     *
     * @param signInProvider sign-in provider
     */
    public void addSignInProvider(final SignInProvider signInProvider) {
        final IdentityProviderType identityProviderType = signInProvider.getProviderType();
        signInProviders.put(identityProviderType, signInProvider);
        identityManager.getIdentityProfileManager()
                .registerIdentityProfileClass(identityProviderType,
                        signInProvider.getIdentityProfileClass());
    }

    /**
     * Call getPreviouslySignedInProvider to determine if the user was left signed-in when the app
     * was last running.  This should be called on a background thread since it may perform file
     * i/o.  If the user is signed in with a provider, this will return the provider for which the
     * user is signed in.  Subsequently, refreshCredentialsWithProvider should be called with the
     * provider returned from this method.
     *
     * @return false if not already signed in, true if the user was signed in with a provider.
     */
    public SignInProvider getPreviouslySignedInProvider() {

        for (final SignInProvider provider : signInProviders.values()) {
            // Note: This method may block. This loop could potentially be sped
            // up by running these calls in parallel using an executorService.
            if (provider.refreshUserSignInState()) {
                return provider;
            }
        }
        return null;
    }

    private class SignInProviderResultAdapter implements SignInProviderResultHandler {
        final private SignInProviderResultHandler handler;
        final private Activity activity;

        private SignInProviderResultAdapter(final Activity activity,
                                            final SignInProviderResultHandler handler) {
            this.handler = handler;
            this.activity = activity;
        }

        private Activity getActivity() {
            return activity;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final IdentityProvider provider) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.onSuccess(provider);
                }
            });
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onCancel(final IdentityProvider provider) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.onCancel(provider);
                }
            });
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onError(final IdentityProvider provider, final Exception ex) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.onError(provider, ex);
                }
            });
        }
    }

    private SignInProviderResultAdapter resultsAdapter;

    /**
     * Refresh Cognito credentials with a provider.  Results handlers are always called on the main
     * thread.
     *
     * @param activity       the calling activity.
     * @param provider       the sign-in provider that was previously signed in.
     * @param resultsHandler the handler to receive results for credential refresh.
     */
    public void refreshCredentialsWithProvider(final Activity activity,
                                               final IdentityProvider provider,
                                               final SignInProviderResultHandler resultsHandler) {

        if (provider == null) {
            throw new IllegalArgumentException("The sign-in provider cannot be null.");
        }

        if (provider.getToken() == null) {
            resultsHandler.onError(provider,
                    new IllegalArgumentException("Given provider not previously logged in."));
        }

        resultsAdapter = new SignInProviderResultAdapter(activity, resultsHandler);
        identityManager.setProviderResultsHandler(resultsAdapter);

        identityManager.federateWithProvider(provider);
    }

    /**
     * Sets the results handler results from sign-in with a provider. Results handlers are
     * always called on the UI thread.
     *
     * @param activity       the calling activity.
     * @param resultsHandler the handler for results from sign-in with a provider.
     */
    public void setProviderResultsHandler(final Activity activity,
                                          final SignInProviderResultHandler resultsHandler) {
        resultsAdapter = new SignInProviderResultAdapter(activity, resultsHandler);
        // Set the final results handler with the identity manager.
        identityManager.setProviderResultsHandler(resultsAdapter);
    }


    public View.OnClickListener initializeSignInButton(Activity activity, final IdentityProviderType providerType,
                                                       final View buttonView) {
        final SignInProvider provider = findProvider(providerType);

        // Initialize the sign in button with the identity manager's results adapter.
        return provider.initializeSignInButton(activity,
                buttonView,
                identityManager.getResultsAdapter());
    }

    public void performCognitoSignUp(String username, String password, String givenName, String email, SignUpHandler handler) {
        CognitoUserPoolsSignInProvider provider = (CognitoUserPoolsSignInProvider) findProvider(IdentityProviderType.COGNITO_USER_POOL);
        provider.signUp(username, password, givenName, email, handler);
    }


    private SignInProvider findProvider(IdentityProviderType providerType) {

        final SignInProvider provider = signInProviders.get(providerType);

        if (provider == null) {
            throw new IllegalArgumentException("No such provider : " + providerType.name());
        }

        return provider;
    }

    /**
     * Handle the Activity result for login providers.
     *
     * @param requestCode the request code.
     * @param resultCode  the result code.
     * @param data        result intent.
     * @return true if the sign-in manager handle the result, otherwise false.
     */
    public boolean handleActivityResult(final int requestCode, final int resultCode, final Intent data) {
        for (final SignInProvider provider : signInProviders.values()) {
            if (provider.isRequestCodeOurs(requestCode)) {
                provider.handleActivityResult(requestCode, resultCode, data);
                return true;
            }
        }

        return false;
    }

    /**
     * Handle the Activity request permissions result for sign-in providers.
     *
     * @param requestCode  the request code.
     * @param permissions  the permissions requested.
     * @param grantResults the grant results.
     */
    public void handleRequestPermissionsResult(final int requestCode, final String permissions[],
                                               final int[] grantResults) {
        final SignInPermissionsHandler handler = providersHandlingPermissions.get(requestCode);
        if (handler != null) {
            handler.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

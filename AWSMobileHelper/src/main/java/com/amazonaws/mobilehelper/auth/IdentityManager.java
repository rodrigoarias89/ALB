package com.amazonaws.mobilehelper.auth;
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

import com.amazonaws.ClientConfiguration;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSBasicCognitoIdentityProvider;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobilehelper.auth.signin.AuthException;
import com.amazonaws.mobilehelper.auth.signin.CognitoAuthException;
import com.amazonaws.mobilehelper.auth.signin.ProviderAuthException;
import com.amazonaws.mobilehelper.auth.signin.ui.FacebookSignInActivity;
import com.amazonaws.mobilehelper.auth.signin.SignInManager;
import com.amazonaws.mobilehelper.auth.signin.SignInProvider;
import com.amazonaws.mobilehelper.auth.signin.SignInProviderResultHandler;
import com.amazonaws.mobilehelper.auth.user.IdentityProfile;
import com.amazonaws.mobilehelper.auth.user.IdentityProfileManager;
import com.amazonaws.mobilehelper.auth.user.ProfileRetrievalException;
import com.amazonaws.mobilehelper.config.AWSMobileHelperConfiguration;

import com.amazonaws.mobilehelper.util.ThreadUtils;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.util.Map;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The identity manager keeps track of the current sign-in provider and is responsible
 * for caching credentials.
 */
public class IdentityManager {

    /** Holder for the credentials provider, allowing the underlying provider to be swapped when necessary. */
    private class AWSCredentialsProviderHolder implements AWSCredentialsProvider {
        private volatile CognitoCachingCredentialsProvider underlyingProvider;

        @Override
        public AWSCredentials getCredentials() {
            return underlyingProvider.getCredentials();
        }

        @Override
        public void refresh() {
            underlyingProvider.refresh();
        }

        private CognitoCachingCredentialsProvider getUnderlyingProvider() {
            return underlyingProvider;
        }

        private void setUnderlyingProvider(final CognitoCachingCredentialsProvider underlyingProvider) {
            // if the current underlyingProvider is not null
            this.underlyingProvider = underlyingProvider;
        }
    }

    /** Log tag. */
    private static final String LOG_TAG = IdentityManager.class.getSimpleName();

    /** Holder for the credentials provider, allowing the underlying provider to be swapped when necessary. */
    private final AWSCredentialsProviderHolder credentialsProviderHolder;

    /** Application context. */
    private final Context appContext;

    /** Configuration for the mobile helper. */
    private final AWSMobileHelperConfiguration awsMobileHelperConfiguration;

    /* Cognito client configuration. */
    private final ClientConfiguration clientConfiguration;

    /** Executor service for obtaining credentials in a background thread. */
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    /** Timeout CountdownLatch for doStartupAuth(). */
    private final CountDownLatch startupAuthTimeoutLatch = new CountDownLatch(1);

    /** Handles retrieving user profiles from the various identity providers. */
    private final IdentityProfileManager identityProfileManager = new IdentityProfileManager();

    /** The identity profile for the currently signed in user, otherwise null. */
    private volatile IdentityProfile currentIdentityProfile = null;

    /** Keep track of the registered sign-in providers. */
    private final List<Class<? extends SignInProvider>> signInProviderClasses = new LinkedList<>();

    /** Current provider beingIdentityProviderType used to obtain a Cognito access token. */
    private volatile IdentityProvider currentIdentityProvider = null;

    /** Results adapter for adapting results that came from logging in with a provider. */
    private SignInProviderResultAdapter resultsAdapter;

    /** Keep track of the currently registered SignInStateChangeListeners. */
    private final HashSet<SignInStateChangeListener> signInStateChangeListeners = new HashSet<>();

    /**
     * Custom Cognito Identity Provider to handle refreshing the individual provider's tokens.
     */
    private class AWSRefreshingCognitoIdentityProvider extends AWSBasicCognitoIdentityProvider {

        /** Log tag. */
        private final String LOG_TAG = AWSRefreshingCognitoIdentityProvider.class.getSimpleName();

        public AWSRefreshingCognitoIdentityProvider(final String accountId,
                                                    final String identityPoolId,
                                                    final ClientConfiguration clientConfiguration,
                                                    final Regions regions) {
            super(accountId, identityPoolId, clientConfiguration);
            // Force refreshing ID provider to use same region as caching credentials provider
            this.cib.setRegion(Region.getRegion(regions));
        }

        @Override
        public String refresh() {

            Log.d(LOG_TAG, "Refreshing token...");

            if (currentIdentityProvider != null) {
                final String newToken = currentIdentityProvider.refreshToken();

                getLogins().put(currentIdentityProvider.getCognitoLoginKey(), newToken);
            }
            return super.refresh();
        }
    }

    /**
     * Constructor. Initializes the Cognito credentials provider.
     * @param context the application context.
     * @param clientConfiguration the client configuration options such as retries and timeouts.
     */
    public IdentityManager(final Context context, final ClientConfiguration clientConfiguration,
                           final AWSMobileHelperConfiguration awsMobileHelperConfiguration) {
        Log.d(LOG_TAG, "IdentityManager init");
        this.appContext = context.getApplicationContext();
        this.awsMobileHelperConfiguration = awsMobileHelperConfiguration;
        this.clientConfiguration = clientConfiguration;
        credentialsProviderHolder = new AWSCredentialsProviderHolder();
        initializeCognito(this.appContext, this.clientConfiguration);
    }

    public AWSMobileHelperConfiguration getHelperConfiguration() {
        return awsMobileHelperConfiguration;
    }

    private void setCredentialsProvider(final Context context,
                                        final CognitoCachingCredentialsProvider cachingCredentialsProvider) {
        credentialsProviderHolder.setUnderlyingProvider(cachingCredentialsProvider);
    }

    private void initializeCognito(final Context context, final ClientConfiguration clientConfiguration) {

        final AWSRefreshingCognitoIdentityProvider refreshingCredentialsProvider =
            new AWSRefreshingCognitoIdentityProvider(null, awsMobileHelperConfiguration.getCognitoIdentityPoolId(),
                clientConfiguration, awsMobileHelperConfiguration.getCognitoRegion());

        setCredentialsProvider(context,
            new CognitoCachingCredentialsProvider(context, refreshingCredentialsProvider,
                awsMobileHelperConfiguration.getCognitoRegion(), clientConfiguration));
    }

    /**
     * @return true if the cached Cognito credentials are expired, otherwise false.
     */
    public boolean areCredentialsExpired() {

        final Date credentialsExpirationDate =
            credentialsProviderHolder.getUnderlyingProvider().getSessionCredentitalsExpiration();

        if (credentialsExpirationDate == null) {
            Log.d(LOG_TAG, "Credentials are EXPIRED.");
            return true;
        }

        long currentTime = System.currentTimeMillis() -
                (long)(SDKGlobalConfiguration.getGlobalTimeOffset() * 1000);

        final boolean credsAreExpired =
                (credentialsExpirationDate.getTime() - currentTime) < 0;

        Log.d(LOG_TAG, "Credentials are " + (credsAreExpired ? "EXPIRED." : "OK"));

        return credsAreExpired;
    }

    /**
     * @return the Cognito credentials provider.
     */
    public AWSCredentialsProvider getCredentialsProvider() {
        return this.credentialsProviderHolder;
    }

    public CognitoCachingCredentialsProvider getUnderlyingProvider() {
        return this.credentialsProviderHolder.getUnderlyingProvider();
    }

    /**
     * Gets the cached unique identifier for the user.
     * @return the cached unique identifier for the user.
     */
    public String getCachedUserID() {
        return credentialsProviderHolder.getUnderlyingProvider().getCachedIdentityId();
    }

    /**
     * Gets the user's unique identifier. This method can be called from
     * any thread.
     * @param handler handles the unique identifier for the user
     */
    public void getUserID(final IdentityHandler handler) {

        executorService.submit(new Runnable() {
            Exception exception = null;

            @Override
            public void run() {
                String identityId = null;

                try {
                    // Retrieve the user identity on the background thread.
                    identityId = credentialsProviderHolder.getUnderlyingProvider().getIdentityId();
                } catch (final Exception exception) {
                    this.exception = exception;
                    Log.e(LOG_TAG, exception.getMessage(), exception);
                } finally {
                    final String result = identityId;
                    Log.d(LOG_TAG, "Got user ID: " + identityId);

                    // Lint doesn't like early return inside a finally block, so nesting further inside the if here.
                    if (handler != null) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (exception != null) {
                                    handler.handleError(exception);
                                    return;
                                }

                                handler.onIdentityId(result);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * The adapter to handle results that come back from Cognito as well as handle the result from
     * any login providers.
     */
    private class SignInProviderResultAdapter implements SignInProviderResultHandler {
        final private SignInProviderResultHandler handler;

        private SignInProviderResultAdapter(final SignInProviderResultHandler handler) {
            this.handler = handler;
        }

        @Override
        public void onSuccess(final IdentityProvider provider) {
            Log.d(LOG_TAG,
                    String.format("SignInProviderResultAdapter.onSuccess(): %s provider sign-in succeeded.",
                            provider.getDisplayName()));
            // Update Cognito login with the token.
            federateWithProvider(provider);
        }

        private void onCognitoSuccess() {
            Log.d(LOG_TAG, "SignInProviderResultAdapter.onCognitoSuccess()");
            handler.onSuccess(currentIdentityProvider);
        }

        private void onCognitoError(final Exception ex) {
            Log.d(LOG_TAG, "SignInProviderResultAdapter.onCognitoError()", ex);
            final IdentityProvider provider = currentIdentityProvider;
            // Sign out of parent provider. This clears the currentIdentityProvider.
            IdentityManager.this.signOut();
            handler.onError(provider, new CognitoAuthException(provider, ex));
        }

        @Override
        public void onCancel(final IdentityProvider provider) {
            Log.d(LOG_TAG, String.format(
                "SignInProviderResultAdapter.onCancel(): %s provider sign-in canceled.",
                provider.getDisplayName()));
            handler.onCancel(provider);
        }

        @Override
        public void onError(final IdentityProvider provider, final Exception ex) {
            Log.e(LOG_TAG,
                String.format("SignInProviderResultAdapter.onError(): %s provider error. %s",
                              provider.getDisplayName(), ex.getMessage()), ex);
            handler.onError(provider, new ProviderAuthException(provider, ex));
        }
    }

    /**
     * Add a listener to receive callbacks when sign-in or sign-out occur.  The listener
     * methods will always be called on a background thread.
     *
     * @param listener the sign-in state change listener.
     */
    public void addSignInStateChangeListener(final SignInStateChangeListener listener) {
        synchronized (signInStateChangeListeners) {
            signInStateChangeListeners.add(listener);
        }
    }

    /**
     * Remove a listener from receiving callbacks when sign-in or sign-out occur.
     * @param listener the sign-in state change listener.
     */
    public void removeSignInStateChangeListener(final SignInStateChangeListener listener) {
        synchronized (signInStateChangeListeners) {
            signInStateChangeListeners.remove(listener);
        }
    }

    /**
     * Call getResultsAdapter to get the IdentityManager's handler that adapts results before
     * sending them back to the handler set by {@link #setProviderResultsHandler(SignInProviderResultHandler)}
     * @return the Identity Manager's results adapter.
     */
    public SignInProviderResultAdapter getResultsAdapter() {
        return resultsAdapter;
    }

    /**
     * Sign out of the current identity provider, and clear Cognito credentials.
     * Note: This call does not attempt to obtain un-auth credentials. To obtain an unauthenticated
     * anonymous (guest) identity, call {@link #getUserID(IdentityHandler)}.
     */
    public void signOut() {
        Log.d(LOG_TAG, "Signing out...");

        if (currentIdentityProvider != null) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    currentIdentityProvider.signOut();
                    credentialsProviderHolder.getUnderlyingProvider().clear();
                    currentIdentityProvider = null;
                    currentIdentityProfile = null;

                    // Notify state change listeners of sign out.
                    synchronized (signInStateChangeListeners) {
                        for (final SignInStateChangeListener listener : signInStateChangeListeners) {
                            listener.onUserSignedOut();
                        }
                    }
                }
            });
        }
    }

    private void refreshCredentialWithLogins(final Map<String, String> loginMap) {
        final CognitoCachingCredentialsProvider credentialsProvider =
            credentialsProviderHolder.getUnderlyingProvider();
        credentialsProvider.clear();
        credentialsProvider.withLogins(loginMap);
        // Calling refresh is equivalent to calling getIdentityId() + getCredentials().
        Log.d(getClass().getSimpleName(), "refresh credentials");
        credentialsProvider.refresh();
        Log.d(getClass().getSimpleName(), "Cognito ID: " + credentialsProvider.getIdentityId());
        Log.d(getClass().getSimpleName(), "Cognito Credentials: " + credentialsProvider.getCredentials());
        final String sharedPrefName = "com.amazonaws.android.auth";
        final String expirationKey = "expirationDate";
        // expire credentials in 2 minutes.
        appContext.getSharedPreferences(sharedPrefName,
            Context.MODE_PRIVATE).edit()
                .putLong(credentialsProvider.getIdentityPoolId() + "." + expirationKey, System.currentTimeMillis() + (510*1000))
                .commit();

    }

    /**
     * Set the results handler that will be used for results when calling federateWithProvider.
     *
     * @param signInProviderResultHandler the results handler.
     */
    public void setProviderResultsHandler(final SignInProviderResultHandler signInProviderResultHandler) {
        if (signInProviderResultHandler == null) {
            throw new IllegalArgumentException("signInProviderResultHandler cannot be null.");
        }
        this.resultsAdapter = new SignInProviderResultAdapter(signInProviderResultHandler);
    }

    /**
     * Login with an identity provider (ie. Facebook, Twitter, etc.).
     *
     * @param provider A sign-in provider.
     */
    public void federateWithProvider(final IdentityProvider provider) {
        Log.d(LOG_TAG, "federateWithProvider");
        final Map<String, String> loginMap = new HashMap<String, String>();
        loginMap.put(provider.getCognitoLoginKey(), provider.getToken());
        currentIdentityProvider = provider;
        initializeCognito(this.appContext, this.clientConfiguration);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshCredentialWithLogins(loginMap);
                } catch (Exception ex) {
                    resultsAdapter.onCognitoError(ex);
                    return;
                }

                resultsAdapter.onCognitoSuccess();

                // Notify state change listeners of sign out.
                synchronized (signInStateChangeListeners) {
                    for (final SignInStateChangeListener listener : signInStateChangeListeners) {
                        listener.onUserSignedIn();
                    }
                }
            }
        });
    }

    /**
     * Gets the current provider.
     * @return current provider or null if not signed-in
     */
    public IdentityProvider getCurrentIdentityProvider() {
        return currentIdentityProvider;
    }

    /**
     * Gets the identity profile manager responsible for retrieving a profiles for a user signed
     * in with a provider.
     * @return the identity profile manager.
     */
    public IdentityProfileManager getIdentityProfileManager() {
        return identityProfileManager;
    }

    /**
     * Reload the user info and image in the background.
     *
     * @param provider sign-in provider
     * @param onReloadComplete Runnable to be executed on the main thread after user info
     *                         and user image is reloaded.
     */
    public void loadUserIdentityProfile(final IdentityProvider provider, final Runnable onReloadComplete) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Retrieving user info and image from identity provider.");
                final IdentityProfile identityProfile;
                try {
                    identityProfile =
                        identityProfileManager.getIdentityProfile(provider);
                } catch (final ProfileRetrievalException ex) {
                    Log.e(LOG_TAG, "Cannot load identity profile.", ex);
                    return;
                }

                Log.d(LOG_TAG, "Loading user image from image url: " + identityProfile.getUserImageUrl());
                // preload user image
                try {
                    identityProfile.loadUserImage();
                } catch (final IOException ex) {
                    Log.w(LOG_TAG, "Failed to prefetch user image: " + identityProfile.getUserImageUrl(), ex);
                }
                currentIdentityProfile = identityProfile;
                ThreadUtils.runOnUiThread(onReloadComplete);
            }
        });
    }

    /**
     * Convenient method to get the identity profile for the current user if a user is signed-in
     * with a provider.
     *
     * @return the identity profile for the current user, otherwise null.
     */
    public IdentityProfile getIdentityProfile() {
        return currentIdentityProfile;
    }

    /**
     * Add a supported identity provider to your app. The provider will be presented as option to sign in to your app.
     *
     * @param providerClass the provider class for the identity provider.
     */
    public void addIdentityProvider(final Class<? extends SignInProvider> providerClass) {
        signInProviderClasses.add(providerClass);
    }

    public Collection<Class<? extends SignInProvider>> getSignInProviderClasses() {
        return signInProviderClasses;
    }


    /**
     * @return true if Cognito credentials have been obtained with at least one provider.
     */
    public boolean isUserSignedIn() {
        final Map<String, String> logins = credentialsProviderHolder.getUnderlyingProvider().getLogins();
        if (logins == null || logins.size() == 0)
            return false;
        return true;
    }

    private void handleStartupAuthResult(final Activity callingActivity,
                                         final StartupAuthResultHandler startupAuthResultHandler,
                                         final AuthException authException,
                                         final Exception unAuthException) {
        runAfterStartupAuthDelay(callingActivity, new Runnable() {
            @Override
            public void run() {
                startupAuthResultHandler.onComplete(new StartupAuthResult(IdentityManager.this,
                    new StartupAuthErrorDetails(authException, unAuthException)));
            }
        });
    }

    private void handleUnauthenticated(final Activity callingActivity,
                                       final StartupAuthResultHandler startupAuthResultHandler,
                                       final AuthException ex) {

        handleStartupAuthResult(callingActivity, startupAuthResultHandler, ex, null);
    }

    /**
     * Starts an activity after the splash timeout.
     * @param runnable runnable to run after the splash timeout expires.
     */
    private void runAfterStartupAuthDelay(final Activity callingActivity, final Runnable runnable) {
        executorService.submit(new Runnable() {
            public void run() {
                // Wait for the splash timeout expiry or for the user to tap.
                try {
                    startupAuthTimeoutLatch.await();
                } catch (InterruptedException e) {
                    Log.d(LOG_TAG, "Interrupted while waiting for startup auth minimum delay.");
                }

                callingActivity.runOnUiThread(runnable);
            }
        });
    }

    /**
     * This should be called from your app's splash activity upon start-up. If the user was previously
     * signed in, this will attempt to refresh their identity using the previously sign-ed in provider.
     * If the user was not previously signed in or their identity could not be refreshed with the
     * previously signed in provider and sign-in is optional, it will attempt to obtain an unauthenticated (guest)
     * identity.
     *
     * @param callingActivity the calling activity.
     * @param startupAuthResultHandler a handler for returning results.
     * @param minimumDelay the minimum delay to wait before returning the sign-in result.
     */
    public void doStartupAuth(final Activity callingActivity,
                              final StartupAuthResultHandler startupAuthResultHandler,
                              final long minimumDelay) {

        executorService.submit(new Runnable() {
            public void run() {
                Log.d(LOG_TAG, "Starting up authentication...");
                final SignInManager signInManager = SignInManager.getInstance(
                    callingActivity.getApplicationContext(), IdentityManager.this);

                if (signInManager == null) {
                    throw new IllegalStateException("You cannot pass null for identityManager.");
                }

                final SignInProvider provider = signInManager.getPreviouslySignedInProvider();

                // if the user was already previously signed-in to a provider.
                if (provider != null) {
                    Log.d(LOG_TAG, "Refreshing credentials with identity provider " + provider.getDisplayName());
                    // asynchronously handle refreshing credentials and call our handler.
                    signInManager.refreshCredentialsWithProvider(callingActivity,
                        provider, new SignInProviderResultHandler() {

                            @Override
                            public void onSuccess(final IdentityProvider provider) {
                                // The sign-in manager is no longer needed once signed in.
                                SignInManager.dispose();

                                Log.d(LOG_TAG, "Successfully got credentials from identity provider '"
                                    + provider.getDisplayName() + "'. Now loading User info.");

                                loadUserIdentityProfile(provider, new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(LOG_TAG, "Successfully loaded user info and image.");
                                        runAfterStartupAuthDelay(callingActivity, new Runnable() {
                                            @Override
                                            public void run() {
                                                startupAuthResultHandler.onComplete(
                                                    new StartupAuthResult(IdentityManager.this, null));
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onCancel(final IdentityProvider provider) {
                                // Should never happen.
                                Log.wtf(LOG_TAG, "Cancel can't happen when handling a previously signed-in user.");
                            }

                            @Override
                            public void onError(final IdentityProvider provider, final Exception ex) {
                                Log.e(LOG_TAG,
                                    String.format("Cognito credentials refresh with %s provider failed. Error: %s",
                                        provider.getDisplayName(), ex.getMessage()), ex);

                                if (ex instanceof AuthException) {
                                    handleUnauthenticated(callingActivity, startupAuthResultHandler,
                                        (AuthException) ex);
                                } else {
                                    handleUnauthenticated(callingActivity, startupAuthResultHandler,
                                        new AuthException(provider, ex));
                                }
                            }
                        });
                } else {
                    handleUnauthenticated(callingActivity, startupAuthResultHandler, null);
                }

                if (minimumDelay > 0) {
                    // Wait for the splash timeout.
                    try {
                        Thread.sleep(minimumDelay);
                    } catch (final InterruptedException ex) {
                        Log.i(LOG_TAG, "Interrupted while waiting for startup auth timeout.");
                    }
                }

                // Expire the splash page delay.
                startupAuthTimeoutLatch.countDown();
            }
        });

    }

    /**
     * This should be called from your app's splash activity upon start-up. If the user was previously
     * signed in, this will attempt to refresh their identity using the previously sign-ed in provider.
     * If the user was not previously signed in or their identity could not be refreshed with the
     * previously signed in provider and sign-in is optional, it will attempt to obtain an unauthenticated (guest)
     * identity.
     *
     * @param callingActivity the calling activity.
     * @param startupAuthResultHandler a handler for returning results.
     */
    public void doStartupAuth(final Activity callingActivity,
                              final StartupAuthResultHandler startupAuthResultHandler) {
        doStartupAuth(callingActivity, startupAuthResultHandler, 0);
    }

    /**
     * Call this to ignore waiting for the remaining timeout delay.
     */
    public void expireSignInTimeout() {
        startupAuthTimeoutLatch.countDown();
    }

    /**
     * Call signInOrSignUp to initiate sign-in with a provider. This will launch the Sign-in Activity.
     *
     * Note: This should not be called when already signed in with a provider.
     *
     * @param context context.
     * @param signInResultHandler the results handler.
     */
    public void signInOrSignUp(final Context context,
                               final SignInResultHandler signInResultHandler) {
        // Start the sign-in activity. We do not finish the calling activity allowing the user to navigate back.
        final SignInManager signInManager = SignInManager.getInstance(
            context.getApplicationContext(), this);
        signInManager.setResultHandler(signInResultHandler);
        context.startActivity(new Intent(context, FacebookSignInActivity.class));
    }
}

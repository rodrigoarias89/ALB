//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.18
//
package com.alabarra.gui.activities;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alabarra.R;
import com.alabarra.SplashActivity;
import com.alabarra.api.BackgroundTaskListener;
import com.alabarra.api.VenueManager;
import com.alabarra.gui.fragments.GetInfoFragment;
import com.alabarra.gui.fragments.HistoryFragment;
import com.alabarra.gui.fragments.MainMenuFragment;
import com.alabarra.gui.fragments.NoPermissionFragment;
import com.alabarra.gui.fragments.NoResultsFragment;
import com.alabarra.gui.fragments.ProfileFragment;
import com.alabarra.gui.fragments.VenueFragment;
import com.alabarra.gui.fragments.VenueListFragment;
import com.alabarra.gui.listeners.NavigationInteractionListener;
import com.alabarra.gui.listeners.SearchInteracionListener;
import com.alabarra.model.Venue;
import com.amazonaws.mobile.AWSMobileClient;

import java.util.List;

public class MainActivity extends BaseLocationActivity implements NavigationInteractionListener, LocationListener, SearchInteracionListener {

    private final static String TAG = "MainActivity";

    private List<Venue> mLastSearchVenues;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        setContentView(R.layout.activity_main);

        findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new ProfileFragment();
                FragmentManager fragmentManager = getFragmentManager();
                dialog.show(fragmentManager, ProfileFragment.TAG);
            }
        });

        if (hasLocationPermission()) {
            onLocationPermissionGranted();
        } else {
            goToFragment(new NoPermissionFragment(), null);
            getLocationPermission();
        }
    }

    @Override
    public void goToMainMenu() {
        goToFragment(new MainMenuFragment(), null);
    }

    @Override
    public void goToHistory() {
        goToFragment(new HistoryFragment(), HistoryFragment.TAG);
    }

    private void goToNoVenuesFound() {
        goToFragment(new NoResultsFragment(), NoResultsFragment.TAG);
    }

    private void goToSearchResults() {
        goToFragment(new VenueListFragment(), VenueListFragment.TAG);
    }

    @Override
    public void goToVenue(Venue venue) {
        Fragment fragment = new VenueFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(VenueFragment.VENUE_ARG, venue);
        fragment.setArguments(bundle);
        goToFragment(fragment, VenueFragment.TAG);
    }

    public void onLocationPermissionGranted() {
        goToMainMenu();
    }

    private void goToFragment(Fragment fragment, String fragmentTag) {
        try {
            FragmentTransaction ft = getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragment, fragmentTag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (Exception e) {
            Log.e(TAG, "Error on refreshing fragment", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AWSMobileClient.defaultMobileClient().getIdentityManager().isUserSignedIn()) {
            // In the case that the activity is restarted by the OS after the application
            // is killed we must redirect to the splash activity to handle the sign-in flow.
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
    }

    @Override
    public void onFindBars() {
        goToFragment(new GetInfoFragment(), null);
    }

    /*
    *
    *  Location Listener
    *
     */

    @Override
    public void onLocationChanged(Location location) {

        mLastKnownLocation = location;
        mLocationManager.removeUpdates(this);

        VenueManager.getInstance().findVenuesNearbyAsync(location, new BackgroundTaskListener<List<Venue>>() {
            @Override
            public void onSuccess(List<Venue> object) {
                if (object == null || object.isEmpty()) {
                    goToNoVenuesFound();
                } else {
                    mLastSearchVenues = object;
                    goToSearchResults();
                }

            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Exception on getting bars", e);
                new AlertDialog.Builder(MainActivity.this).setMessage(R.string.unknown_error).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goToMainMenu();
                    }
                }).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (hasToGoToMenu()) {
            goToMainMenu();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasToGoToMenu() {
        Fragment fragmentNoResults = getFragmentManager().findFragmentByTag(NoResultsFragment.TAG);
        if (fragmentNoResults != null && fragmentNoResults.isVisible()) {
            return true;
        }
        Fragment fragmentResults = getFragmentManager().findFragmentByTag(VenueListFragment.TAG);
        if (fragmentResults != null && fragmentResults.isVisible()) {
            return true;
        }
        Fragment fragmentVenue = getFragmentManager().findFragmentByTag(VenueFragment.TAG);
        if (fragmentVenue != null && fragmentVenue.isVisible()) {
            return true;
        }
        Fragment fragmentHistory = getFragmentManager().findFragmentByTag(HistoryFragment.TAG);
        if (fragmentHistory != null && fragmentHistory.isVisible()) {
            return true;
        }
        return false;
    }

    @Override
    public List<Venue> getFoundedVenues() {
        return mLastSearchVenues;
    }

    @Override
    public Location getCurrentLocation() {
        return mLastKnownLocation;
    }
}

//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.18
//
package com.alabarra.gui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alabarra.R;
import com.alabarra.SplashActivity;
import com.alabarra.gui.fragments.MainMenuFragment;
import com.alabarra.gui.fragments.NoPermissionFragment;
import com.alabarra.gui.fragments.ProfileFragment;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobilehelper.auth.IdentityManager;

public class MainActivity extends BaseLocationActivity {

    private IdentityManager identityManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();

        setContentView(R.layout.activity_main);

        findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                goToFragment(new ProfileFragment());
            }
        });

        if (hasLocationPermission()) {
            onLocationPermissionGranted();
        } else {
            goToFragment(new NoPermissionFragment());
            getLocationPermission();
        }
    }

    public void onLocationPermissionGranted() {
        goToFragment(new MainMenuFragment());
    }

    private void goToFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
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

}
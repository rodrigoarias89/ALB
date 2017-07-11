package com.alabarra.gui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.alabarra.gui.listeners.LocationPermissionInteractionListener;


public abstract class BaseLocationActivity extends AppCompatActivity implements LocationPermissionInteractionListener {

    private static final int LOCATION_PERMISSIONS_REQUEST_RESULT = 1001;

    /**
     * Permissions
     */

    public boolean hasLocationPermission() {
        switch (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            case PackageManager.PERMISSION_GRANTED:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void getLocationPermission() {
        if (!hasLocationPermission()) {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSIONS_REQUEST_RESULT);
        }
    }

    public void onLocationPermissionGranted() {
        //Override if necessary
    }

    private void askForPermission(String permission, int response) {
        ActivityCompat.requestPermissions(this,
                new String[]{permission},
                response);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_RESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onLocationPermissionGranted();
                }
                return;
            }
        }
    }

}

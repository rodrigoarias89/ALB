package com.alabarra.gui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.alabarra.gui.listeners.LocationInteractionListener;

import java.util.Calendar;


public abstract class BaseLocationActivity extends AppCompatActivity implements LocationInteractionListener, LocationListener {

    private static final int LOCATION_PERMISSIONS_REQUEST_RESULT = 1001;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    protected LocationManager mLocationManager;

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

    @Override
    public void getLocation() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - MIN_TIME) {
            onLocationChanged(location);
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }
    }

    /*
    *
    * Location listener methods. Override if necessary
    *
     */

    @Override
    public abstract void onLocationChanged(Location location);

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}

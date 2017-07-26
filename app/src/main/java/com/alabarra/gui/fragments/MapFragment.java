package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.listeners.NavigationInteractionListener;
import com.alabarra.gui.listeners.SearchInteracionListener;
import com.alabarra.model.Venue;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by rodrigoarias on 7/25/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public final static String TAG = "MapFragment";

    private MapView mMapView;

    private NavigationInteractionListener mNavigationListener;
    private SearchInteracionListener mSearchListener;


    //For API pre 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            initFragment(activity);
        }
    }

    //Not called in API pre 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initFragment(context);
    }

    private void initFragment(Context context) {
        mSearchListener = (SearchInteracionListener) context;
        mNavigationListener = (NavigationInteractionListener) context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        view.findViewById(R.id.go_to_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigationListener.goToListFromMap();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            googleMap.setMyLocationEnabled(true);

            LatLng latLng = new LatLng(mSearchListener.getCurrentLocation().getLatitude(), mSearchListener.getCurrentLocation().getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(cameraUpdate);

        } catch (SecurityException e) {
            Crashlytics.logException(e);
            Log.e(TAG, "No permissions", e);
        }

        for (Venue venue : mSearchListener.getFoundedVenues()) {
            LatLng venuePos = new LatLng(venue.getLocation().getLatitude(), venue.getLocation().getLongitude());
            googleMap.addMarker(new MarkerOptions().position(venuePos).title(venue.getName()).snippet(venue.getAddress()));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}

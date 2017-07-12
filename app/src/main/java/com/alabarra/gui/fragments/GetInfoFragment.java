package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.listeners.LocationInteractionListener;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class GetInfoFragment extends Fragment {

    private LocationManager mLocationManager;
    private LocationInteractionListener mListener;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;


    //For API pre 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        mListener = (LocationInteractionListener) context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListener.getLocation();
    }

}

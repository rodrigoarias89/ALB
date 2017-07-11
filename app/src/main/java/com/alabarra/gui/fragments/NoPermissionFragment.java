package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.listeners.LocationPermissionInteractionListener;

/**
 * Created by rodrigoarias on 7/10/17.
 */

public class NoPermissionFragment extends Fragment {

    private LocationPermissionInteractionListener mListener;


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
        mListener = (LocationPermissionInteractionListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_permissions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.ask_for_permissions_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getLocationPermission();
            }
        });
    }

}

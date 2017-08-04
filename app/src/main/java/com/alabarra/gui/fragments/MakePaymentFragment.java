package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.listeners.ScreenInteractionListener;

/**
 * Created by rodrigoarias on 8/1/17.
 */

public class MakePaymentFragment extends Fragment {

    public static String TAG = "MakePaymentFragment";

    private ScreenInteractionListener mListener;

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
        mListener = (ScreenInteractionListener) context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_making_purchase, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListener.showMenuBar(false);
    }

}

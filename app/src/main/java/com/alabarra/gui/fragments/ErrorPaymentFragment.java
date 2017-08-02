package com.alabarra.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 8/1/17.
 */

public class ErrorPaymentFragment extends Fragment {
    public final static String TAG = "ErrorPaymentFragment";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error_payment, container, false);
    }


}

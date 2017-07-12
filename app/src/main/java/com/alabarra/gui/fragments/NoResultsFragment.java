package com.alabarra.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class NoResultsFragment extends Fragment {

    public final static String TAG = "NoResultsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_no_results, container, false);
    }

}

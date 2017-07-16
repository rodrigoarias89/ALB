package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.list.venues.VenueRecyclerAdapter;
import com.alabarra.gui.list.venues.VenueRecyclerView;
import com.alabarra.gui.listeners.NavigationInteractionListener;
import com.alabarra.gui.listeners.SearchInteracionListener;
import com.alabarra.model.Venue;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueListFragment extends Fragment implements VenueRecyclerAdapter.OnVenueClickListener {

    public final static String TAG = "VenueListFragment";

    private SearchInteracionListener mSearchListener;
    private NavigationInteractionListener mNavigationListener;


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
        mSearchListener = (SearchInteracionListener) context;
        mNavigationListener = (NavigationInteractionListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_venues, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        VenueRecyclerView venueRecyclerView = (VenueRecyclerView) view.findViewById(R.id.venue_recycler_view);
        venueRecyclerView.setVenuesAndLocation(mSearchListener.getFoundedVenues(), mSearchListener.getCurrentLocation());
        venueRecyclerView.setOnVenueClickListener(this);
    }

    @Override
    public void onVenueClicked(Venue venue) {
        mNavigationListener.goToVenue(venue);
    }


}

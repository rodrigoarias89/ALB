package com.alabarra.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabarra.R;
import com.alabarra.model.Venue;
import com.bumptech.glide.Glide;

/**
 * Created by rodrigoarias on 7/12/17.
 */

public class VenueFragment extends Fragment {

    public static final String TAG = "VenueFragment";
    public static final String VENUE_ARG = "VENUE_ARG";
    private Venue mVenue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_venue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mVenue = getArguments().getParcelable(VENUE_ARG);

        TextView title = (TextView) view.findViewById(R.id.venue_title);
        TextView address = (TextView) view.findViewById(R.id.venue_address);
        AppCompatImageView image = (AppCompatImageView) view.findViewById(R.id.venue_image);

        if (mVenue != null) {
            title.setText(mVenue.getName());
            address.setText(mVenue.getAddress());
            Glide.with(getActivity()).load(mVenue.getPicture()).into(image);
        }
    }

}
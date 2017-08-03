package com.alabarra.gui.list.venues;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.gui.utils.PositionUtils;
import com.alabarra.model.Venue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueRecyclerAdapter extends RecyclerView.Adapter<VenueRecyclerAdapter.VenueViewHolder> {

    private Location mCurrentLocation;
    private List<Venue> mVenueList;
    private Comparator<Venue> comparator;

    private OnVenueClickListener mListener;

    public VenueRecyclerAdapter() {
        comparator = new Comparator<Venue>() {
            @Override
            public int compare(Venue venue1, Venue venue2) {
                if (mCurrentLocation != null) {
                    return (int) (mCurrentLocation.distanceTo(venue1.getLocation()) - mCurrentLocation.distanceTo(venue2.getLocation()));
                } else {
                    return 0;
                }
            }
        };
    }

    public void setCurrentLocation(Location location) {
        mCurrentLocation = location;
    }

    public void setListener(OnVenueClickListener listener) {
        mListener = listener;
    }

    public void setVenues(List<Venue> venues) {
        mVenueList = venues;
        Collections.sort(mVenueList, comparator);
    }

    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VenueViewHolder(new VenueRecyclerCell(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(VenueViewHolder holder, int position) {
        holder.setData(mVenueList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVenueList.size();
    }

    class VenueViewHolder extends RecyclerView.ViewHolder {

        private VenueRecyclerCell mCell;

        public VenueViewHolder(VenueRecyclerCell itemView) {
            super(itemView);
            mCell = itemView;
        }

        public void setData(final Venue venue) {
            String distance = "";
            if (mCurrentLocation != null) {
                distance = PositionUtils.getFormattedDistance(mCurrentLocation.distanceTo(venue.getLocation()));
            }
            mCell.setData(venue.getName().toUpperCase(), venue.getAddress(), distance);
            mCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onVenueClicked(venue);
                }
            });
        }
    }


    /*
    *
    * On Click Listener
    *
     */

    public interface OnVenueClickListener {
        void onVenueClicked(Venue venue);
    }

}

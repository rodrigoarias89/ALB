package com.alabarra.gui.list;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.gui.utils.PositionUtils;
import com.alabarra.model.Venue;

import java.util.List;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueRecyclerAdapter extends RecyclerView.Adapter<VenueRecyclerAdapter.VenueViewHolder> {

    private Location mCurrentLocation;
    private List<Venue> mVenueList;

    private OnVenueClickListener mListener;

    public void setCurrentLocation(Location location) {
        mCurrentLocation = location;
    }

    public void setListener(OnVenueClickListener listener) {
        mListener = listener;
    }

    public void setVenues(List<Venue> venues) {
        mVenueList = venues;
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

    class VenueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Venue mVenue;
        private VenueRecyclerCell mCell;

        public VenueViewHolder(VenueRecyclerCell itemView) {
            super(itemView);
            mCell = itemView;
        }

        public void setData(final Venue venue) {
            mVenue = venue;

            String distance = "";
            if (mCurrentLocation != null) {
                distance = PositionUtils.getFormattedDistance(mCurrentLocation.distanceTo(venue.getLocation()));
            }
            mCell.setData(venue.getName(), distance);
            mCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onVenueClicked(venue);
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onVenueClicked(mVenue);
            }
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

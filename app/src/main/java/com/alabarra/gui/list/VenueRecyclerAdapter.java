package com.alabarra.gui.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alabarra.model.Venue;

import java.util.List;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueRecyclerAdapter extends RecyclerView.Adapter<VenueRecyclerAdapter.VenueViewHolder> {

    private List<Venue> mVenueList;

    public void setVenues(List<Venue> venues) {
        mVenueList = venues;
    }

    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VenueViewHolder(new VenueRecyclerCell(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(VenueViewHolder holder, int position) {
        holder.setName(mVenueList.get(position));
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

        public void setName(Venue venue) {
            //TODO
            mCell.setData(venue.getName(), "");
        }

    }

}

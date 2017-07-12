package com.alabarra.gui.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.alabarra.model.Venue;

import java.util.List;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueRecyclerView extends RecyclerView {

    private VenueRecyclerAdapter mAdapter;


    public VenueRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

//        UalaDividerItemDecoration dividerItemDecoration = new UalaDividerItemDecoration(getContext(),
//                layoutManager.getOrientation());
//        addItemDecoration(dividerItemDecoration);

        mAdapter = new VenueRecyclerAdapter();
        setAdapter(mAdapter);
    }

    public void setVenues(List<Venue> venues) {
        mAdapter.setVenues(venues);
    }

}

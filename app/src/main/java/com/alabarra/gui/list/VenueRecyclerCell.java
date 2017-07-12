package com.alabarra.gui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueRecyclerCell extends FrameLayout {

    private TextView mTitle;

    public VenueRecyclerCell(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.component_venue_cell, this);
        mTitle = (TextView) view.findViewById(R.id.venue_cell_title);
    }

    public void setData(String name, String distance) {
        mTitle.setText(name);
    }
}

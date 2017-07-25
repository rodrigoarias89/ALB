package com.alabarra.gui.list.menues;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 7/15/17.
 */

public class CategoryRecyclerCell extends FrameLayout {

    private TextView mTitle;

    public CategoryRecyclerCell(@NonNull Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.component_category_cell, this);
        mTitle = (TextView) view.findViewById(R.id.category_cell_title);

    }

    public void setData(String name) {
        mTitle.setText(name.toUpperCase());
    }


}

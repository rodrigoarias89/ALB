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

public class MenuItemRecyclerCell extends FrameLayout {

    private TextView mTitle;
    private TextView mAmount;

    public MenuItemRecyclerCell(@NonNull Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.component_menu_item_cell, this);
        mTitle = (TextView) view.findViewById(R.id.menu_item_cell_title);
        mAmount = (TextView) view.findViewById(R.id.menu_item_cell_amount);

    }

    public void setData(String name, String amount) {
        mTitle.setText(name);
        mAmount.setText(amount);
    }


}

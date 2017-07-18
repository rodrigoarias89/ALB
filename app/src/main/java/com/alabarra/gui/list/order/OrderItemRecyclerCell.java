package com.alabarra.gui.list.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class OrderItemRecyclerCell extends FrameLayout {

    private TextView mTitle;
    private TextView mAmount;
    private TextView mQuantity;

    public OrderItemRecyclerCell(@NonNull Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.component_order_item_cell, this);
        mTitle = (TextView) view.findViewById(R.id.item_order_cell_title);
        mQuantity = (TextView) view.findViewById(R.id.item_order_cell_quantity);
        mAmount = (TextView) view.findViewById(R.id.item_order_cell_amount);
    }

    public void setData(String name, String quantity, String amount) {
        mTitle.setText(name);
        mQuantity.setText(quantity);
        mAmount.setText(amount);
    }

}

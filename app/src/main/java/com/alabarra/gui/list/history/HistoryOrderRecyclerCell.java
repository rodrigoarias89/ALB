package com.alabarra.gui.list.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 7/16/17.
 */

public class HistoryOrderRecyclerCell extends FrameLayout {

    private TextView mTitle;
    private TextView mDate;
    private TextView mAmount;

    public HistoryOrderRecyclerCell(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.component_history_order_cell, this);
        mTitle = (TextView) view.findViewById(R.id.history_order_cell_title);
        mDate = (TextView) view.findViewById(R.id.history_order_cell_date);
        mAmount = (TextView) view.findViewById(R.id.history_order_cell_amount);
    }

    public void setData(String name, String date, String amount) {
        mTitle.setText(name);
        mDate.setText(date);
        mAmount.setText(amount);
    }

}

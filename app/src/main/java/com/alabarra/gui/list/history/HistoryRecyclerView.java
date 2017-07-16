package com.alabarra.gui.list.history;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.alabarra.gui.components.CustomDividerItemDecoration;
import com.alabarra.model.DoneOrder;

import java.util.List;

/**
 * Created by rodrigoarias on 7/16/17.
 */

public class HistoryRecyclerView extends RecyclerView {

    private HistoryRecyclerAdapter mAdapter;

    public HistoryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new CustomDividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        addItemDecoration(dividerItemDecoration);

        mAdapter = new HistoryRecyclerAdapter();
        setAdapter(mAdapter);
    }

    public void setOrders(List<DoneOrder> orders) {
        mAdapter.setOrders(orders);
    }

}

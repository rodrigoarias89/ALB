package com.alabarra.gui.list.order;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.alabarra.model.MenuItem;

import java.util.Map;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class OrderRecyclerView extends RecyclerView {

    private OrderRecyclerAdapter mAdapter;

    public OrderRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

//        DividerItemDecoration dividerItemDecoration = new CustomDividerItemDecoration(getContext(),
//                layoutManager.getOrientation());
//        addItemDecoration(dividerItemDecoration);

        mAdapter = new OrderRecyclerAdapter();
        setAdapter(mAdapter);
    }

    public void setItems(Map<MenuItem, Integer> items) {
        mAdapter.setItems(items);
    }


}

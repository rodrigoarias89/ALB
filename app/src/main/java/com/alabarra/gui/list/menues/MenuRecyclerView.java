package com.alabarra.gui.list.menues;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.alabarra.gui.listeners.OnMenuItemClickListener;
import com.alabarra.model.Menu;

/**
 * Created by rodrigoarias on 7/15/17.
 */

public class MenuRecyclerView extends RecyclerView {

    private MenuRecyclerAdapter mAdapter;

    public MenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

        mAdapter = new MenuRecyclerAdapter();
        setAdapter(mAdapter);
    }

    public void setMenu(Menu menu) {
        mAdapter.setMenu(menu);
        mAdapter.notifyDataSetChanged();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mAdapter.setOnMenuItemClickListener(listener);
    }
}

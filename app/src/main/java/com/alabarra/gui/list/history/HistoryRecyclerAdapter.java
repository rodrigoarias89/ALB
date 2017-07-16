package com.alabarra.gui.list.history;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by rodrigoarias on 7/16/17.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder> {


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        private HistoryOrderRecyclerView mCell;

        public HistoryViewHolder(HistoryOrderRecyclerView itemView) {
            super(itemView);
            mCell = itemView;
        }

        public void setData() {
        }
    }

}

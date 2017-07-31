package com.alabarra.gui.list.history;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alabarra.model.DoneOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigoarias on 7/16/17.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder> {

    private List<DoneOrder> mOrders;

    HistoryRecyclerAdapter() {
        mOrders = new ArrayList<>();
    }

    void setOrders(List<DoneOrder> orders) {
        mOrders = orders;
        notifyDataSetChanged();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(new HistoryOrderRecyclerCell(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.setData(mOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        private HistoryOrderRecyclerCell mCell;

        HistoryViewHolder(HistoryOrderRecyclerCell itemView) {
            super(itemView);
            mCell = itemView;
        }

        public void setData(DoneOrder order) {
            mCell.setData(order.getVenue().getName(), order.getDate(), order.getAmount());
        }
    }

}

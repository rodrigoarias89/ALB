package com.alabarra.gui.list.order;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alabarra.model.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {

    List<Map.Entry<MenuItem, Integer>> mItems;

    public OrderRecyclerAdapter() {
        mItems = new ArrayList<>();
    }

    public void setItems(Map<MenuItem, Integer> items) {
        mItems = new ArrayList<>(items.entrySet());
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(new OrderItemRecyclerCell(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.setData(mItems.get(position).getKey(), mItems.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        private OrderItemRecyclerCell mCell;

        OrderViewHolder(OrderItemRecyclerCell itemView) {
            super(itemView);
            mCell = itemView;
        }

        void setData(MenuItem item, int quantity) {
            float fullPrice = item.getPrice() * quantity;
            //TODO hardcoded strings
            mCell.setData(item.getName(), "x " + quantity + " unidades", "$" + fullPrice);
        }
    }
}

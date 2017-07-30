package com.alabarra.gui.list.order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.gui.listeners.OnMenuItemClickListener;
import com.alabarra.model.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {

    List<Map.Entry<MenuItem, Integer>> mItems;
    private OnMenuItemClickListener mMenuItemListener;

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

        void setData(final MenuItem item, int quantity) {
            float fullPrice = item.getPrice() * quantity;
            //TODO hardcoded strings
            if (quantity > 1) {
                mCell.setData(item.getName(), "x " + quantity, "$" + fullPrice);
            } else {
                mCell.setData(item.getName(), "", "$" + fullPrice);
            }
            if (mMenuItemListener != null) {
                mCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMenuItemListener.onMenuItemClick(item);
                    }
                });
            }
        }
    }

    public void removeItem(MenuItem menuItem) {
        Map.Entry<MenuItem, Integer> row = null;
        for (Map.Entry<MenuItem, Integer> entry : mItems) {
            if (entry.getKey() == menuItem) {
                row = entry;
                break;
            }
        }
        if (row != null) {
            int index = mItems.indexOf(row);
            if (row.getValue() == 1) {
                mItems.remove(index);
                notifyItemRemoved(index);
            } else {
//                row.setValue(row.getValue() - 1);
                notifyItemChanged(index);
            }
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mMenuItemListener = listener;
    }
}

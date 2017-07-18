package com.alabarra.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rodrigoarias on 7/16/17.
 */

public class Order {

    private Venue mVenue;
    private Map<MenuItem, Integer> mItems;

    private OnOrderUpdateListener mListener;

    public void setOnOrderUpdateListener(OnOrderUpdateListener listener) {
        mListener = listener;
    }

    public Order(Venue venue) {
        mVenue = venue;
        mItems = new HashMap<>();
    }

    public Map<MenuItem, Integer> getItems() {
        return mItems;
    }

    public void addMenuItem(MenuItem menuItem, int quantity) {
        if (mItems.containsKey(menuItem)) {
            int previousQuantity = mItems.get(menuItem);
            mItems.put(menuItem, previousQuantity + quantity);
        } else {
            mItems.put(menuItem, quantity);
        }
        notifyOrderUpdate();
    }

    private void notifyOrderUpdate() {
        if (mListener != null) {
            if (mItems.isEmpty()) {
                mListener.onOrderEmpty();
            } else {
                mListener.onOrderUpdate(getOrderAmount());
            }
        }
    }

    public float getOrderAmount() {
        float totalAmount = 0;
        for (Map.Entry<MenuItem, Integer> entries : mItems.entrySet()) {
            totalAmount += (entries.getKey().getPrice() * entries.getValue());
        }
        return totalAmount;
    }


    public interface OnOrderUpdateListener {

        void onOrderUpdate(float amount);

        void onOrderEmpty();

    }
}

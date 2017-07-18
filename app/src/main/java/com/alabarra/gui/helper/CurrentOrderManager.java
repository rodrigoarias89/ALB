package com.alabarra.gui.helper;

import com.alabarra.model.Order;
import com.alabarra.model.Venue;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class CurrentOrderManager {

    private static CurrentOrderManager mInstance;

    private Venue mVenue;
    private Order mOrder;

    private CurrentOrderManager() {
    }

    public static CurrentOrderManager getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentOrderManager();
        }
        return mInstance;
    }

    public static void reset() {
        mInstance = null;
    }

    public void setNewOrder(Venue venue, Order order) {
        mVenue = venue;
        mOrder = order;
    }

    public Venue getVenue() {
        return mVenue;
    }

    public Order getOrder() {
        return mOrder;
    }
}

package com.alabarra.model;

/**
 * Created by rodrigoarias on 7/16/17.
 */

public class DoneOrder {

    private String mId;
    private String mAmount;
    private String mDate;
    private Venue mVenue;

    public DoneOrder(String id, String amount, String date, Venue venue) {
        mId = id;
        mAmount = amount;
        mDate  = date;
        mVenue = venue;
    }

    public String getId() {
        return mId;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getDate() {
        return mDate;
    }

    public Venue getVenue() {
        return mVenue;
    }
}

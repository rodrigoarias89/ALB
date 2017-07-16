package com.alabarra.model;

/**
 * Created by rodrigoarias on 7/15/17.
 */

public class MenuItem {

    private String mId;
    private String mName;
    private Float mPrice;
    private String mDescription;

    public MenuItem(String id, String name, float price, String description) {
        mId = id;
        mName = name;
        mPrice = price;
        mDescription = description;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Float getPrice() {
        return mPrice;
    }

    public String getDescription() {
        return mDescription;
    }
}

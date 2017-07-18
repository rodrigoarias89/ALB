package com.alabarra.model;

import android.location.Location;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class Venue {

    private String mId;
    private String mName;
    private String mAddress;
    private String mPicture;
    private Location mLocation;
    private Menu mMenu;

    public Venue(String id, String name, String address, String picture, Location location) {
        mId = id;
        mName = name;
        mAddress = address;
        mPicture = picture;
        mLocation = location;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getPicture() {
        return mPicture;
    }

    public Location getLocation() {
        return mLocation;
    }

    public Menu getMenu() {
        return mMenu;
    }

    public void setMenu(Menu menu) {
        mMenu = menu;
    }
}

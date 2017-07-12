package com.alabarra.model;

import android.location.Location;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class Venue {

    private String mName;
    private String mAddress;
    private String mPicture;
    private Location mLocation;

    public Venue(String name, String address, String picture, Location location) {
        mName = name;
        mAddress = address;
        mPicture = picture;
        mLocation = location;
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

}

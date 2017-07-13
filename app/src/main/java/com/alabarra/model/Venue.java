package com.alabarra.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class Venue implements Parcelable {

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


    /*
    *
    * Parcelable
    *
     */

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mAddress);
        parcel.writeString(mPicture);
        parcel.writeParcelable(mLocation, i);

    }

    private Venue(Parcel in) {
        mName = in.readString();
        mAddress = in.readString();
        mPicture = in.readString();
        mLocation = in.readParcelable(Location.class.getClassLoader());
    }

}

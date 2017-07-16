package com.alabarra.model;

import java.util.List;

/**
 * Created by rodrigoarias on 7/15/17.
 */

public class Category {

    private String mId;
    private String mName;
    private String mDescription;
    private List<MenuItem> mItems;

    public Category(String id, String name, String description, List<MenuItem> items) {
        mId = id;
        mName = name;
        mDescription = description;
        mItems = items;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public List<MenuItem> getItems() {
        return mItems;
    }
}

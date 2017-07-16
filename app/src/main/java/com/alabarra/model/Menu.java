package com.alabarra.model;

import java.util.List;

/**
 * Created by rodrigoarias on 7/15/17.
 */

public class Menu {

    private List<Category> mCategories;

    public Menu(List<Category> categories) {
        mCategories = categories;
    }

    public List<Category> getCategories() {
        return mCategories;
    }
}

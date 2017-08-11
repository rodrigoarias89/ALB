package com.alabarra.model;

import android.location.Location;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import ar.com.alabarra.clientsdk.model.MenuModel;
import ar.com.alabarra.clientsdk.model.MenuModelCategoriesItem;
import ar.com.alabarra.clientsdk.model.MenuModelCategoriesItemItemsItem;
import ar.com.alabarra.clientsdk.model.VenuesModelVenuesItem;

/**
 * Created by rodrigoarias on 7/15/17.
 */

public class VenueFactory {

    private final static String TAG = "VenueFactory";

    public static Venue createVenue(VenuesModelVenuesItem item) {
        try {
            Location location = new Location("reverseGeocoded");
            location.setLatitude(Double.parseDouble(item.getLat()));
            location.setLongitude(Double.parseDouble(item.getLng()));
            return new Venue(item.getVenueId(), item.getName(), item.getAddress(), item.getPicture(), location);
        } catch (Exception e) {
            Crashlytics.logException(e);
            return null;
        }
    }

    public static Menu createMenu(MenuModel model) {
        List<Category> categories = new ArrayList<>();
        for (MenuModelCategoriesItem categoryItem : model.getCategories()) {
            if (!categoryItem.getItems().isEmpty()) {
                categories.add(createCategory(categoryItem));
            }
        }
        return new Menu(categories);
    }

    private static Category createCategory(MenuModelCategoriesItem categoryItem) {
        List<MenuItem> menuItemList = new ArrayList<>();
        for (MenuModelCategoriesItemItemsItem item : categoryItem.getItems()) {
            MenuItem menuItem = createMenuItem(item);
            if (menuItem != null) {
                menuItemList.add(menuItem);
            }
        }
        return new Category(categoryItem.getCategoryId(), categoryItem.getName(), categoryItem.getDescription(), menuItemList);
    }

    private static MenuItem createMenuItem(MenuModelCategoriesItemItemsItem item) {
        try {
            Float amount = Float.parseFloat(item.getPrice());
            return new MenuItem(item.getItemId(), item.getName(), amount, item.getDescription());
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error with price on item", e);
            return null;
        }
    }
}

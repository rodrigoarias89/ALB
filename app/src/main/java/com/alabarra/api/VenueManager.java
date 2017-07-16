package com.alabarra.api;

import android.location.Location;
import android.support.annotation.Nullable;

import com.alabarra.model.Menu;
import com.alabarra.model.Venue;
import com.alabarra.model.VenueFactory;

import java.util.ArrayList;
import java.util.List;

import ar.com.alabarra.clientsdk.model.MenuModel;
import ar.com.alabarra.clientsdk.model.VenuesModel;
import ar.com.alabarra.clientsdk.model.VenuesModelVenuesItem;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueManager {

    private static VenueManager mInstance;

    public static VenueManager getInstance() {
        if (mInstance == null) {
            mInstance = new VenueManager();
        }
        return mInstance;
    }

    public void findVenuesNearbyAsync(Location location, @Nullable final BackgroundTaskListener<List<Venue>> listener) {
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        ApiHelper.getInstance().findBarsNearby(lat, lng, new ApiHelper.OnApiResponse<VenuesModel>() {
            @Override
            public void onSuccess(VenuesModel response) {
                if (listener != null) {
                    List<Venue> venues = new ArrayList<>();
                    for (VenuesModelVenuesItem item : response.getVenues()) {
                        venues.add(VenueFactory.createVenue(item));
                    }
                    listener.onSuccess(venues);
                }
            }

            @Override
            public void onFailed(Exception exception) {
                if (listener != null) {
                    listener.onFailed(exception);
                }
            }
        });
    }

    public void getVenueMenue(Venue venue, @Nullable final BackgroundTaskListener<Menu> listener) {
        ApiHelper.getInstance().getVenueMenu(venue.getId(), new ApiHelper.OnApiResponse<MenuModel>() {
            @Override
            public void onSuccess(MenuModel response) {
                if (listener != null) {
                    listener.onSuccess(VenueFactory.createMenu(response));
                }
            }

            @Override
            public void onFailed(Exception exception) {
                if (listener != null) {
                    listener.onFailed(exception);
                }
            }
        });
    }

}

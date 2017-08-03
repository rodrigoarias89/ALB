package com.alabarra.api;

import android.location.Location;
import android.support.annotation.Nullable;

import com.alabarra.model.Menu;
import com.alabarra.model.Venue;
import com.alabarra.model.VenueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.alabarra.clientsdk.model.MenuModel;
import ar.com.alabarra.clientsdk.model.VenuesModel;
import ar.com.alabarra.clientsdk.model.VenuesModelVenuesItem;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class VenueManager {

    private static VenueManager mInstance;

    private Map<String, Venue> mVenues;

    private VenueManager() {
        mVenues = new HashMap<>();
    }

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
                        if (mVenues.get(item.getVenueId()) == null) {
                            Venue venue = VenueFactory.createVenue(item);
                            if (venue != null) {
                                mVenues.put(item.getVenueId(), VenueFactory.createVenue(item));
                            } else {
                                continue;
                            }
                        }
                        venues.add(mVenues.get(item.getVenueId()));
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

    public void getVenueMenueAsync(final Venue venue, @Nullable final BackgroundTaskListener<Menu> listener) {
        ApiHelper.getInstance().getVenueMenu(venue.getId(), new ApiHelper.OnApiResponse<MenuModel>() {
            @Override
            public void onSuccess(MenuModel response) {
                Menu menu = VenueFactory.createMenu(response);
                venue.setMenu(menu);
                if (listener != null) {
                    listener.onSuccess(menu);
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

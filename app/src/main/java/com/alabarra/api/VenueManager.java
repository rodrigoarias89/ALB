package com.alabarra.api;

import android.location.Location;
import android.support.annotation.Nullable;

import com.alabarra.model.Venue;

import java.util.ArrayList;
import java.util.List;

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
                        Location location = new Location("reverseGeocoded");
                        location.setLatitude(Double.parseDouble(item.getLat()));
                        location.setLongitude(Double.parseDouble(item.getLng()));
                        venues.add(new Venue(item.getName(), item.getAddress(), item.getPicture(), location));
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

}

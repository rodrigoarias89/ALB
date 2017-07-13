package com.alabarra.gui.listeners;

import android.location.Location;

import com.alabarra.model.Venue;

import java.util.List;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public interface SearchInteracionListener {

    List<Venue> getFoundedVenues();

    Location getCurrentLocation();

}

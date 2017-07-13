package com.alabarra.gui.listeners;

import com.alabarra.model.Venue;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public interface NavigationInteractionListener {

    void onFindBars();

    void goToVenue(Venue venue);

}
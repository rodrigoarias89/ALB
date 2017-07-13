package com.alabarra.gui.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by rodrigoarias on 7/12/17.
 */

public class PositionUtils {

    private final static String METERS = "m";
    private final static String KILOMETERS = "km";

    private final static NumberFormat mKilometerFormat = initKilometerFormat();

    private static NumberFormat initKilometerFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("es", "ES"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat;
    }

    public static String getFormattedDistance(float distance) {
        if (distance < 100) {
            return (int) distance + " " + METERS;
        } else {
            return mKilometerFormat.format((distance / 1000)) + " " + KILOMETERS;
        }
    }
}

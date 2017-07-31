package com.alabarra.gui.utils;

/**
 * Created by rodrigoarias on 7/30/17.
 */

public class QuantityUtils {

    private final static String MULTIPLIER_SYMBOL = "x ";

    public static String getFormattedItemQuantity(int quantity) {
        if (quantity > 1) {
            return MULTIPLIER_SYMBOL + quantity;
        } else {
            return "";
        }
    }
}

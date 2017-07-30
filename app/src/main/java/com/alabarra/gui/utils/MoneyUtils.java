package com.alabarra.gui.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by rodrigoarias on 7/30/17.
 */

public class MoneyUtils {

    private static final String CURRENCY_SYMBOL = "$";
    private static final String CURRENCY_DIVIDER = ",";

    private static NumberFormat mNumberFormat = initNumberFormat(true);
    private static NumberFormat mNumberFormatWithoutDecimals = initNumberFormat(false);

    private static NumberFormat initNumberFormat(boolean withDecimals) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("es", "ES"));
        if (withDecimals) {
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(2);
        } else {
            numberFormat.setMaximumFractionDigits(0);
        }
        return numberFormat;
    }

    public static String getAmountWithCurrencySymbol(float value) {
        return CURRENCY_SYMBOL + mNumberFormat.format(value);
    }
}

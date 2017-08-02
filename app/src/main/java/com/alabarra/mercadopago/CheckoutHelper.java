package com.alabarra.mercadopago;

import android.app.Activity;

import com.alabarra.R;
import com.alabarra.model.Order;
import com.alabarra.model.Venue;
import com.mercadopago.constants.Sites;
import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.model.Item;
import com.mercadopago.preferences.CheckoutPreference;

import java.math.BigDecimal;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class CheckoutHelper {


    private Activity mActivity;

    private Venue mVenue;
    private Order mOrder;

    public CheckoutHelper(Activity activity, Venue venue, Order order) {
        mActivity = activity;
        mVenue = venue;
        mOrder = order;
    }

    public void startPayment() {
        CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
                .addItem(new Item(mVenue.getName(), new BigDecimal(mOrder.getOrderAmount())))
                .setSite(Sites.ARGENTINA)
                .build();
        startMercadoPagoCheckout(checkoutPreference);
    }

    private void startMercadoPagoCheckout(CheckoutPreference checkoutPreference) {
        new MercadoPagoCheckout.Builder()
                .setActivity(mActivity)
                .setPublicKey(mActivity.getString(R.string.mp_public_key))
                .setCheckoutPreference(checkoutPreference)
                .startForPaymentData();
    }
}

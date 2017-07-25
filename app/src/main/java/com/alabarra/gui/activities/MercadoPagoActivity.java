package com.alabarra.gui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.model.PaymentData;
import com.mercadopago.util.JsonUtil;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class MercadoPagoActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MercadoPagoCheckout.CHECKOUT_REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_DATA_RESULT_CODE) {
                PaymentData paymentData = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentData"), PaymentData.class);
                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getStringExtra("mercadoPagoError") != null) {
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }

}

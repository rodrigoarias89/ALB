package com.alabarra.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabarra.R;
import com.alabarra.gui.helper.CurrentOrderManager;
import com.alabarra.gui.list.order.OrderRecyclerView;
import com.alabarra.gui.listeners.OnMenuItemClickListener;
import com.alabarra.model.MenuItem;
import com.alabarra.model.Order;
import com.alabarra.model.Venue;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class CheckOrderFragment extends Fragment implements OnMenuItemClickListener, Order.OnOrderUpdateListener {

    public static final String TAG = "CheckOrderFragment";

    private OrderRecyclerView mRecyclerView;

    private Venue mVenue;
    private Order mOrder;
    private TextView mTotalAmount;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_order, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mVenue = CurrentOrderManager.getInstance().getVenue();
        mOrder = CurrentOrderManager.getInstance().getOrder();

        mTotalAmount = (TextView) view.findViewById(R.id.order_total_amount);
        showTotalAmount(mOrder.getOrderAmount());

        mRecyclerView = (OrderRecyclerView) view.findViewById(R.id.order_check_list);
        mRecyclerView.setOnMenuItemClickListener(this);
        mRecyclerView.setItems(mOrder.getItems());

        view.findViewById(R.id.confirm_order_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        view.findViewById(R.id.add_more_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mOrder.setOnOrderUpdateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mOrder.removeOnOrderUpdateListener();
    }


    @Override
    public void onMenuItemClick(MenuItem item) {
        mRecyclerView.removeItem(item);
        mOrder.removeMenuItem(item, 1);
    }

    @Override
    public void onOrderUpdate(float amount) {
        showTotalAmount(amount);
    }

    @Override
    public void onOrderEmpty() {
        getActivity().onBackPressed();
    }

    private void showTotalAmount(float amount) {
        mTotalAmount.setText("$" + amount);
    }
}

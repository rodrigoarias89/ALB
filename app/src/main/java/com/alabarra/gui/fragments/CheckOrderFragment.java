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
import com.alabarra.model.Order;
import com.alabarra.model.Venue;

/**
 * Created by rodrigoarias on 7/18/17.
 */

public class CheckOrderFragment extends Fragment {

    public static final String TAG = "CheckOrderFragment";

    private OrderRecyclerView mRecyclerView;

    private Venue mVenue;
    private Order mOrder;

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

        ((TextView) view.findViewById(R.id.order_total_amount)).setText("$" + mOrder.getOrderAmount());

        mRecyclerView = (OrderRecyclerView) view.findViewById(R.id.order_check_list);
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


}

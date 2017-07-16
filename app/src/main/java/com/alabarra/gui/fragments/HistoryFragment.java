package com.alabarra.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.list.history.HistoryRecyclerView;
import com.alabarra.model.DoneOrder;
import com.alabarra.model.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigoarias on 7/16/17.
 */

public class HistoryFragment extends Fragment {

    public final static String TAG = "HistoryFragment";

    private HistoryRecyclerView mRecyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (HistoryRecyclerView) view.findViewById(R.id.history_recycler_view);

        //TODO
        Venue venue1 = new Venue("1", "Rosebar", "als", "nsal", null);
        Venue venue2 = new Venue("2", "Temple Bar", "als", "nsal", null);
        Venue venue3 = new Venue("3", "Cervelar", "als", "nsal", null);
        List<DoneOrder> orderList = new ArrayList<>();
        orderList.add(new DoneOrder("1", "1200", "12/01/17", venue1));
        orderList.add(new DoneOrder("2", "350", "12/01/17", venue2));
        orderList.add(new DoneOrder("3", "700", "12/01/17", venue3));
        mRecyclerView.setOrders(orderList);

    }

}

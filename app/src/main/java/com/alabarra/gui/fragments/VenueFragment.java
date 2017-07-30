package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alabarra.R;
import com.alabarra.api.BackgroundTaskListener;
import com.alabarra.api.VenueManager;
import com.alabarra.gui.helper.CurrentOrderManager;
import com.alabarra.gui.list.menues.MenuRecyclerView;
import com.alabarra.gui.listeners.NavigationInteractionListener;
import com.alabarra.gui.listeners.OnMenuItemClickListener;
import com.alabarra.gui.listeners.SearchInteracionListener;
import com.alabarra.gui.utils.MoneyUtils;
import com.alabarra.gui.utils.PositionUtils;
import com.alabarra.model.Menu;
import com.alabarra.model.MenuItem;
import com.alabarra.model.Order;
import com.alabarra.model.Venue;
import com.bumptech.glide.Glide;

/**
 * Created by rodrigoarias on 7/12/17.
 */

public class VenueFragment extends Fragment implements OnMenuItemClickListener, Order.OnOrderUpdateListener {

    public static final String TAG = "VenueFragment";

    private Venue mVenue;
    private Menu mMenu;
    private Order mOrder;

    private MenuItem mCurrentItemDialog;

    private MenuRecyclerView mMenuRecyclerView;
    private View mProgress;
    private View mError;
    private Button mOrderButton;

    private AppCompatImageView mImage;
    private TextView mTitle;
    private TextView mAddress;
    private TextView mDistance;

    private SearchInteracionListener mSearchListener;
    private NavigationInteractionListener mNavigationListener;


    //For API pre 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            initFragment(activity);
        }
    }

    //Not called in API pre 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initFragment(context);
    }

    private void initFragment(Context context) {
        mNavigationListener = (NavigationInteractionListener) context;
        mSearchListener = (SearchInteracionListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_venue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mTitle = (TextView) view.findViewById(R.id.venue_title);
        mAddress = (TextView) view.findViewById(R.id.venue_address);
        mImage = (AppCompatImageView) view.findViewById(R.id.venue_image);
        mDistance = (TextView) view.findViewById(R.id.venue_distance);

        mProgress = view.findViewById(R.id.venue_progress);
        mError = view.findViewById(R.id.venue_progress_error);
        mOrderButton = (Button) view.findViewById(R.id.order_button);
        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOrderCheck();
            }
        });

        mMenuRecyclerView = (MenuRecyclerView) view.findViewById(R.id.venue_menu);
        mMenuRecyclerView.setOnMenuItemClickListener(this);

        mVenue = CurrentOrderManager.getInstance().getVenue();
        if (CurrentOrderManager.getInstance().getOrder() != null) {
            mOrder = CurrentOrderManager.getInstance().getOrder();
            onOrderUpdate(mOrder.getOrderAmount());
        } else {
            mOrder = new Order(mVenue);
        }

        initVenueInfo();
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

    private void initVenueInfo() {
        if (mVenue != null) {
            mTitle.setText(mVenue.getName().toUpperCase());
            mAddress.setText(mVenue.getAddress());

            mDistance.setText(PositionUtils.getFormattedDistance(
                    mSearchListener.getCurrentLocation().distanceTo(mVenue.getLocation())));

            Glide.with(getActivity()).load(mVenue.getPicture()).into(mImage);
            getVenueMenu();
        }
    }

    private void goToOrderCheck() {
        CurrentOrderManager.getInstance().setNewOrder(mVenue, mOrder);
        mNavigationListener.goToCheckOrder();
    }

    private void getVenueMenu() {

        if (mVenue.getMenu() != null) {
            onMenu(mVenue.getMenu());
        } else {
            VenueManager.getInstance().getVenueMenueAsync(mVenue, new BackgroundTaskListener<Menu>() {
                @Override
                public void onSuccess(Menu object) {
                    onMenu(object);
                }

                @Override
                public void onFailed(Exception e) {
                    showOnFailed();
                }
            });
        }
    }

    private void onMenu(Menu menu) {
        mMenu = menu;
        showMenu();
    }

    private void showMenu() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgress.setVisibility(View.GONE);
                    mMenuRecyclerView.setMenu(mMenu);
                }
            });
        }
    }

    private void showOnFailed() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgress.setVisibility(View.GONE);
                    mError.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onMenuItemClick(MenuItem item) {

        mCurrentItemDialog = item;

        DialogFragment dialog = new AddItemDialogFragment();
        dialog.setTargetFragment(this, AddItemDialogFragment.DIALOG_RESPONSE);

        Bundle bundle = new Bundle();
        bundle.putString(AddItemDialogFragment.MENU_ITEM_TITLE_ARG, item.getName());
        bundle.putString(AddItemDialogFragment.MENU_ITEM_DESC_ARG, item.getDescription());
        dialog.setArguments(bundle);


        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, AddItemDialogFragment.TAG);

    }

    private void setOrderButtonText(Float amount) {
        mOrderButton.setText(String.format(getString(R.string.check_order), MoneyUtils.getAmountWithCurrencySymbol(amount)));
    }

    @Override
    public void onOrderUpdate(float amount) {
        if (amount > 0) {
            setOrderButtonText(amount);
            if (mOrderButton.getVisibility() == View.GONE) {
                mOrderButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onOrderEmpty() {
        if (mOrderButton.getVisibility() == View.VISIBLE) {
            mOrderButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AddItemDialogFragment.DIALOG_RESPONSE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getExtras().containsKey(AddItemDialogFragment.DIALOG_RESPONSE_QUANTITY)) {
                    int quant = data.getExtras().getInt(AddItemDialogFragment.DIALOG_RESPONSE_QUANTITY);

                    if (mCurrentItemDialog != null) {
                        mOrder.addMenuItem(mCurrentItemDialog, quant);
                    }
                }
            }
        }

        mCurrentItemDialog = null;
    }
}

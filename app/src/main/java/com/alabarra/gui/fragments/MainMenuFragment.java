package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.components.AccentTextView;
import com.alabarra.gui.helper.IdentityHelper;
import com.alabarra.gui.helper.ShareHelper;
import com.alabarra.gui.listeners.NavigationInteractionListener;

/**
 * Created by rodrigoarias on 7/10/17.
 */

public class MainMenuFragment extends Fragment {

    private NavigationInteractionListener mListener;

    //For API pre 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        mListener = (NavigationInteractionListener) context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccentTextView accentTextView = (AccentTextView) view.findViewById(R.id.menu_accent_text_view);
        String name = (IdentityHelper.getInstance().getFirstName() + ",").toUpperCase();
        String welcome = String.format(getString(R.string.welcome_title), name);
        accentTextView.setTextWithAccentColor(welcome, name);

        view.findViewById(R.id.bars_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFindBars();
            }
        });


        view.findViewById(R.id.invite_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInvite();
            }
        });
    }

    private void onInvite() {
        ShareHelper.share(getActivity());
    }
}

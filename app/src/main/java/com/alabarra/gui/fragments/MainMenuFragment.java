package com.alabarra.gui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.components.AccentTextView;
import com.alabarra.gui.helper.IdentityHelper;

/**
 * Created by rodrigoarias on 7/10/17.
 */

public class MainMenuFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccentTextView accentTextView = (AccentTextView) view.findViewById(R.id.menu_accent_text_view);
        String name = (IdentityHelper.getInstance().getFirstName() + ",").toLowerCase();
        String welcome = String.format(getString(R.string.welcome_title), name);
        accentTextView.setTextWithAccentColor(welcome, name);
    }
}

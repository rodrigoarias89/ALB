package com.alabarra.gui.fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.helper.IdentityHelper;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rodrigoarias on 7/10/17.
 */

public class ProfileFragment extends DialogFragment {

    public final static String TAG = "Profile";

    private IdentityHelper identityHelper;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container,
                false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        identityHelper = IdentityHelper.getInstance();

        CircleImageView profileImage = (CircleImageView) view.findViewById(R.id.profile_pic);


        profileImage.setImageBitmap(identityHelper.getUserImage());


        view.findViewById(R.id.profile_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The user is currently signed in with a provider. Sign out of that provider.
                identityHelper.signOut(getActivity());
                return;
            }
        });


    }

}

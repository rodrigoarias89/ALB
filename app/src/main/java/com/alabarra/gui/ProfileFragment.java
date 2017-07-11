package com.alabarra.gui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alabarra.R;
import com.alabarra.SignInHandler;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobilehelper.auth.IdentityManager;
import com.amazonaws.mobilehelper.auth.user.IdentityProfile;

/**
 * Created by rodrigoarias on 7/10/17.
 */

public class ProfileFragment extends Fragment {

    private IdentityManager identityManager;
    private IdentityProfile identityProfile;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        identityManager = AWSMobileClient.defaultMobileClient().getIdentityManager();
        identityProfile = identityManager.getIdentityProfile();

        ImageView profileImage = (ImageView) view.findViewById(R.id.profile_pic);
        TextView usernameText = (TextView) view.findViewById(R.id.profile_username);


        if(identityProfile != null) {
            usernameText.setText(identityProfile.getUserName());
            profileImage.setImageBitmap(identityProfile.getUserImage());
        }


        view.findViewById(R.id.profile_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The user is currently signed in with a provider. Sign out of that provider.
                identityManager.signOut();
                identityManager.signInOrSignUp(getActivity(), new SignInHandler());
                getActivity().finish();
                return;
            }
        });


    }

}

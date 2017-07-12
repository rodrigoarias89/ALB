package com.alabarra.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.alabarra.R;
import com.alabarra.api.ApiHelper;
import com.alabarra.api.BackgroundTaskListener;
import com.alabarra.api.UserManager;
import com.alabarra.gui.activities.MainActivity;
import com.alabarra.gui.helper.IdentityHelper;

/**
 * Created by rodrigoarias on 7/11/17.
 */

public class NavigationManager {

    private static final String TAG = "NavigationManager";

    public static void initMain(final Activity parentActivity) {
        ApiHelper.init(IdentityHelper.getInstance().getProvider());

        IdentityHelper identityHelper = IdentityHelper.getInstance();
        UserManager.getInstance().signUpOrSignInAsync(identityHelper.getEmail(), identityHelper.getName(), identityHelper.getUsername(), new BackgroundTaskListener<Void>() {
            @Override
            public void onSuccess(Void object) {
                parentActivity.startActivity(new Intent(parentActivity, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                parentActivity.finish();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Exception on signing up", e);
                new AlertDialog.Builder(parentActivity).setMessage(R.string.unknown_error).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        parentActivity.finish();
                    }
                }).show();
            }
        });


    }
}

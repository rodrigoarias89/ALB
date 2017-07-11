package com.alabarra.gui.helper;

import android.app.Activity;
import android.content.Intent;

import com.alabarra.R;


/**
 * Created by rodrigoarias on 3/1/17.
 */

public class ShareHelper {

    public static void share(Activity parentActivity) {
        String invitationString = parentActivity.getString(R.string.invite_to_alabarra);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, invitationString);
        sendIntent.setType("text/plain");
        parentActivity.startActivity(Intent.createChooser(sendIntent, invitationString));
    }
}

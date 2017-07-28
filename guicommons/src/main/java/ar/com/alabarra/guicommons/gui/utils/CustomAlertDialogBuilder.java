package ar.com.alabarra.guicommons.gui.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

/**
 * Created by rodrigoarias on 11/22/16.
 */

public class CustomAlertDialogBuilder extends AlertDialog.Builder {

    public CustomAlertDialogBuilder(Context context) {
        super(context);
    }

    @Override
    public CustomAlertDialogBuilder setView(View view) {
        return (CustomAlertDialogBuilder) super.setView(view);
    }

    public Dialog createProgressDialog() {
        Dialog alertDialog = new Dialog(getContext());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ProgressBar progressBar = new ProgressBar(getContext());
        alertDialog.setContentView(progressBar);

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}

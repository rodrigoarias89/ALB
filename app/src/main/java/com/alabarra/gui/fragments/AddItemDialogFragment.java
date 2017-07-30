package com.alabarra.gui.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 7/30/17.
 */

public class AddItemDialogFragment extends DialogFragment {

    public static final String TAG = "AddItemDialogFragment";

    public static final String MENU_ITEM_TITLE_ARG = "MENU_ITEM_TITLE_ARG";
    public static final String MENU_ITEM_DESC_ARG = "MENU_ITEM_DESC_ARG";

    public static final int DIALOG_RESPONSE = 1001;
    public static final String DIALOG_RESPONSE_QUANTITY = "DIALOG_RESPONSE_QUANTITY";

    private TextView mQuantityText;

    private int mQuantity = 1;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_item, container,
                false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String title = getArguments().getString(MENU_ITEM_TITLE_ARG);
        String description = getArguments().getString(MENU_ITEM_DESC_ARG);


        ((TextView) view.findViewById(R.id.add_item_title)).setText(title);
        ((TextView) view.findViewById(R.id.add_item_desc)).setText(description);

        mQuantityText = (TextView) view.findViewById(R.id.add_item_quantity);
        setQuantityText();

        view.findViewById(R.id.add_item_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mQuantity < 100) {
                    mQuantity++;
                    setQuantityText();
                }
            }
        });

        view.findViewById(R.id.add_item_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mQuantity > 1) {
                    mQuantity--;
                    setQuantityText();
                }
            }
        });

        view.findViewById(R.id.add_item_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                dismiss();
            }
        });

        view.findViewById(R.id.add_item_dialog_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().putExtra(DIALOG_RESPONSE_QUANTITY, mQuantity);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });
    }

    private void setQuantityText() {
        mQuantityText.setText("" + mQuantity);
    }
}

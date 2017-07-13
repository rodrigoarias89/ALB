package com.alabarra.gui.components;

import android.content.Context;

import com.alabarra.R;

/**
 * Created by rodrigoarias on 5/22/17.
 */

public class CustomDividerItemDecoration extends android.support.v7.widget.DividerItemDecoration {

    /**
     * Creates a divider {@link android.support.v7.widget.RecyclerView.ItemDecoration} that can be used with a
     * {@link android.support.v7.widget.LinearLayoutManager}.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public CustomDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            setDrawable(context.getDrawable(R.drawable.list_divider));
        } else {
            setDrawable(context.getResources().getDrawable(R.drawable.list_divider));
        }
    }
}

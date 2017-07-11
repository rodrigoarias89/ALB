package com.alabarra.gui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;

import com.alabarra.R;


/**
 * Created by rodrigoarias on 11/21/16.
 */

public class AccentTextView extends AppCompatTextView {

    private final static String TAG = "AccentTextView";

    public AccentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AccentTextView,
                0, 0);


        String accentString = "";
        try {
            accentString = a.getString(R.styleable.AccentTextView_accentText);
            if (accentString != null && !accentString.isEmpty()) {
                setTextWithAccentColor((String) getText(), accentString);
            }
        } finally {
            a.recycle();
        }
    }


    public void setTextWithAccentColor(String text, String... accentWords) {
        setAccentColorWords(text, accentWords);
    }

    private void setAccentColorWords(String text, String... boldWords) {
        try {
            SpannableString ss = new SpannableString(text);

            for (String word : boldWords) {
                int index = text.indexOf(word);
                if (index > -1) {
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorAccent)), index, index + word.length(), 0);
                }
            }

            setText(ss);

        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Error on styling", e);
        }
    }
}

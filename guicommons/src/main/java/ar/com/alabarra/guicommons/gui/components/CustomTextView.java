package ar.com.alabarra.guicommons.gui.components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by rodrigoarias on 11/16/16.
 */

public class CustomTextView extends AppCompatTextView {

    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";
    private int textStyle = 0;

    public CustomTextView(Context context) {
        super(context);
        applyCustomFont();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(attrs);
    }

    private void applyCustomFont(AttributeSet attrs) {
        textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
        applyCustomFont();
    }

    private void applyCustomFont() {
        Typeface customFont = selectTypeface(textStyle);
        setTypeface(customFont);
    }

    private Typeface selectTypeface(int textStyle) {
        switch (textStyle) {
            case Typeface.BOLD:
                return FontCache.getTypeface("fonts/Montserrat-Bold.ttf", getContext());
            case Typeface.NORMAL:
            default:
                return FontCache.getTypeface("fonts/Montserrat-Regular.ttf", getContext());
        }
    }
}

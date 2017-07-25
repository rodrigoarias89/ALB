package ar.com.alabarra.guicommons.gui.components;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by rodrigoarias on 11/16/16.
 */

public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontCache.get(fontName);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontName, typeface);
        }

        return typeface;
    }

}

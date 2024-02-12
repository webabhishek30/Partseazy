package com.partseazy.android.utility.fonts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.partseazy.android.Logger.CustomLogger;

import java.io.File;

/**
 * Created by gaurav on 24/12/16.
 *  * credits: sloop
 */

public class FontUtiity {

    public static final String INIT_EXCEPTION = "FontsManagerException";
    public static Typeface defaultTypeface = null;
    public static Typeface defaultBoldTypeface = null;

    public static Typeface getDefaultTypeface() {
        return defaultTypeface;
    }

    private FontUtiity() {
    }

    /**
     * @param typeface
     * @Title: init
     */
    public static void init(Typeface typeface, Typeface typeFaceBold) {
        if (typeface == null) {
            CustomLogger.e("typeface is null");
            throw new IllegalStateException("typeface name");
        } else {
            defaultTypeface = typeface;
            defaultBoldTypeface = typeFaceBold;
        }
    }

    /**
     * @param context
     * @param fontPath Assets path of font
     * @Title: initFormAssets
     */
    public static void initFormAssets(Context context, String fontPath) {
        try {
            defaultTypeface = Typeface.createFromAsset(context.getAssets(), fontPath);
        } catch (Exception e) {
            CustomLogger.e("Font error");
            throw new IllegalStateException("font exception");
        }
    }

    /**
     * Use of this method is discouraged for performance reasons. you should load fonts from assets instead.
     *
     * @param fontPath path from external memory (sdcard/font.ttf）
     * @Title: initFormFile
     */
    public static void initFormFile(String fontPath) {
        try {
            defaultTypeface = Typeface.createFromFile(fontPath);
        } catch (Exception e) {
            CustomLogger.e("Font error from file");
            throw new IllegalStateException("font exception");
        }
    }

    /**
     * Use of this method is discouraged for performance reasons. you should load fonts from assets instead.
     *
     * @param fontFile path of font file
     * @Title: initFormFile
     */
    public static void initFormFile(File fontFile) {
        try {
            defaultTypeface = Typeface.createFromFile(fontFile);
        } catch (Exception e) {
            CustomLogger.e("Font error from FIle");
            throw new IllegalStateException("font exception");
        }
    }

    /**
     * @param activity
     * @Title: changeFonts
     */
    public static void changeFonts(Activity activity) {
        if (defaultTypeface == null) {
            CustomLogger.e(INIT_EXCEPTION);
            throw new IllegalStateException(INIT_EXCEPTION);
        }
        ToolBarHelper.changeTitleFonts(activity, defaultTypeface);
        changeFonts((ViewGroup) activity.findViewById(android.R.id.content), defaultTypeface);
    }

    /**
     * @param view
     * @Title: changeFonts
     */
    public static void changeFonts(View view) {
        if (defaultTypeface == null) {
            CustomLogger.e(INIT_EXCEPTION);
            throw new IllegalStateException(INIT_EXCEPTION);
        }
        changeFonts(view, defaultTypeface);
    }

    /**
     * @param viewGroup
     * @Title: changeFonts
     */
    public static void changeFonts(ViewGroup viewGroup) {
        if (defaultTypeface == null) {
            CustomLogger.e(INIT_EXCEPTION);
            throw new IllegalStateException(INIT_EXCEPTION);
        }
        changeFonts(viewGroup, defaultTypeface);
    }

    /**
     * @param viewGroup
     * @param typeface
     * @Title: changeFonts
     */
    public static void changeFonts(ViewGroup viewGroup, Typeface typeface) {
        try {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                changeFonts(v, typeface);
            }
        } catch (Exception e) {
            CustomLogger.e(e.toString());
            // TODO
        }
    }

    /**
     * @param view
     * @param typeface void
     * @Title: changeFonts
     */
    public static void changeFonts(View view, Typeface typeface) {
        try {
            if (view instanceof ViewGroup) {
                changeFonts((ViewGroup) view, typeface);
            } else if (view instanceof TextView) {
                if (((TextView) view).getTypeface() != null && ((TextView) view).getTypeface().isBold() && defaultBoldTypeface != null) {
                    ((TextView) view).setTypeface(defaultBoldTypeface);
                } else
                    ((TextView) view).setTypeface(typeface);
            } else if (view instanceof Button) {
                if (((Button) view).getTypeface() != null && ((Button) view).getTypeface().isBold() && defaultBoldTypeface != null) {
                    ((Button) view).setTypeface(defaultBoldTypeface);
                } else
                    ((Button) view).setTypeface(typeface);
            } else if (view instanceof Switch) {
                if (((Switch) view).getTypeface() != null && ((Switch) view).getTypeface().isBold() && defaultBoldTypeface != null) {
                    ((Switch) view).setTypeface(defaultBoldTypeface);
                } else
                    ((Switch) view).setTypeface(typeface);
            } else if (view instanceof EditText) {
                if (((EditText) view).getTypeface() != null && ((EditText) view).getTypeface().isBold() && defaultBoldTypeface != null) {
                    ((EditText) view).setTypeface(defaultBoldTypeface);
                } else
                    ((EditText) view).setTypeface(typeface);
            }
        } catch (Exception e) {
            CustomLogger.e(e.toString());
        }

    }
}

package com.partseazy.android.utility.fonts;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Log;

import com.partseazy.android.Logger.CustomLogger;

/**
 * Created by gaurav on 24/12/16.
 */

public class ToolBarHelper {

    private static final String TAG = "ActioHelper";
    public static final String INIT_EXCEPTION = "AcHelperExp";

    public static void changeActionBarFonts(android.support.v7.app.ActionBar actionBar, Typeface typeface) {
        if (typeface == null) {
            return;
        }
        try {
            if(actionBar != null)
            setTitle(actionBar, typeface, actionBar.getTitle().toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    /**
     * @param activity
     * @param typeface
     * @Title: changeTitleFonts
     */
    public static void changeTitleFonts(Activity activity, Typeface typeface) {
        if (typeface == null || activity == null) {
            return;
        }
        if (activity instanceof AppCompatActivity) {
            try {
                android.support.v7.app.ActionBar actionBar = ((AppCompatActivity ) activity).getSupportActionBar();
                if(actionBar != null)
                    setTitle(actionBar, typeface, actionBar.getTitle().toString());
            } catch (Exception e) {
                CustomLogger.e(e.toString());
            }

        } else if (activity instanceof Activity) {
            try {
                ActionBar actionBar = activity.getActionBar();
                if(actionBar != null)
                    setTitle(actionBar, typeface, actionBar.getTitle().toString());
            } catch (Exception e) {
                CustomLogger.e(e.toString());
            }
        }
    }

    public static void setTitle(Activity activity, Typeface typeface, String title) {
        if (activity instanceof android.support.v7.app.AppCompatActivity ) {
            try {
                android.support.v7.app.ActionBar actionBar = ((AppCompatActivity ) activity).getSupportActionBar();
                setTitle(actionBar, typeface, title);
            } catch (Exception e) {
                CustomLogger.e(e.toString());
            }

        } else if (activity instanceof Activity) {
            try {
                ActionBar actionBar = activity.getActionBar();
                setTitle(actionBar, typeface, title);
            } catch (Exception e) {
                CustomLogger.e(e.toString());
            }
        }
    }

    public static void setTitle(android.support.v7.app.ActionBar actionBar, Typeface typeface, String title) {
        if (typeface == null || actionBar == null) {
            CustomLogger.d("typefacactionbar");

            return;
        }
        SpannableString sp = new SpannableString(title);
        sp.setSpan(new TypefaceSpan(typeface), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(actionBar, sp);
    }

    public static void setTitle(ActionBar actionBar, Typeface typeface, String title) {
        if (typeface == null || actionBar == null) {
            return;
        }
        SpannableString sp = new SpannableString(title);
        sp.setSpan(new TypefaceSpan(typeface), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(actionBar, sp);
    }

    /**
     * @param actionBar
     * @param spannableString
     * @Title: setTitle
     */
    public static void setTitle(android.support.v7.app.ActionBar actionBar, SpannableString spannableString) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN && Build.MANUFACTURER.toUpperCase().equals("LGE")) {
            actionBar.setTitle(spannableString.toString());
        } else {
            actionBar.setTitle(spannableString);
        }
    }


    /**
     * @param actionBar
     * @param spannableString
     * @Title: setTitle
     */
    @TargetApi(11)
    public static void setTitle(ActionBar actionBar, SpannableString spannableString) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN && Build.MANUFACTURER.toUpperCase().equals("LGE")) {
            actionBar.setTitle(spannableString.toString());
        } else {
            actionBar.setTitle(spannableString);
        }
    }


    /**
     * @ClassName: TypefaceSpan
     * @author: sloop
     */
    private static class TypefaceSpan extends MetricAffectingSpan {

        Typeface typeface;

        TypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(typeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(typeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}

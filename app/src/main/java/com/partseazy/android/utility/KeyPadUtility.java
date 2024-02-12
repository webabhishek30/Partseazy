package com.partseazy.android.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Pumpkin Guy on 05/12/16.
 */

public class KeyPadUtility {

    /**
     * shows soft keypad on screen
     *
     * @param activity
     * @param view
     */
    public static void showSoftKeypad(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * hides the soft keypad from screen
     *
     * @param pActivity
     */
    public static void hideSoftKeypad(Activity pActivity) {

        if (pActivity != null && pActivity.getWindow() != null
                && pActivity.getWindow().getCurrentFocus() != null) {
            hideSoftKeypad(pActivity, pActivity.getWindow().getCurrentFocus());
        }
    }

    /**
     * hides the soft keypad from screen
     *
     * @param pActivity
     * @param view
     */
    public static void hideSoftKeypad(Activity pActivity, View view) {
        InputMethodManager imm = (InputMethodManager) pActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}


package com.partseazy.android.utility.dialog;

import android.content.DialogInterface;

/**
 * Created by gaurav on 24/12/16.
 */


public abstract class DialogListener {
    public abstract void onPositiveButton(DialogInterface dialog);

    public void onNegativeButton(DialogInterface dialog) {

    }

    public void onNeutralButton(DialogInterface dialog) {

    }

    void onCancel() {
            /*dialog is cancelled explicitly or implicitly  by user by backpress or touching outer area.
            * you may override this in child if required*/
    }
}
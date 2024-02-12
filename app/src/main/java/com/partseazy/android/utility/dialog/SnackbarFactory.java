package com.partseazy.android.utility.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.utility.CommonUtility;

/**
 * Created by Pumpkin Guy on 05/12/16.
 */

public class SnackbarFactory {

    public interface SnackBarButtonListener {
        public void onButtonClick();
    }

    /**
     * Get done button callback
     *
     * @param act       - activity to the snackbar into
     * @param message   -message to show
     * @param buttonTxt - button name to show on right
     * @param listener  - right button callback listener
     */
    public static void showSnackbar(Activity act, String message, String buttonTxt, final SnackBarButtonListener listener) {
        final Snackbar snackbar = Snackbar.make(act.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbar.setAction(buttonTxt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onButtonClick();
                snackbar.dismiss();
            }
        })
                .setActionTextColor(snackbarView.getContext().getResources().getColor(R.color.colorOrange));


        snackbarView.setBackgroundColor(snackbarView.getContext().getResources().getColor(R.color.colorPrimary));
        //snackbarView.setBackgroundColor(ContextCompat.getColor(snackbarView.getContext(), R.color.colorPrimary));
        snackbar.show();
    }


    /**
     * Get done button callback
     *
     * @param act       - activity to the snackbar into
     * @param message   -message to show
     * @param buttonTxt - button name to show on right
     * @param duration  - if 0 show Snackbar for default time
     *                  if >=1 show Snackbar for indefinite time
     * @param listener  - right button callback listener
     */
    public static Snackbar showSnackbar(Activity act, String message, int duration, String buttonTxt, final SnackBarButtonListener listener) {
        if (duration >= 1)
            duration = (Snackbar.LENGTH_INDEFINITE);
        else
            duration = Snackbar.LENGTH_LONG;
        final Snackbar snackbar = Snackbar.make(act.findViewById(android.R.id.content), message, duration);
        snackbar.setAction(buttonTxt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onButtonClick();
                snackbar.dismiss();
            }
        })
                .setActionTextColor(Color.WHITE);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(snackbarView.getContext().getResources().getColor(R.color.black));
        //snackbarView.setBackgroundColor(ContextCompat.getColor(snackbarView.getContext(), R.color.colorPrimary));
        snackbar.show();
        return snackbar;
    }

    public static void showSnackbar(Activity act, String message) {
        Snackbar snackbar = Snackbar.make(act.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(group.getContext().getResources().getColor(R.color.black));
        //group.setBackgroundColor(ContextCompat.getColor(act.getApplicationContext(), R.color.primary));
        snackbar.show();
    }

    /**
     * Show Snackbar with default or INDEFINITE length
     *
     * @param act      - activity to the snackbar into
     * @param message  -message to show
     * @param duration - if 0 show Snackbar for default time
     *                 if >=1 show Snackbar for indefinite time
     */
    public static void showSnackbar(Activity act, String message, int duration) {
        if (duration >= 1)
            duration = (Snackbar.LENGTH_INDEFINITE);
        else
            duration = Snackbar.LENGTH_LONG;
        Snackbar snackbar = Snackbar.make(act.findViewById(android.R.id.content), message, duration);
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(group.getContext().getResources().getColor(R.color.black));
        //group.setBackgroundColor(ContextCompat.getColor(act.getApplicationContext(), R.color.primary));
        snackbar.show();
    }

    public static void showSnackbarFromTop(Activity act, String message) {
        Snackbar snack = Snackbar.make(act.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        view.setBackgroundColor(view.getContext().getResources().getColor(R.color.black));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }

    public static void showNoConnectionSnackBar(final Activity act, String message, String buttonTxt) {
      /*  final Snackbar snackbar = Snackbar.make(act.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(buttonTxt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String action = Settings.ACTION_WIFI_SETTINGS;
                if (CommonUtility.isConnectedMobile(act.getApplicationContext()))
                    action = Settings.ACTION_NETWORK_OPERATOR_SETTINGS;
                act.startActivity(new Intent(action));
                snackbar.dismiss();
            }
        })
                .setActionTextColor(Color.WHITE);
//        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(snackbarView.getContext().getResources().getColor(R.color.colorPrimary));
        //snackbarView.setBackgroundColor(ContextCompat.getColor(snackbarView.getContext(), R.color.primary));
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        //textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic,0, 0, 0);
        snackbar.show();*/
        final Dialog dialog=new Dialog(act,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_no_network_found);
        dialog.findViewById(R.id.btn_retry_no_internet_connection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showGPSDisabledSnackBar(final Activity act, String message, String buttonTxt) {
        final Snackbar snackbar = Snackbar.make(act.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(buttonTxt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        })
                .setActionTextColor(Color.WHITE);
//        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(snackbarView.getContext().getResources().getColor(R.color.colorPrimary));
        //snackbarView.setBackgroundColor(ContextCompat.getColor(snackbarView.getContext(), R.color.primary));
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        //textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic,0, 0, 0);
        snackbar.show();
    }
}
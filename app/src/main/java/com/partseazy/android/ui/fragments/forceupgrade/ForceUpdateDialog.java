package com.partseazy.android.ui.fragments.forceupgrade;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.partseazy.android.R;

/**
 * Created by gaurav on 25/02/17.
 */

public class ForceUpdateDialog implements View.OnClickListener,
        DialogInterface.OnDismissListener,
        DialogInterface.OnCancelListener {

    private static ForceUpdateDialog dialogInstance;
    private static OnForceUpdateClickListener mOnForceUpdateClickListener;


    private Dialog dialog;
    private Activity mActivity;


    public ForceUpdateDialog(OnForceUpdateClickListener onForceUpdateClickListener) {
        mOnForceUpdateClickListener = onForceUpdateClickListener;

    }

    public void showUpgradeDialog(final Activity activity,
                                  String headerMsg, boolean forced) {
        if (dialog == null) {
            mActivity = activity;
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.force_update);
            Button remindLater = (Button) dialog
                    .findViewById(R.id.txv_remind_me);

            Button upgradeNow = (Button) dialog
                    .findViewById(R.id.txv_update_now);
            upgradeNow.setOnClickListener(this);

            remindLater.setOnClickListener(this);
            if (forced) {
                remindLater.setVisibility(View.GONE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 2.0f);
                upgradeNow.setLayoutParams(param);
            } else {
                remindLater.setVisibility(View.VISIBLE);
            }


            // upgrade_image.setOnClickListener(this);
            dialog.setOnDismissListener(this);
            dialog.setOnCancelListener(this);
            if (forced) {
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mActivity.finish();

                    }
                });
            }
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txv_update_now) {
            Context context = v.getContext();
            String appPackageName = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://play.google.com/store/apps/details?id="
                                + appPackageName)));
            }
            dialog.dismiss();
            mActivity.finish();
        } else if (v.getId() == R.id.txv_remind_me) {
            dialog.dismiss();
            mOnForceUpdateClickListener.shouldGoFurther(true);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        this.dialog = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    public boolean isVisible() {
        return dialog != null;
    }

    public interface OnForceUpdateClickListener {

        public void shouldGoFurther(boolean shouldGo);
    }


}


package com.partseazy.android.ui.fragments.forceupgrade;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.utility.CommonUtility;

/**
 * Created by naveen on 14/7/17.
 */

public class AppExitDailog implements View.OnClickListener,
        DialogInterface.OnDismissListener,
        DialogInterface.OnCancelListener {


    private Dialog dialog;
    private Activity mActivity;
    private Context mcontext;

    public AppExitDailog() {

    }



    public void showUpgradeDialog(final Context context) {
        if (dialog == null) {
            mcontext = context;
            mActivity = (Activity) context;
            dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dailog_app_exit);
            Button exitBT = (Button) dialog.findViewById(R.id.exitBT);
            TextView headingtV = (TextView) dialog.findViewById(R.id.headingTV);

            TextView descTV = (TextView)dialog.findViewById(R.id.descTV);

            headingtV.setText(context.getString(R.string.account_blocked));
           // descTv.setText(Html.fromHtml(context.getString(R.string.app_block_message)));

            String messageString = context.getString(R.string.app_block_message)+" "+ StaticMap.CC_PHONE;

            SpannableString ss = new SpannableString(messageString);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    CommonUtility.dialPhoneNumber(context,StaticMap.CC_PHONE);
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    int linkColor = ContextCompat.getColor(context, R.color.green_checkout);
                    ds.setColor(linkColor);
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan, messageString.length()-11, messageString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            descTV.setText(ss);
            descTV.setMovementMethod(LinkMovementMethod.getInstance());
            exitBT.setOnClickListener(this);
            dialog.setOnDismissListener(this);
            dialog.setOnCancelListener(this);
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exitBT) {
            CommonUtility.exitApp(mActivity);

        }
        //else if (v.getId() == R.id.txv_remind_me) {
//            dialog.dismiss();
//            mOnForceUpdateClickListener.shouldGoFurther(true);
//        }
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


}



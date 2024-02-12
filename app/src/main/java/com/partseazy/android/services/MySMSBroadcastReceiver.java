package com.partseazy.android.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * you may not use this file except in compliance with the License.
 * Created by tHe Avadhesh  on 30-09-2019.
 */


public class MySMSBroadcastReceiver extends BroadcastReceiver {

    static onSMSRetrieved onSMSRetrieved;
    public static void registerCallback(onSMSRetrieved _onSMSRetrieved) {
        onSMSRetrieved=_onSMSRetrieved;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Pattern pattern = Pattern.compile("(\\d{4})");
                    Matcher matcher = pattern.matcher(message);
                    String val = "";
                    if (matcher.find()) {
                        System.out.println(matcher.group(1));

                        val = matcher.group(1);
                    }
                    Log.d("OTP_","message::"+val);
                    if(onSMSRetrieved!=null)
                    {
                        onSMSRetrieved.onSMSRetrieved(val);
                    }

                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }

    public interface onSMSRetrieved{
        public void onSMSRetrieved(String otp);
    }
}

package com.partseazy.android.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by naveen on 13/6/17.
 */

public class WakefulReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the Intent to deliver to our service.
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            Intent service = new Intent(context, UserActiveService.class);
//            context.startService(service);
        }
    }
}

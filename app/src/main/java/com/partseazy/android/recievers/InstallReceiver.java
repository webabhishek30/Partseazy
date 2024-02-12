package com.partseazy.android.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.partseazy.android.datastore.DataStore;

/**
 * Created by shubhang on 16/06/17.
 */

public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // called in the install referrer broadcast case
        String rawReferrerString = intent.getStringExtra("referrer");

        if (rawReferrerString != null && !"".equals(rawReferrerString)) {
            String[] separated = rawReferrerString.split(",");
            for (int i = 0; i < separated.length; i++) {
                if (i % 2 == 0) {
                    switch (separated[i]) {
                        case "inv":
                            DataStore.setInvitationCode(context, separated[i + 1]);
                            break;
                        case "product":
                            DataStore.setProductId(context, separated[i + 1]);
                            break;
                        case "deal":
                            DataStore.setDealId(context, separated[i + 1]);
                            break;
                        case "deals":
                            DataStore.setGoToDeals(context, "1");
                            break;
                        case "supp":
                            DataStore.setSupplierId(context, separated[i + 1]);
                            break;
                        case "chat":
                            DataStore.setGoToChat(context, true);
                            break;
                        case "sell":
                            DataStore.setGoToDeals(context, "2");
                            break;
                    }
                }
            }
        }
    }
}

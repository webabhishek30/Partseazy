package com.partseazy.android.pushnotification;

import android.content.Context;
import android.os.Bundle;

import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.moengage.pushbase.push.PushMessageListener;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;

/**
 * Created by gaurav on 30/03/17.
 */

public class CustomPushListener extends PushMessageListener {

    private static final String EXTRA_CUSTOM_PAYLOAD = "ex_self_silent_update";

    @Override
    protected void onPostNotificationReceived(Context context, Bundle extras) {
        super.onPostNotificationReceived(context, extras);
        CustomLogger.d("Push Extra Data is " + extras.toString());
        PartsEazyEventBus.getInstance().postEvent(EventConstant.NOTIFICATION_RECEIVED);
    }

    @Override
    public void onNonMoEngageMessageReceived(Context context, Bundle extras) {
        super.onNonMoEngageMessageReceived(context, extras);

    }


    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {

        boolean isRequired = super.isNotificationRequired(context, extras);
        if (isRequired) {
            FeatureMap.Feature pushNotfFeature = FeatureMap.getFeatureMap(FeatureMapKeys.push_notfication);
            if (!pushNotfFeature.isActive()) {
                CustomLogger.d("Disabling Notification since feature is OFF");
                isRequired = false;
            }
            if (extras.containsKey(EXTRA_CUSTOM_PAYLOAD)) {
                isRequired = false;
            }
        }
        return isRequired;


    }
}

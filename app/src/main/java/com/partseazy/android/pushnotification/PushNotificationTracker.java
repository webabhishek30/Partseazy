package com.partseazy.android.pushnotification;

import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;

/**
 * Created by gaurav on 30/03/17.
 */

public class PushNotificationTracker {

    public static boolean isMoEngageTrackingActive() {
        FeatureMap.Feature pushNotfFeature = FeatureMap.getFeatureMap(FeatureMapKeys.push_notfication);
        return pushNotfFeature.isActive();
    }

}

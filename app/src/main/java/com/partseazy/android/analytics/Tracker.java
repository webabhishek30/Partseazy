package com.partseazy.android.analytics;

/**
 * Created by gaurav on 07/03/17.
 */

public class Tracker {

    public static String getAction(String label) {
        if (label == null)
            return "";
        return label.toLowerCase().replace(" ", "_");
    }

    public static String getScreen(String tagName) {
        return (tagName != null && tagName.contains("Fragment")) ? tagName.replace("Fragment", "") : tagName;
    }

    public static void trackScreen(String screenName) {
        if (AnalyticsManager.getInstance() != null)
            AnalyticsManager.getInstance().trackScreenView(screenName);

    }


    public static void trackEvent(String screenName, String category, String action, String label) {
        if (AnalyticsManager.getInstance() != null)
            AnalyticsManager.getInstance().trackEvent(getScreen(screenName), category,
                    action != null ? getAction(action) : label, label);
    }

    public static void trackEvent(String screenName, String category, String action, String label, long value) {
        if (AnalyticsManager.getInstance() != null)
            AnalyticsManager.getInstance().trackEvent(getScreen(screenName), category,
                    action, label, value);
    }


    public static void trackEvent(String screenName, String category, String label) {
        if (AnalyticsManager.getInstance() != null)
            AnalyticsManager.getInstance().trackEvent(getScreen(screenName), category,
                    getAction(label), label);
    }

}

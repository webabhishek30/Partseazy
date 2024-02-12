package com.partseazy.android.analytics;

import android.app.Activity;
import android.content.Context;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by gaurav on 07/03/17.
 */

public class AnalyticsManager {

    private static AnalyticsManager instance;
    private static Context mContext;
    private Tracker mTracker;
    private GoogleAnalytics analytics;

    public static AnalyticsManager createInstance(Context context) {
        if (instance == null) {
            synchronized (AnalyticsManager.class) {
                instance = new AnalyticsManager(context);
            }
        }
        return instance;
    }

    public static AnalyticsManager getInstance() {
        return createInstance(mContext);
    }


    public AnalyticsManager(Context context) {
        analytics = GoogleAnalytics.getInstance(context);
        if (analytics == null)
            return;
        mTracker = analytics.newTracker(R.xml.global_tracker);

        if (BuildConfig.DEBUG) {
            analytics.setDryRun(true);
        } else {
            analytics.setDryRun(false);
        }

        reportActivityStart((Activity) context);
        this.mContext = context;
    }


    public Tracker getTracker() {
        return mTracker;
    }


    /**
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    protected void trackScreenView(String screenName) {

        mTracker.setScreenName(com.partseazy.android.analytics.Tracker.getScreen(screenName));
        mTracker.enableAutoActivityTracking(true);
        mTracker.enableExceptionReporting(true);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        // MoEHelper to track screen
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("UserScreenVisible",screenName);
        MoEHelper.getInstance(mContext).trackEvent("ScreenName", builder);
    }


    /**
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    protected void trackEvent(String screenName, String category, String action, String label) {

        // Build and send an Event.
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());


    }


    /**
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     * @param value    value
     */
    protected void trackEvent(String screenName, String category, String action, String label, long value) {

        // Build and send an Event.
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).setValue(value).build());


    }


    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Context context, Exception e) {
        if (e != null && mTracker != null) {

            mTracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(context, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    protected static void sendHit(HitBuilders.ScreenViewBuilder viewBuilder, HitBuilders.EventBuilder evtBuilder, String screenName) {
        if (getInstance() != null && getInstance().getTracker() != null) {
            screenName = com.partseazy.android.analytics.Tracker.getScreen(screenName);
            Tracker t = getInstance().getTracker();
            t.set("&cu", "INR");
            t.setScreenName(screenName);
            if (viewBuilder != null)
                t.send(viewBuilder.build());
            if (evtBuilder != null)
                t.send(evtBuilder.build());
        }
    }

    public void reportActivityStart(Activity activity) {
        analytics.reportActivityStart(activity);
    }


}

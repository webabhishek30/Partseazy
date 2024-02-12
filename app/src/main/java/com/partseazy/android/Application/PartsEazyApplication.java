package com.partseazy.android.Application;


import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.MoEngage;
import com.moengage.core.model.AppStatus;
import com.partseazy.android.R;
import com.partseazy.android.utility.fonts.FontUtiity;
import com.yariksoffice.lingver.Lingver;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Pumpkin Guy on 20/11/16.
 */

public class PartsEazyApplication extends Application {

    public static PartsEazyApplication partsEazyAppContext;
    private static String DEFAULT_LOG_TAG = "B2C2";
    static Typeface REGULAR, LIGHT, MEDIUM;
    private int cartCount;
    private int cartAmount;
    private int favCount;


    public static PartsEazyApplication getInstance() {
        return partsEazyAppContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(this, Locale.ENGLISH);
        MultiDex.install(this);
        partsEazyAppContext = this;
        initializeCrashlytics();

        Log.i(DEFAULT_LOG_TAG, "PartsEazyApplication.onCreate() uptime: "
                + SystemClock.uptimeMillis() + " elapsedRealtime: "
                + SystemClock.elapsedRealtime());
        initFont();
        //FacebookSdk Initialize
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(this);
        initPushNotifcationsMOESdk();


    }

    private void initPushNotifcationsMOESdk() {
        MoEngage moEngage = new MoEngage.Builder(this, "E9QEJHMWTU1OBAC5SFX2IVNW")
                .setSenderId("1062115007024") // required only if you are using GCM.
                .setNotificationSmallIcon(R.drawable.ic_launcher)
                .setNotificationLargeIcon(R.drawable.ic_launcher)
                .optOutTokenRegistration()
                .build();

        MoEngage.initialise(moEngage);
     /*   MoEngage.optOutDataTracking(this,true);
        MoEngage.optOutPushNotification(this,true);
        MoEngage.optOutInAppNotification(this,true);*/
    }

    public static void showToast(String msg) {
        Toast.makeText(partsEazyAppContext, "" + msg, Toast.LENGTH_LONG).show();
    }

    private void initFont() {
        MEDIUM = Typeface.createFromAsset(getAssets(), "fonts/Medium.ttf");
        REGULAR = Typeface.createFromAsset(getAssets(), "fonts/Regular.ttf");
        FontUtiity.init(REGULAR, MEDIUM);
    }

    private void initializeCrashlytics() {

        Fabric.with(this, new Crashlytics());

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public void setCartAmount(int cartAmount) {
        this.cartAmount = cartAmount;
    }

    public int getCartCount() {
        return cartCount;
    }

    public int getCartAmount() {
        return cartAmount;
    }

    public void setFavCount(int cartCount) {
        this.favCount = cartCount;
    }

    public int getFavCount() {
        return favCount;
    }




}

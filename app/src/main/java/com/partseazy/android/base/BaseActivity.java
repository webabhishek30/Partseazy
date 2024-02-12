package com.partseazy.android.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.moengage.core.model.AppStatus;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.ui.activity.HomeActivity;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.moe.pushlibrary.MoEHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;


/**
 * Created by Pumpkin Guy on 04/12/16.
 */

public class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION = 99;
    private DisplayMetrics displaymetrics;
    public MoEHelper mHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mHelper = MoEHelper.getInstance(getApplicationContext());


            //MoEngage User
        if (!DataStore.getExistingUser(getApplicationContext())) {
            DataStore.setExistingUser(getApplicationContext(), true);
            mHelper.setAppStatus(AppStatus.INSTALL);
            CustomLogger.d("First Time MOE User");
        } else {
            mHelper.setAppStatus(AppStatus.UPDATE);
            CustomLogger.d("Existing MOE User");
        }

    }

    public MoEHelper getMoeInsatnce() {
        return mHelper;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mHelper.onStart(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        mHelper.onResume(this);


    }

    protected void registerReceiver() {

        registerReceiver(networkChangeReceiver, new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (Exception ex) {
            CustomLogger.e("Got Exception ", ex);
        }

        mHelper.onStop(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHelper.onRestoreInstanceState(savedInstanceState);
    }


    public static void showToast(String msg) {
        if (msg != null)
            Toast.makeText(PartsEazyApplication.partsEazyAppContext, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mHelper.onNewIntent(this, intent);

    }

    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onNetworkChanged(intent);
        }
    };

    void onNetworkChanged(Intent intent) {
        ConnectivityManager networkManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoFromBroadcast = ConnectivityManagerCompat
                .getNetworkInfoFromBroadcast(networkManager, intent);
        if (networkInfoFromBroadcast != null)
            onNetworkChanged(networkInfoFromBroadcast.isConnected());
    }

    public void onNetworkChanged(boolean connected) {

        CustomLogger.d("Network has been changed " + connected);
        BaseFragment currentFragment = (BaseFragment) BaseFragment.getTopFragment(getSupportFragmentManager());
        CustomLogger.d("Current fragment is  " + currentFragment);

        if (currentFragment != null) {
            currentFragment.onNetworkConnectionChanged(connected);
        }

    }


    @Override
    public void onBackPressed() {
        if (((HomeActivity) this).isDeepLink() || ((HomeActivity) this).isPushNotification())
            onPopBackStack(true);
        else
            onPopBackStack(false);
    }


    public void onPopBackStack(boolean isMenuBack) {
        CustomLogger.d("onPopBackStack");
        final FragmentManager fragmentManager = getSupportFragmentManager();


        if (fragmentManager.getBackStackEntryCount() == 1) {


            if ((BaseFragment.getTopFragment(getSupportFragmentManager()) instanceof DealHomeFragment)) {
                BaseActivity.this.finish();

            } else {

                if (isMenuBack) {
                    BaseFragment.popToHome(this);

                } else {
                 //   MoEHelper.getInstance(this).logoutUser();
                    BaseActivity.this.finish();
                }

            }
        } else {
            BaseFragment currentFragment = BaseFragment.getTopFragment(fragmentManager);

            if (currentFragment instanceof OnBackPressed) {
                boolean shouldFinsh = ((OnBackPressed) currentFragment).onBackPressed();
                if (!shouldFinsh)
                    return;
            }

            if (currentFragment != null && currentFragment.isVisible()) {
                BaseFragment.popBackStack(fragmentManager);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public DisplayMetrics getDeviceMatrices() {

        if (displaymetrics == null) {
            displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        }
//        int height = displaymetrics.heightPixels;
//        int width = displaymetrics.widthPixels;
        return displaymetrics;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            View view = getCurrentFocus();
            if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
                int scrcoords[] = new int[2];
                view.getLocationOnScreen(scrcoords);
                float x = ev.getRawX() + view.getLeft() - scrcoords[0];
                float y = ev.getRawY() + view.getTop() - scrcoords[1];
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                    ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
            }
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            CustomLogger.e(e.toString());
            return false;
        }
    }


}

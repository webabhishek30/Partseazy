package com.partseazy.android.pushnotification;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.partseazy.android.Logger.CustomLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

public class FCMRegistrationUtils extends Handler {

    private Context context;
    private PushNotificationTask pushNotificationTask = null;

    public FCMRegistrationUtils(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void handleMessage(final Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1 && msg.obj != null) {
            final String pushnotificationId = msg.obj.toString();
            CustomLogger.d("push regid: " + pushnotificationId);
//            PushNotificationTask.TaskListener listener = new PushNotificationTask.TaskListener() {
//
//                @Override
//                public void onSuccess(RegistrationResponse registrationResponse) {
//                }
//
//                @Override
//                public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
//                }
//            };
//
//            pushNotificationTask = new PushNotificationTask(pushnotificationId, listener, context);
//            pushNotificationTask.execute((Void) null);

        } else {
            CustomLogger.d("Handler: Background registration failed");
            registerInBackground(this);
        }
    }

    // To Register for push notification service
    public void setUpFcmNotification() {
        // Check device for Play Services APK. If check succeeds, proceed with
        // GCM registration.
        if (checkPlayServices()) {
            String regid = MobiComUserPreference.getInstance(context).getDeviceRegistrationId();
            if (TextUtils.isEmpty(regid)) {
                registerInBackground(this);
            }
            CustomLogger.d("push regid: " + regid);
        } else {
            CustomLogger.d("No valid Google Play Services APK found.");
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a dialog that allows users
     * to download the APK from the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Toast.makeText(context, "Please download the Google play store apk", Toast.LENGTH_SHORT).show();
            } else {
                CustomLogger.d("This device is not supported for Google Play Services");
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously. Stores the registration ID and app versionCode in the
     * application's shared preferences.
     */
    private void registerInBackground(final Handler handler) {

        new Thread(new Runnable() {

            int retryCount = 0;

            @Override
            public void run() {
                CustomLogger.d("Registering In Background Thread");
                try {
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                    if (refreshedToken != null) {
                        Message msg = new Message();
                        msg.what = 1; // success
                        msg.obj = refreshedToken;
                        handler.sendMessage(msg);
                    }else{
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        run();
                    }
                } catch (Exception ex) {
                    // Retry three times....
                    retryCount++;
                    if (retryCount < 3) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        run();
                    } else {
                        CustomLogger.d("Error :" + ex.getMessage() + "\n");
                        Message msg = new Message();
                        msg.what = 0; // failure
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
    }
}
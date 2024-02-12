package com.partseazy.android.pushnotification;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegisterUserClientService;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.partseazy.android.Logger.CustomLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.moengage.push.PushManager;

public class FcmInstanceIDListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String registrationId = FirebaseInstanceId.getInstance().getToken();

        if (registrationId != null) {
            CustomLogger.d("Found Registration Id:" + registrationId);

            //Moengage

            PushManager.getInstance().refreshToken(getApplicationContext(), registrationId);
            //Applogic
            Applozic.getInstance(this).setDeviceRegistrationId(registrationId);
            if (MobiComUserPreference.getInstance(this).isRegistered()) {
                try {
                    RegistrationResponse registrationResponse = new RegisterUserClientService(this).updatePushNotificationId(registrationId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

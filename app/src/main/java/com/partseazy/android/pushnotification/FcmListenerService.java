package com.partseazy.android.pushnotification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.applozic.mobicomkit.api.notification.MobiComPushReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.moengage.push.PushManager;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.ui.activity.HomeActivity;

import java.util.Map;


public class FcmListenerService extends FirebaseMessagingService {
    private static final String TAG = FcmListenerService.class.getSimpleName();
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static int NOTIFICATION_ID;
    Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        CustomLogger.d("Message data:" + remoteMessage.getData());

        if (remoteMessage.getData().size() > 0) {

            if (MobiComPushReceiver.isMobiComPushNotification(remoteMessage.getData())) {
                CustomLogger.d("Applozic notification processing...");
                MobiComPushReceiver.processMessageAsync(this, remoteMessage.getData());
                return;
            } else if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData())) {
                CustomLogger.d("Moengage notification processing...");
                PushManager.getInstance().getPushHandler().handlePushPayload(getApplicationContext(), remoteMessage.getData());
                return;
            } else
            {
                handleNotification(""+remoteMessage.getData().get("message"), ""+remoteMessage.getData().get("title"));
            }


        }

        else
        {
            String messageNotification = remoteMessage.getNotification().getBody();
            if (messageNotification != null) {
                handleNotification(messageNotification, ""+remoteMessage.getNotification().getTitle());
            }

        }


    }

    private void handleNotification(String message, String title) {
        Log.v("notification", "---4" + message + "---" + title);

        if (!NotificationUtils.isAppIsInBackground(this)) {
            try {


                PendingIntent contentIntent = null;
                    intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                manager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
//                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.notification_sound);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (Build.VERSION.SDK_INT < 26) {
                            return;
                        }
                        AudioAttributes attributes = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.setDescription("Channel description");
                        //          channel.setSound(uri,attributes);
                        notificationManager.createNotificationChannel(channel);

                    }

                    builder = new NotificationCompat.Builder(this, "default")
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.b2c2_logo))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setColor(getResources().getColor(R.color.colorPrimary))
                            .setColor(Color.TRANSPARENT)
                            .setContentText(message)
                            //            .setSound(uri)
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                } else {
                    builder = new NotificationCompat.Builder(this)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.b2c2_logo))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText(message)
                            //.setSound(uri)
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                }

                builder.setContentIntent(contentIntent);
                mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, builder.build());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
            pushNotification.putExtra("isNotification", true);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        } else {
            try {

                PendingIntent contentIntent = null;


                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("select", "0");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                manager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
//                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.notification_sound);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (Build.VERSION.SDK_INT < 26) {
                            return;
                        }
                        AudioAttributes attributes = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.setDescription("Channel description");
                        //channel.setSound(uri,attributes);
                        notificationManager.createNotificationChannel(channel);
                    }
                    builder = new NotificationCompat.Builder(this, "default")
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.b2c2_logo))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setColor(getResources().getColor(R.color.colorPrimary))
                            .setColor(Color.TRANSPARENT)
                            .setContentText(message)
                            //  .setSound(uri)
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                } else {
                    builder = new NotificationCompat.Builder(this)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.b2c2_logo))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText(message)
                            //   .setSound(uri)
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message));
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                }
                builder.setContentIntent(contentIntent);
                mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
            pushNotification.putExtra("isNotification", true);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }
    }

}
package com.partseazy.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.partseazy.android.BuildConfig;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.datastore.DataStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by naveen on 12/6/17.
 */

public class UserActiveService extends Service {

    final long timeInterval = 3600000;

    public UserActiveService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        new UserDataThread().start();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    class UserDataThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if(DataStore.getUserPhoneNumber(getApplicationContext())!=null) {
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        String baseURL = BuildConfig.URL != null ? BuildConfig.URL : "http://13.234.33.246:8090/";
                        url = new URL(baseURL + "api/v1/user-info/connect/" + DataStore.getUserPhoneNumber(getApplicationContext()));

                        CustomLogger.d(url.toString());

                        urlConnection = (HttpURLConnection) url
                                .openConnection();
                        urlConnection.setRequestMethod("PUT");
                        urlConnection.setDoOutput(true);

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();

                        CustomLogger.d(stringBuilder.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }

                    try {
                        Thread.sleep(timeInterval);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}

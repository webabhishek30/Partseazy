package com.partseazy.android.gson;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Pumpkin Guy on 05/12/16.
 */

public class GsonHelper<T> {

    private final Handler mHandler;

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private static GsonHelper gsonHelper;

    public static GsonHelper getInstance() {

        if (gsonHelper == null) {
            synchronized (GsonHelper.class) {
                gsonHelper = new GsonHelper();
            }
        }
        return gsonHelper;
    }

    private GsonHelper() {
        mHandler = new Handler(Looper.getMainLooper());
        getGson();
    }

    /**
     * Call this to parse json data in Background using Gson
     *
     * @param responseObject - response received from sever
     * @param classType      - POJO class type for parsing response data
     * @param callback       - callback handler. posts parsed object to caller thread
     */
    public void parse(final String responseObject, final Class<T> classType, final OnGsonParseCompleteListener<T> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final T data = getGson().fromJson(responseObject, classType);
                    if (callback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (data != null)
                                    callback.onParseComplete(data);
                                else
                                    callback.onParseFailure(new NullPointerException("parse error, data is null"));
                            }
                        });
                    }
                } catch (final JsonSyntaxException e) {
                    if (callback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onParseFailure(e);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void parseOnUIThread(final String responseObject, final Class<T> classType, final OnGsonParseCompleteListener<T> callback) {

        try {
            final T data = getGson().fromJson(responseObject, classType);
            if (callback != null) {

                if (data != null)
                    callback.onParseComplete(data);
                else
                    callback.onParseFailure(new Exception("data is null"));
            }
        } catch (final JsonSyntaxException e) {
            if (callback != null) {
                callback.onParseFailure(e);
            }
        }
    }

    public static final <T> List<T> getList(String json) throws Exception {
        Type typeOfList = new TypeToken<List<T>>(){}.getType();
        return getGson().fromJson(json, typeOfList);
    }
}

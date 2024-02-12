package com.partseazy.android.gson;


import com.partseazy.android.Logger.CustomLogger;

public abstract class OnGsonParseCompleteListener<T> {
        public abstract void onParseComplete(T data);
        public void onParseFailure(Exception exception) {
            CustomLogger.e("Exception ", exception);
        }

}

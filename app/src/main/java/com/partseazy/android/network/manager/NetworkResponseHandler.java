package com.partseazy.android.network.manager;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.network.request.WebServiceConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pumpkin Guy on 21/11/16.
 */

public abstract class NetworkResponseHandler {

    protected static HashMap<String, String> headersMap;


    public static HashMap<String, String> getHeaders(Context context) {

        if (headersMap == null) {
            headersMap = new HashMap<String, String>();
            headersMap.put(WebServiceConstants.X2_PLATFORM, WebServiceConstants.ANDROID_HEADER_PLATFORM);
            headersMap.put(WebServiceConstants.X2_APP_INFO, WebServiceConstants.ANDROID_HEADER_APPINFO_VERSION_NUMBER);

            headersMap.put("accept-encoding", "gzip");
            headersMap.put("Content-Type", "application/json");
            headersMap.put("charset", "utf-8");


            if (DataStore.getSessionID(context) != null) {
                headersMap.put(WebServiceConstants.X2_SESSIONID, DataStore.getSessionID(context));
            }

        }
        return headersMap;
    }

    public static String generateGetUrl(String url, Map<String, String> params) {
        if (params != null) {
            url = url.replace(" ", "%20");
            Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                String value = params.get(key);
                if (TextUtils.isEmpty(value)) {
                    value = "";
                }
                uriBuilder.appendQueryParameter(key, value);
            }
            return uriBuilder.build().toString();
        } else {
            return url;
        }

    }


    public abstract String getBasePartsEazyUrl();


    protected Request<?> jsonRequestInternal(final int identifier, String url, RequestParams params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean shouldCache) {

        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            url = getBasePartsEazyUrl() + url;
        }

        CustomLogger.d("The requested URL is " + url);
        JsonObjectRequest request = new JsonObjectRequest(url, null, listener, errorListener);
        CustomLogger.d("The requested URL is " + url);
        return volleyRequest(request, identifier, params, shouldCache);
    }

    protected Request<?> volleyRequest(Request<?> request, int identifier, RequestParams params, boolean shouldCache) {
        request.setIdentifier(identifier);
        request.setShouldCache(shouldCache);
        request.setTag(this);
        CustomLogger.d("Preheader request" + request);
        WebServicePostParams.addDeafultHeaderParams(headersMap);
        if (params != null) {
            HashMap<String, String> headersMapExtraHeader = new HashMap<>();
            headersMapExtraHeader.putAll(headersMap);
            WebServicePostParams.addExtraHeaders(headersMapExtraHeader, params);
            request.setHeaders(headersMapExtraHeader);

        } else {
            request.setHeaders(headersMap);

        }
        CustomLogger.d("PostHeader request" + request);
        request.setRetryPolicy(
                new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequest(request);
        return request;
    }


    protected Request<?> multiPartRequest(Request<?> request, int identifier, RequestParams params, boolean shouldCache) {
        request.setIdentifier(identifier);
        request.setShouldCache(shouldCache);
        request.setTag(this);
        CustomLogger.d("Preheader request" + request);
        Map<String, String> headerMap = new HashMap<>();
        WebServicePostParams.addDeafultHeaderParams(headerMap);
        request.setHeaders(headerMap);
        CustomLogger.d("PostHeader request" + request);
        request.setRetryPolicy(
                new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequest(request);
        return request;
    }


    protected void addRequest(Request<?> request) {
        CustomLogger.d("Add request :" + request.getUrl());
    }

    protected void deleteRequest(Request<?> request) {
        CustomLogger.d("Delete request :" + request.getUrl());

    }
}

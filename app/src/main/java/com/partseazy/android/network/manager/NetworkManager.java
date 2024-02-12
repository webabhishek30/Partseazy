package com.partseazy.android.network.manager;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.MultipartRequest;
import com.android.volley.toolbox.MultipartRequestMap;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServicePostParams;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Pumpkin Guy on 15/11/16.
 */

public class NetworkManager extends NetworkResponseHandler implements RequestQueue.RequestFilter, RequestQueue.GlobalRequestQueueListener {


    private static RequestQueue requestQueue;
    private static NetworkManager instance;

    /**
     * The initial socket timeout in milliseconds
     */
    public static final int INITIAL_TIMEOUT_MS = 60 * 1000;
    private final int mMaxNumRetries = 1;


    @Override
    public String getBasePartsEazyUrl() {
        return (BuildConfig.URL != null ? BuildConfig.URL : "http://13.234.33.246:8090/");
    }

    private NetworkManager(Context context, String savedBaseUrl) {

        getHeaders(context);

        if (requestQueue == null) {
            requestQueue =
                    Volley.newRequestQueue(context);
        }
    }


    public static NetworkManager newInstance(Context context, String baseUrl) {
        if (instance == null) {
            instance = new NetworkManager(context, baseUrl);
            CustomLogger.d("Network Instance has been been initialized");
        }
        return instance;
    }

    public static NetworkManager getNetworkManagerInstance() {
        return instance;
    }

    public static RequestQueue getRequestInstance() {
        return requestQueue;
    }


    public void addRequest(Request<?> request) {
        if (!request.isDeleteCache() && request.hasHadResponseDelivered()) {
            throw new UnsupportedOperationException(
                    "Cannot reuse Request which has already served the request");
        }
        requestQueue.add(request);
    }

    public void deleteRequest(Request<?> request) {
        request.setDeleteCache(true);
        addRequest(request);
    }

    @Override
    public boolean apply(Request<?> request) {
        return request.getTag() == this;
    }


    @Override
    public void onResponse(Request request, Object responseObject, Response response) {

        final long startTimeInMillis = response.getStartTimeInMillis();
        final long endTimeInMillis = response.getEndTimeInMillis();
        if (startTimeInMillis > 0 && endTimeInMillis > 0) {
            long diff = (endTimeInMillis - startTimeInMillis);

        }
    }

    @Override
    public void onErrorResponse(Request request, VolleyError error) {

    }




    public Request<?> GetRequest(int identifier, String urlPath, Map<String, String> requestMap, RequestParams params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean shouldCache) {

        CustomLogger.d("Json Get request ");
        if (!TextUtils.isEmpty(urlPath) && !urlPath.startsWith("http")) {
            urlPath = getBasePartsEazyUrl() + urlPath;
        }


        String url = generateGetUrl(urlPath, requestMap);
        return jsonRequestInternal(identifier, url, params, listener, errorListener, shouldCache);

    }


    public Request<?> PostRequestContactAPI(int identifier, String url, Map<String, Object> requestMap, RequestParams params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean shouldCache) {

        CustomLogger.d("Json Post request ");
        JSONObject jsonRequest = (requestMap == null) ? null : new JSONObject(requestMap);

        JsonObjectRequest request =
                new JsonObjectRequest( url, jsonRequest, listener,
                        errorListener);
        CustomLogger.d("The url is " + request.getUrl());
        CustomLogger.d("The body is " + jsonRequest);
        request.setRetryPolicy(new DefaultRetryPolicy(0, mMaxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return volleyRequest(request, identifier, params, shouldCache);
    }

    public Request<?> PostRequest(int identifier, String url, Map<String, Object> requestMap, RequestParams params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean shouldCache) {

        CustomLogger.d("Json Post request ");
        JSONObject jsonRequest = (requestMap == null) ? null : new JSONObject(requestMap);

        JsonObjectRequest request =
                new JsonObjectRequest(getBasePartsEazyUrl() + url, jsonRequest, listener,
                        errorListener);
        CustomLogger.d("The url is " + request.getUrl());
        CustomLogger.d("The body is " + jsonRequest);
        request.setRetryPolicy(new DefaultRetryPolicy(0, mMaxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return volleyRequest(request, identifier, params, shouldCache);
    }
    public Request<?> PostFormDataRequest(int identifier, String url, Map<String, Object> requestMap, RequestParams params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean shouldCache) {

        CustomLogger.d("Json Post request ");
        JSONObject jsonRequest = (requestMap == null) ? null : new JSONObject(requestMap);

        JsonObjectRequest request =
                new JsonObjectRequest(getBasePartsEazyUrl() + url, jsonRequest, listener,
                        errorListener);
        CustomLogger.d("The url is " + request.getUrl());
        CustomLogger.d("The body is " + jsonRequest);
        request.setRetryPolicy(new DefaultRetryPolicy(0, mMaxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return volleyRequest(request, identifier, params, shouldCache);
    }


    public Request<?> PutRequest(int identifier, String url, Map<String, Object> requestMap, RequestParams params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean shouldCache) {

        CustomLogger.d("Json Put request ");
        JSONObject jsonRequest = (requestMap == null) ? null : new JSONObject(requestMap);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.PUT, getBasePartsEazyUrl() + url, jsonRequest, listener,
                        errorListener);
        CustomLogger.d("The url is " + request.getUrl());
        CustomLogger.d("The body is " + jsonRequest);
        return volleyRequest(request, identifier, params, shouldCache);
    }


    public Request<?> MultipartRequest(int identifier, String url,
                                       MultipartRequestMap requestMap, RequestParams params,
                                       Response.StringListener listener,
                                       Response.ErrorListener errorListener,
                                       Boolean shouldCache) {

        CustomLogger.d("Multipart Request ");

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST, getBasePartsEazyUrl() + url, requestMap, listener, errorListener);
        CustomLogger.d("The url is " + multipartRequest.getUrl());
        CustomLogger.d("The body is " + multipartRequest);

        return multiPartRequest(multipartRequest, identifier, params, shouldCache);


    }


    public Request<?> UploadOrderImageMultipartRequest(int identifier, String url,
                                                       MultipartRequestMap requestMap, RequestParams params,
                                                       Response.StringListener listener,
                                                       Response.ErrorListener errorListener,
                                                       Boolean shouldCache) {

        CustomLogger.d("Multipart Request ");

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,  url, requestMap, listener, errorListener);
        CustomLogger.d("The url is " + multipartRequest.getUrl());
        CustomLogger.d("The body is " + multipartRequest);


        return multiPartRequest(multipartRequest, identifier, params, shouldCache);


    }

    public Request<?> DeleteRequest(int identifier, String url, Map<String, String> requestMap, RequestParams params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, boolean shouldCache) {

        CustomLogger.d("Json Delete request ");
        JSONObject jsonRequest = (requestMap == null) ? null : new JSONObject(requestMap);
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            url = getBasePartsEazyUrl() + url;
        }
        url = generateGetUrl(url, requestMap);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.DELETE, url, jsonRequest, listener,
                        errorListener);
        CustomLogger.d("The url is " + request.getUrl());
        CustomLogger.d("The body is " + jsonRequest);

        return volleyRequest(request, identifier, params, shouldCache);
    }

    public Request<?> StringRequest(StringRequest request, int identifier, Map<String, String> header, RequestParams params, boolean shouldCache) {

        CustomLogger.d("The request is " + request);
        request.setIdentifier(identifier);
        request.setShouldCache(shouldCache);
        request.setTag(this);
        WebServicePostParams.addDeafultHeaderParams(headersMap);
        if (params != null)
            WebServicePostParams.addExtraHeaders(headersMap, params);

        request.setHeaders((header != null) ? header : headersMap);
        request.setRetryPolicy(
                new DefaultRetryPolicy(INITIAL_TIMEOUT_MS, mMaxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequest(request);
        return request;
    }

    public void cancelAll() {
        requestQueue.cancelAll(this);
    }

}

package com.android.volley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MultipartRequest extends Request<String> {

    private Response.StringListener mListener = null;
    private MultipartRequestMap params = null;
    private HttpEntity httpEntity = null;


    public MultipartRequest(int method, String url, MultipartRequestMap params, Response.StringListener listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.params = params;
        this.mListener = listener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (params != null) {
            httpEntity = params.getEntity();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }



    @Override
    protected Response<String> parseNetworkResponseUnpacked(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.processedData, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.processedData);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }


    @Override
    protected void deliverResponse(Request<String> request, String response, Response<String> fullResponse) {
        mListener.onStringResponse(request, response, fullResponse);


    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    public String getBodyContentType() {
        return httpEntity.getContentType().getValue();
    }


}

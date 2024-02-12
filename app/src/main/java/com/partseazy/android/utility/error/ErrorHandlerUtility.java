package com.partseazy.android.utility.error;

import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.ui.model.error.APIError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by gaurav on 26/12/16.
 */

public class ErrorHandlerUtility {


    /**
     * To fetch error data from API error response, If any
     *
     * @param error VolleyError onject
     * @return
     */
    public static APIError getAPIErrorData(VolleyError error) {

        APIError apiError = new APIError();

        try {
            if(error.networkResponse!=null) {
                String response = new String(error.networkResponse.data, "UTF-8");
                JSONObject errorJsonFromAPI = new JSONObject(response);
                JSONObject errObj = errorJsonFromAPI.optJSONObject("result");
                if(errObj!=null) {
                    CustomLogger.d("The error from server is " + new JSONObject(response));
                    apiError.erObject = errorJsonFromAPI;
                    apiError.message = errObj.getString("message");
                    apiError.issue = errObj.getString("issue");
                }else{
                    apiError.erObject = errorJsonFromAPI;
                    apiError.message = errorJsonFromAPI.getString("message");
                    apiError.issue = errorJsonFromAPI.getString("issue");
                }
            }


        } catch (UnsupportedEncodingException e) {
            CustomLogger.d("Exception ", e);
        } catch (JSONException e) {
            CustomLogger.d("Exception ", e);
        }

        return apiError;
    }
}

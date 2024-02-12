package com.partseazy.android.map;

import org.json.JSONObject;

/**
 * Created by gaurav on 14/01/17.
 */

public class FeatureMap {

    public static JSONObject DATA = null;
    private static Feature FEATURE = new Feature();


    public static void setFeatureMap(JSONObject resObj) {
        DATA = resObj;
    }

    public static Feature getFeatureMap(String mapKey) {
        if (FEATURE == null)
            FEATURE = new Feature();
        if (DATA == null) {
            FEATURE.clear();
            return FEATURE;
        }

        FEATURE.parseFeature(mapKey);
        return FEATURE;
    }


    /**
     * To Check, whether Feature is enabled or not
     *
     * @param mapKey
     * @return
     */
    public static boolean isFeatureEnabled(String mapKey) {
        if (FEATURE == null)
            FEATURE = new Feature();
        if (DATA == null) {
            FEATURE.clear();
            return FEATURE.isActive();
        }

        FEATURE.parseFeature(mapKey);
        return FEATURE.isActive();
    }


    public static class Feature {
        public int ACTIVE;
        public String PARAMS;

        public Feature() {
            clear();
        }

        public void parseFeature(String key) {
            JSONObject dataObj = DATA.optJSONObject(key);
            if (dataObj == null) {
                FEATURE.clear();
                return;
            }

            ACTIVE = dataObj.optInt("active");
            if (dataObj.optJSONObject("params") != null)
                PARAMS = dataObj.optString("params");
        }

        public boolean isActive() {
            return (ACTIVE == 1) ? true : false;
        }

        public void clear() {
            ACTIVE = 1;
            PARAMS = "";
        }
    }
}
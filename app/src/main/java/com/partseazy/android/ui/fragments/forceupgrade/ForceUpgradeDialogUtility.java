package com.partseazy.android.ui.fragments.forceupgrade;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.map.StaticMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gaurav on 25/02/17.
 */

public class ForceUpgradeDialogUtility {

    private static ArrayList<Integer> expiredVersionList = new ArrayList<Integer>();
    private static ArrayList<Integer> laterVersionList = new ArrayList<Integer>();
    public static JSONArray expiredVersions;
    public static JSONArray laterVersions;
    public static Integer FORCE_UPGRADE_VERSION;
    public static Integer MINIMUM_VERSION_SUPPORTED;
    public static Boolean FORCED_DIALOG_ENABLE;


    public enum VERSION_STATUS {

        SUPPORTED,
        LATER,
        EXPIRED;

    }


    public static void parseForceUpgradeMap(String responseObject, boolean status, final StaticMap.OnStaticMapParseListener listener) {

        try {

            JSONObject forceObject = new JSONObject(responseObject);

            MINIMUM_VERSION_SUPPORTED = forceObject.optInt("min_supported_version");
            FORCED_DIALOG_ENABLE = status;

            expiredVersions = forceObject.optJSONArray("expired_version");
            laterVersions = forceObject.optJSONArray("later_version");
            parseList(expiredVersionList, expiredVersions);
            parseList(laterVersionList, laterVersions);
        } catch (JSONException e) {
            CustomLogger.e("Exception ", e);
            if (listener != null)
                listener.onParseError();
        }
    }

    public static Enum isUpgradeRequired(int CURRENT_VERSION) {

        if (!FORCED_DIALOG_ENABLE || MINIMUM_VERSION_SUPPORTED == null) {
            return VERSION_STATUS.SUPPORTED;
        }

        if (CURRENT_VERSION >= MINIMUM_VERSION_SUPPORTED.intValue()) {
            return VERSION_STATUS.SUPPORTED;
        }

        if (laterVersionList.contains(CURRENT_VERSION)) {
            return VERSION_STATUS.LATER;
        }

        if (expiredVersionList.contains(CURRENT_VERSION)) {
            return VERSION_STATUS.EXPIRED;
        }

        return VERSION_STATUS.EXPIRED;

    }

    private static void parseList(ArrayList<Integer> list, JSONArray jArray) {
        if (jArray != null) {
            int len = jArray.length();
            for (int i = 0; i < len; i++) {
                list.add((Integer) jArray.opt(i));
            }
        }
    }
}


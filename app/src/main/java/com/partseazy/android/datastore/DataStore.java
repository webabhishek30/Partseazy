package com.partseazy.android.datastore;


import android.content.Context;
import android.content.SharedPreferences;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.model.supplier.placeAutocomplete.Prediction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.R.attr.value;

/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class DataStore {

    private static final String PREF_NAME = "PARTSEAZY_PREFERNCES";
    public static final String SESSION_ID = "_SESSION_ID";
    public static final String USER_SUPPLIER_TYPE = "USER_SUPPLIER_TYPE";
    public static final String USER_RETAILER_TYPE = "USER_RETAILER_TYPE";
    public static final String USER_AGENT_TYPE = "USER_AGENT_TYPE";
    public static final String PINCODE = "PINCODE";

    public static final String USER_PREMIUM = "USER_TAG_PREMIUM";

    public static final String INVITATION_CODE = "INVITATION_CODE";

    public static final String CART_ERR = "ERROR";
    public static final String DELIVERY_DAY = "DELIVERY";

    public static final String APP_USERID = "USERID";
    public static final String APP_USERNAME = "USERNAME";
    public static final String APP_USER_CREDIT = "USERCREDIT";
    public static final String APP_USEREMAIL = "USEREMAIL";
    public static final String APP_USER_SHOP_ID = "USER_SHOP_ID";
    public static final String APP_USER_CATEGORY_ID = "APP_USER_CATEGORY_ID";
    public static final String APP_USER_CATEGORY_NAME = "APP_USER_CATEGORY_NAME";


    public static final String USER_L1_CATEGORY_LIST = "USER_L1_CATEGORY_LIST";
    public static final String USER_L2_CATEGORY_LIST = "USER_L2_CATEGORY_LIST";
    public static final String USER_L3_CATEGORY_LIST = "USER_L3_CATEGORY_LIST";

    public static final String SEARCH_AUTO_SUGGESTION = "SEARCH_AUTO_SUGGESTION";
    public static final String SEARCH_AUTO_COMPLETE_LOCALITY = "SEARCH_AUTO_COMPLETE_LOCALITY";

    public static final String OTP_VERIFIED = "_OTP_VERIFIED";


    public static final String REGISTER_MOBILE = "_REGISTER_MOBILE";

    public static final String EXISTING_USER_MOE = "_EXISTING_USER_MOE";

    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String DEAL_ID = "DEAL_ID";
    public static final String SUPPLIER_ID = "SUPPLIER_ID";
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String GO_TO_DEALS = "GO_TO_DEALS_2";
    public static final String GO_TO_CHAT = "GO_TO_CHAT";
    public static final String GO_TO_REPORT = "GO_TO_REPORT";

    public static final String SUPPLIER_DEAL_FILTER = "SUPPLIER_DEAL_FILTER";

    public static final String APPLOZIC_LOGIN_REQUESTING = "AAPLOZIC_LOGIN_REQUESTING";
    public static final String APPLOZIC_LOGGED_IN = "APPLOZIC_LOGGED_IN";

    public static final String CONSUMER_FINANCING = "CONSUMER_FINANCING";

    private static final int RECENT_MAX = 10;

    public static SharedPreferences getDataStore(Context context) {

        SharedPreferences sharedPreferences = PartsEazyApplication.getInstance().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    private static void writeToPrefs(Context context, String key, String val) {
        SharedPreferences prefs = getDataStore(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, val);
        editor.commit();
    }

    private static void writeToPrefs(Context context, String key, long val) {
        SharedPreferences prefs = getDataStore(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, val);
        editor.commit();
    }

    private static void writeToPrefs(Context context, String key, boolean val) {
        SharedPreferences prefs = getDataStore(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }


    private static void deleteFromPrefs(Context context, String key) {
        SharedPreferences prefs = getDataStore(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key).commit();
        editor.commit();
    }

    private static boolean readBooleanPrefs(Context context, String key) {
        SharedPreferences prefs = getDataStore(context);
        return prefs.getBoolean(key, false);
    }

    private static String readStringPrefs(Context context, String key) {
        SharedPreferences prefs = getDataStore(context);
        return prefs.getString(key, null);
    }


    private static Long readLongPrefs(Context context, String key) {
        SharedPreferences prefs = getDataStore(context);
        return prefs.getLong(key, 0L);
    }

    public static String getSessionID(Context context) {
        return readStringPrefs(context, SESSION_ID);
    }

    public static void setSessionID(Context context, String sessionKey) {
        writeToPrefs(context, SESSION_ID, sessionKey);
    }

    public static boolean isSupplierType(Context context) {
        return readBooleanPrefs(context, USER_SUPPLIER_TYPE);
    }

    public static void setSupplierType(Context context, boolean isSupplier) {
        writeToPrefs(context, USER_SUPPLIER_TYPE, isSupplier);
    }

    public static boolean isRetailerType(Context context) {
        return readBooleanPrefs(context, USER_RETAILER_TYPE);
    }

    public static void setRetailerType(Context context, boolean isRetailer) {
        writeToPrefs(context, USER_RETAILER_TYPE, isRetailer);
    }

    public static boolean isAgentType(Context context) {
        return readBooleanPrefs(context, USER_AGENT_TYPE);
    }


    public static void setAgentType(Context context, boolean isAgent) {
        writeToPrefs(context, USER_AGENT_TYPE, isAgent);
    }

    public static boolean isPremiumUser(Context context) {
        return readBooleanPrefs(context, USER_PREMIUM);
    }

    public static void setUserPremium(Context context, boolean isPremium) {
        writeToPrefs(context, USER_PREMIUM, isPremium);
    }

    public static boolean isConsumerFinancingApplicable(Context context) {
        return readBooleanPrefs(context, CONSUMER_FINANCING);
    }

    public static void setConsumerFinancing(Context context, boolean cosumerFinance) {
        writeToPrefs(context, CONSUMER_FINANCING, cosumerFinance);
    }

    public static void clearSessionID(Context context) {
        deleteFromPrefs(context, SESSION_ID);
    }


    public static void clearInfo(Context context, BaseFragment fragment) {
        deleteFromPrefs(context, SESSION_ID);
    }


    public static String getPincode(Context context) {
        return readStringPrefs(context, PINCODE);
    }

    public static void setPincode(Context context, String sessionKey) {
        writeToPrefs(context, PINCODE, sessionKey);
    }

    public static String getInvitationCode(Context context) {
        return readStringPrefs(context, INVITATION_CODE);
    }

    public static void setInvitationCode(Context context, String sessionKey) {
        writeToPrefs(context, INVITATION_CODE, sessionKey);
    }

    public static String getDeliveryDay(Context context) {
        return readStringPrefs(context, DELIVERY_DAY);
    }

    public static void setDeliveryDay(Context context, String value) {
        writeToPrefs(context, DELIVERY_DAY, value);
    }

    public static String getUserID(Context context) {
        return readStringPrefs(context, APP_USERID);
    }

    public static void setUserId(Context context, String value) {
        writeToPrefs(context, APP_USERID, value);
    }

    public static String getUserName(Context context) {
        return readStringPrefs(context, APP_USERNAME);
    }

    public static String getUserCredit(Context context) {
        return readStringPrefs(context, APP_USER_CREDIT);
    }

    public static void setUserName(Context context, String value) {
        writeToPrefs(context, APP_USERNAME, value);
    }

    public static void setUserCredit(Context context, String value) {
        writeToPrefs(context, APP_USER_CREDIT, value);
    }

    public static String getUserEmail(Context context) {
        return readStringPrefs(context, APP_USEREMAIL);
    }

    public static void setAppUserEmail(Context context, String value) {
        writeToPrefs(context, APP_USEREMAIL, value);
    }

    public static String getUserShopId(Context context) {
        return readStringPrefs(context, APP_USER_SHOP_ID);
    }

    public static void setUserShopId(Context context, String shopId) {
        writeToPrefs(context, APP_USER_SHOP_ID, value);
    }

    public static String getUserCategoryId(Context context) {
        return readStringPrefs(context, APP_USER_CATEGORY_ID);
    }

    public static void setUserCategoryId(Context context, String catId) {
        writeToPrefs(context, APP_USER_CATEGORY_ID, catId);
    }
    public static String getUserCategoryName(Context context) {
        return readStringPrefs(context, APP_USER_CATEGORY_NAME);
    }

    public static void setUserCategoryName(Context context, String catName) {
        writeToPrefs(context, APP_USER_CATEGORY_NAME, catName);
    }

    public static String getL3CategoryList(Context context) {
        return readStringPrefs(context, USER_L3_CATEGORY_LIST);
    }

    public static void setL3CategoryList(Context context, String value) {
        writeToPrefs(context, USER_L3_CATEGORY_LIST, value);
    }

    public static String getL2CategoryList(Context context) {
        return readStringPrefs(context, USER_L2_CATEGORY_LIST);
    }

    public static void setL2CategoryList(Context context, String value) {
        writeToPrefs(context, USER_L2_CATEGORY_LIST, value);
    }

    public static String getL1CategoryList(Context context) {
        return readStringPrefs(context, USER_L1_CATEGORY_LIST);
    }

    public static void setL1CategoryList(Context context, String value) {
        writeToPrefs(context, USER_L1_CATEGORY_LIST, value);
    }


    public static boolean isOTOVerified(Context context) {
        return readBooleanPrefs(context, OTP_VERIFIED);

    }

    public static void setOTPVerified(Context context, boolean value) {
        writeToPrefs(context, OTP_VERIFIED, value);
    }


    public static String getUserPhoneNumber(Context context) {
        return readStringPrefs(context, REGISTER_MOBILE);

    }

    public static void setUserPhoneNUmber(Context context, String phone) {
        writeToPrefs(context, REGISTER_MOBILE, phone);
    }


    public static void addItemInAutoSuggestList(Context context, String autoSuggestKeyword) {


        List<String> searchList = DataStore.getRecentAutoSuggestSearch(context);

        if (searchList.contains(autoSuggestKeyword)) {
            int index = searchList.indexOf(autoSuggestKeyword);
            searchList.remove(index);
            searchList.add(0, autoSuggestKeyword);
        } else {

            if (searchList.size() < RECENT_MAX) {
                searchList.add(0, autoSuggestKeyword);
            } else {
                searchList.add(0, autoSuggestKeyword);
                searchList.remove(RECENT_MAX);
            }
        }
        CustomLogger.d("searchList " + searchList.size());
        DataStore.setRecentAutoSuggestSearch(context, searchList);

    }

    public static void setRecentAutoSuggestSearch(Context context, List<String> searchList) {
        String searchListJson = new Gson().toJson(searchList);
        writeToPrefs(context, SEARCH_AUTO_SUGGESTION, searchListJson);
    }

    public static List<String> getRecentAutoSuggestSearch(Context context) {

        List<String> suggestionList;
        String autoSuggestJson = readStringPrefs(context, SEARCH_AUTO_SUGGESTION);
        if (autoSuggestJson != null) {
            String[] autoSuggestionList = new Gson().fromJson(autoSuggestJson, String[].class);
            suggestionList = new LinkedList(Arrays.asList(autoSuggestionList));
        } else {
            suggestionList = new LinkedList<>();
        }
        return suggestionList;

    }


    public static Boolean getExistingUser(Context context) {
        return readBooleanPrefs(context, EXISTING_USER_MOE);
    }

    public static void setExistingUser(Context context, Boolean status) {
        writeToPrefs(context, EXISTING_USER_MOE, status);
    }


    public static String getProductId(Context context) {
        return readStringPrefs(context, PRODUCT_ID);
    }

    public static void setProductId(Context context, String value) {
        writeToPrefs(context, PRODUCT_ID, value);
    }

    public static String getDealId(Context context) {
        return readStringPrefs(context, DEAL_ID);
    }

    public static void setDealId(Context context, String value) {
        writeToPrefs(context, DEAL_ID, value);
    }

    public static String getSupplierId(Context context) {
        return readStringPrefs(context, SUPPLIER_ID);
    }

    public static void setSupplierId(Context context, String value) {
        writeToPrefs(context, SUPPLIER_ID, value);
    }

    public static String getCategoryId(Context context) {
        return readStringPrefs(context, CATEGORY_ID);
    }

    public static void setCategoryId(Context context, String value) {
        writeToPrefs(context, CATEGORY_ID, value);
    }

    public static String getGoToDeals(Context context) {
        return readStringPrefs(context, GO_TO_DEALS);
    }

    public static void setGoToDeals(Context context, String value) {
        writeToPrefs(context, GO_TO_DEALS, value);
    }

    public static boolean getGoToReport(Context context) {
        return readBooleanPrefs(context, GO_TO_REPORT);
    }

    public static void setGoToReport(Context context, boolean value) {
        writeToPrefs(context, GO_TO_REPORT, value);
    }

    public static boolean getGoToChat(Context context) {
        return readBooleanPrefs(context, GO_TO_CHAT);
    }

    public static void setGoToChat(Context context, boolean value) {
        writeToPrefs(context, GO_TO_CHAT, value);
    }

    public static boolean isApplozicLoggedIn(Context context) {
        return readBooleanPrefs(context, APPLOZIC_LOGGED_IN);
    }

    public static void setApplozicLoggedIn(Context context, boolean value) {
        writeToPrefs(context, APPLOZIC_LOGGED_IN, value);
    }

    public static boolean isApplozicLoginRequesting(Context context) {
        return readBooleanPrefs(context, APPLOZIC_LOGIN_REQUESTING);
    }

    public static void setApplozicLoginRequesting(Context context, boolean value) {
        writeToPrefs(context, APPLOZIC_LOGIN_REQUESTING, value);

    }


    public static void addInRecentLocalityList(Context context, Prediction prediction) {


        List<Prediction> searchList = DataStore.getRecentLocalitySearchList(context);
        int itemPosition = getPredictionPosition(searchList, prediction);
        CustomLogger.d("itemPosition ::" + itemPosition);
        if (itemPosition > -1) {
            CustomLogger.d("searchList 1");
            searchList.remove(itemPosition);
            searchList.add(0, prediction);
        } else {
            CustomLogger.d("searchList 2");
            if (searchList.size() < RECENT_MAX) {
                CustomLogger.d("searchList 3");
                searchList.add(0, prediction);
            } else {
                CustomLogger.d("searchList 4");
                searchList.add(0, prediction);
                searchList.remove(RECENT_MAX);
            }
        }
        CustomLogger.d("searchList Count ::" + searchList.size());
        DataStore.setRecentLocalitySearch(context, searchList);

    }

    public static List<Prediction> getRecentLocalitySearchList(Context context) {

        List<Prediction> suggestionList;
        String autoSuggestJson = readStringPrefs(context, SEARCH_AUTO_COMPLETE_LOCALITY);
        CustomLogger.d("autoSuggestJSon ::" + autoSuggestJson);
        if (autoSuggestJson != null) {
            Type listType = new TypeToken<List<Prediction>>() {
            }.getType();
            suggestionList = new Gson().fromJson(autoSuggestJson, listType);
        } else {
            suggestionList = new ArrayList<>();
        }
        return suggestionList;
    }

    public static void setRecentLocalitySearch(Context context, List<Prediction> searchList) {
        String searchListJson = new Gson().toJson(searchList);
        writeToPrefs(context, SEARCH_AUTO_COMPLETE_LOCALITY, searchListJson);
    }

    public static int getPredictionPosition(List<Prediction> predictionList, Prediction prediction) {
        int position = -1;
        if (predictionList != null && predictionList.size() > 0) {
            for (int i = 0; i < predictionList.size(); i++) {

                if (predictionList.get(i).id.equals(prediction.id)) {
                    position = i;
                    break;
                }

            }
        }
        return position;
    }

}

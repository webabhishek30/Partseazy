package com.partseazy.android.map;


/**
 * Created by Pumpkin Guy on 13/12/16.
 */

import com.partseazy.android.Logger.CustomLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Static Map class, which is responsible to read
 * all static and constants from Static Map API
 */
public class StaticMap {


    private static String SHOP_SIZE_UNIT;
    public static String CC_PHONE;
    public static String CC_EMAIL;
    public static String NODAL_BANK_NAME;
    public static String NODAL_COMPANY_NAME;
    public static String NODAL_BANK_IFSC;
    public static String NODAL_BANK_ACCOUNT;
    public static int OTP_START_INDEX = -1;
    public static String IMG_BASE_URL;
    public static String IMG_STRECH_URL;


    //shop size
    public static String size_xs;
    public static String size_s;
    public static String size_m;
    public static String size_l;
    public static String size_xl;
    public static String size_xxl;

    //cart errors
    public static String cart_er_isActive;
    public static String cart_er_isServicable;
    public static String cart_er_inStock;
    public static String cart_er_minQty;
    public static String cart_er_maxQty;


    //dispatch
    public static String dispatch_lower_days;
    public static String dispatch_upper_days;
    public static String delivery_lower_days;
    public static String delivery_upper_days;

    //sort catalogue
    public static JSONArray filterPriceList;
    public static JSONArray catalogueSortList;
    public static JSONArray searchSortList;


    // financial Credit Limit

    public static String credit_limit_15K;
    public static String credit_limit_25K;
    public static String credit_limit_50K;
    public static String credit_limit_1L;

    // Credit Facility
    public static String credit_epay_form;
    public static String credit_facility_desc;

    //color map
    public static Map<String, String> colorCodeMap;


    //COD discount percentage
    public static String COD_DISCOUNT;


    //COD discount percentage
    public static String REGISTRATION_TITLE;

    public static String REGISTRATION_MESSAGE;

    //IMAGE HANDLING
    public static String IMAGE;
    public static String IMG_WIDTH = "WIDTH";
    public static String IMG_HEIGHT = "HEIGHT";
    public static String IMG_SUB_PATH = "PATH";

    // FINANCIAL APPLICATION STATUS
    public static String CREDIT_NOT_APPLIED;
    public static String CREDIT_UNDER_PROCESS;
    public static String CREDIT_REJECTED;
    public static String CREDIT_APPROVED_ALLOWED;
    public static String CREDIT_APPROVED_DISALLOWED;

    //Education
    public static String EDUCATION_10TH;
    public static String EDUCATION_12TH;
    public static String EDUCATION_GRADUATION;
    public static String EDUCATION_POST_GRADUATION;
    public static String EDUCATION_OTHER;

    public static Map<String, String> shippingMap;
    public static String EXPRESS_SHIPPING = "express";
    public static String SELF_SHIPPING = "self";


    // Search Supplier Map
    public static Map<String, String> footfallMap;
    public static Map<String, String> shopSizeMap;
    public static Map<String, String> turnOverMap;


    //PREPAID PAYMENTS
    public static String PAYMENY_PREPAID;
    public static String PAYMENY_PREPAID_CC_DC;
    public static String PAYMENY_PREPAID_CC_DC_KEY;

    public static String PAYMENY_PREPAID_NB;
    public static String PAYMENY_PREPAID_NB_KEY;

    public static String PAYMENY_PREPAID_UPI;
    public static String PAYMENY_PREPAID_UPI_KEY;


    // DEALS
    public static int EXPIRY_DAY;
    public static String REPORT_CARD_URL;


    public interface OnMapListener {
        public void onParseError();
    }


    public static void parseStaticMap(JSONObject responseObject, final OnMapListener listener) {

        try {


            //Customer Care

            JSONObject ccObject = responseObject.optJSONObject("customer_care");
            if (ccObject != null) {
                StaticMap.CC_PHONE = ccObject.optString("phone");
                StaticMap.CC_EMAIL = ccObject.optString("email");
            }


            JSONObject payObject = responseObject.optJSONObject("payments");
            if (payObject != null) {
                JSONObject nodalObj = payObject.optJSONObject("nodal_account");
                if (nodalObj != null) {

                    StaticMap.NODAL_BANK_NAME = nodalObj.optString("bank_name");
                    StaticMap.NODAL_COMPANY_NAME = nodalObj.optString("company");
                    StaticMap.NODAL_BANK_IFSC = nodalObj.optString("ifsc");
                    StaticMap.NODAL_BANK_ACCOUNT = nodalObj.optString("number");
                }
            }


            //Verification OTP
            JSONObject jsonObject = responseObject.optJSONObject("verification_sms");
            if (jsonObject != null) {
                String OTP_PATTERN = jsonObject.optString("sms_message");
                OTP_START_INDEX = OTP_PATTERN.indexOf("XXXX");
            }


            //Shop size
            jsonObject = responseObject.optJSONObject("shop");
            if (jsonObject != null) {
                StaticMap.SHOP_SIZE_UNIT = jsonObject.optString("unit");
                JSONObject sizeObject = jsonObject.optJSONObject("size");
                StaticMap.size_xs = sizeObject.optString("xs");
                StaticMap.size_s = sizeObject.optString("s");
                StaticMap.size_m = sizeObject.optString("m");
                StaticMap.size_l = sizeObject.optString("l");
                StaticMap.size_xl = sizeObject.optString("xl");
                StaticMap.size_xxl = sizeObject.optString("xxl");
            }


            //Cart Errors
            jsonObject = responseObject.optJSONObject("errors");
            if (jsonObject != null) {
                JSONObject screenObject = jsonObject.optJSONObject("screen");
                if (screenObject != null) {
                    JSONObject erObject = screenObject.optJSONObject("cart");
                    if (erObject != null) {
                        StaticMap.cart_er_isActive = erObject.optString("is_active");
                        StaticMap.cart_er_isServicable = erObject.optString("is_servicable");
                        StaticMap.cart_er_inStock = erObject.optString("in_stock");
                        StaticMap.cart_er_minQty = erObject.optString("min_qty");
                        StaticMap.cart_er_maxQty = erObject.optString("max_qty");
                    }
                }
            }


            //Dispatch days
            jsonObject = responseObject.optJSONObject("transportation");
            if (jsonObject != null) {
                JSONObject daysObject = jsonObject.optJSONObject("days");
                if (daysObject != null) {
                    JSONObject dispatch = jsonObject.optJSONObject("dispatch");
                    if (dispatch != null) {
                        StaticMap.dispatch_lower_days = dispatch.optString("lower");
                        StaticMap.dispatch_upper_days = dispatch.optString("upper");
                    }
                    dispatch = jsonObject.optJSONObject("delivery");

                    if (dispatch != null) {
                        StaticMap.delivery_lower_days = dispatch.optString("lower");
                        StaticMap.delivery_upper_days = dispatch.optString("upper");
                    }
                }

            }

            //Catalogue Sort
            jsonObject = responseObject.optJSONObject("catalog");
            if (jsonObject != null) {
                catalogueSortList = jsonObject.optJSONArray("sort_options");
            }

            jsonObject = responseObject.optJSONObject("search");
            if (jsonObject != null) {
                searchSortList = jsonObject.getJSONArray("sort_options");
            }

            //Catalogue Filter price
            jsonObject = responseObject.optJSONObject("catalog");
            if (jsonObject != null) {
                JSONObject filtePriceObject = jsonObject.optJSONObject("filter_price");
                if (filtePriceObject != null) {
                    StaticMap.filterPriceList = filtePriceObject.optJSONArray("range");

                }
            }

            //Financial Application price
            jsonObject = responseObject.optJSONObject("finance_application");
            if (jsonObject != null) {
                JSONObject desiredLimit = jsonObject.optJSONObject("desired_limit");
                if (desiredLimit != null) {
                    StaticMap.credit_limit_15K = desiredLimit.getString("15 K");
                    StaticMap.credit_limit_25K = desiredLimit.getString("25 K");
                    StaticMap.credit_limit_50K = desiredLimit.getString("50 K");
                    StaticMap.credit_limit_1L = desiredLimit.getString("1 Lac");


                }
                StaticMap.credit_facility_desc = jsonObject.getString("html_description");
                StaticMap.credit_epay_form = jsonObject.getString("epay_form");


                JSONObject educationObject = jsonObject.optJSONObject("education");
                if (educationObject != null) {
                    StaticMap.EDUCATION_10TH = educationObject.optString("10th");
                    StaticMap.EDUCATION_12TH = educationObject.optString("12th");
                    StaticMap.EDUCATION_GRADUATION = educationObject.optString("graduation");
                    StaticMap.EDUCATION_POST_GRADUATION = educationObject.optString("post_graduation");
                    StaticMap.EDUCATION_OTHER = educationObject.optString("other");

                }
            }

            jsonObject = responseObject.optJSONObject("color");
            if (jsonObject != null) {
                colorCodeMap = null;
                JSONArray colorArray = jsonObject.getJSONArray("values");
                if (colorArray != null && colorArray.length() > 0) {
                    colorCodeMap = new HashMap<>();
                    for (int i = 0; i < colorArray.length(); i++) {
                        JSONObject colorObj = colorArray.getJSONObject(i);
                        colorCodeMap.put(colorObj.getString("name"), colorObj.getString("value"));

                    }
                }
            }


            //Cod discount

            JSONObject codObject = responseObject.optJSONObject("cod_payment");
            if (ccObject != null) {
                StaticMap.COD_DISCOUNT = ccObject.optString("cod_discount");
            }


            JSONObject registerObject = responseObject.optJSONObject("register");
            if (registerObject != null) {
                StaticMap.REGISTRATION_MESSAGE = registerObject.optString("message");
                StaticMap.REGISTRATION_TITLE = registerObject.optString("title_message");

            }


            JSONObject IMGObject = responseObject.optJSONObject("image");
            if (registerObject != null) {
                StaticMap.IMG_BASE_URL = IMGObject.optString("base_url");
                StaticMap.IMG_STRECH_URL = IMGObject.optString("strech_url");
            }

            JSONObject financePaymentObj = responseObject.optJSONObject("finance_payment");
            if (financePaymentObj != null) {
                StaticMap.CREDIT_NOT_APPLIED = financePaymentObj.optString("not_applied");
                StaticMap.CREDIT_UNDER_PROCESS = financePaymentObj.optString("under_process");
                StaticMap.CREDIT_REJECTED = financePaymentObj.optString("rejected");
                JSONObject approvedObj = financePaymentObj.optJSONObject("approved");
                if (approvedObj != null) {
                    StaticMap.CREDIT_APPROVED_ALLOWED = approvedObj.optString("success");
                    StaticMap.CREDIT_APPROVED_DISALLOWED = approvedObj.optString("failure");
                }
            }


            jsonObject = responseObject.optJSONObject("shipping");
            if (jsonObject != null) {
                shippingMap = new HashMap<>();
               shippingMap.put("express", jsonObject.getString("express"));
                shippingMap.put("self", jsonObject.getString("self"));
                shippingMap.put("standard", jsonObject.getString("standard"));
            }


            JSONObject payOBJ = responseObject.optJSONObject("payment");
            JSONObject prepayObj = payOBJ.optJSONObject("prepaid");

            if (prepayObj != null) {
                StaticMap.PAYMENY_PREPAID_CC_DC = prepayObj.getJSONObject("credit_debit_card").getString("display_name");
                StaticMap.PAYMENY_PREPAID_CC_DC_KEY = prepayObj.getJSONObject("credit_debit_card").getString("key");


                StaticMap.PAYMENY_PREPAID_NB = prepayObj.getJSONObject("net_banking").getString("display_name");
                StaticMap.PAYMENY_PREPAID_NB_KEY = prepayObj.getJSONObject("net_banking").getString("key");


                StaticMap.PAYMENY_PREPAID_UPI = prepayObj.getJSONObject("upi").getString("display_name");
                StaticMap.PAYMENY_PREPAID_UPI_KEY = prepayObj.getJSONObject("upi").getString("key");


            }


            JSONObject dealObj = responseObject.optJSONObject("deals");
            if (dealObj != null) {
                StaticMap.EXPIRY_DAY = dealObj.getInt("expire_in");
                StaticMap.REPORT_CARD_URL = dealObj.getString("report_card_url");
            }


            JSONObject shopObject = responseObject.optJSONObject("shop");
            if (shopObject != null) {

                JSONObject footfallObj = shopObject.getJSONObject("footfall_array");
                if (footfallObj != null) {
                    JSONArray footfallArray = footfallObj.getJSONArray("values");

                    if (footfallArray != null && footfallArray.length() > 0) {
                        footfallMap = new LinkedHashMap<>();
                        for (int i = 0; i < footfallArray.length(); i++) {
                            JSONObject obj = footfallArray.getJSONObject(i);
                            footfallMap.put(obj.getString("name"), obj.getString("value"));

                        }
                    }
                }


                JSONObject shopSizeObj = shopObject.getJSONObject("size_array");
                if (shopSizeObj != null) {
                    JSONArray shopSizeArray = shopSizeObj.getJSONArray("values");

                    if (shopSizeArray != null && shopSizeArray.length() > 0) {
                        shopSizeMap = new LinkedHashMap<>();
                        for (int i = 0; i < shopSizeArray.length(); i++) {
                            JSONObject obj = shopSizeArray.getJSONObject(i);
                            shopSizeMap.put(obj.getString("name"), obj.getString("value"));

                        }
                    }
                }

                JSONObject turnOverObj = shopObject.getJSONObject("turnover_array");
                if (turnOverObj != null) {
                    JSONArray turnOverArray = turnOverObj.getJSONArray("values");

                    if (turnOverArray != null && turnOverArray.length() > 0) {
                        turnOverMap = new LinkedHashMap<>();
                        for (int i = 0; i < turnOverArray.length(); i++) {
                            JSONObject obj = turnOverArray.getJSONObject(i);
                            turnOverMap.put(obj.getString("name"), obj.getString("value"));

                        }
                    }
                }


            }


        } catch (Exception e) {
            CustomLogger.e("Exception ", e);
            if (listener != null)
                listener.onParseError();
        }

    }


    public interface OnStaticMapParseListener {
        public void onParseError();
    }


}


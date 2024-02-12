package com.partseazy.android.constants;

import java.net.PortUnreachableException;

/**
 * Created by naveen on 6/12/16.
 */
public class AppConstants {
    public static final long SPLASH_DURATION_MS = 2000;
    public static final int MIN_SHOP_SIZE = 100;
    public static final int MAX_SHOP_SIZE = 1000;
    public static final int OTP_START_INDEX = 0;
    public static final int OTP_LENGTH = 4;
    public static final long OTP_WAIT_TIME = 60 * 1000;

    public static final int COD_RANGE = 5000;

    public static final int SUCCESS_STATUS = 1;

    public static final String SMS_ORIGIN = "INBTCT";

    // Maps Constants
    public static final String CATEGORY_ID = "category_id";
    public static final String MINIMUM_PRICE = "min_price";
    public static final String MAXIMUM_PRICE = "max_price";
    public static final String TAG = "positioning";

    public static final String LOW_TAG = "economic";
    public static final String MEDIUM_TAG = "middle";
    public static final String HIGH_TAG = "upper";
    public static final String LUXURY_TAG = "premium";

    public static final String ATTRIBUTE_COLOR_TAG = "color_tag";
    public static final String ATTRIBUTE_TAG = "attribute_tag";
    public static final String TRANSACTION_CANCELLED_BY_USER = "Transaction cancelled by user";


    public static final String AUTOCOMPLETE_PLACE_KEY = "AIzaSyDaXGb3IJQOZ21JDk26qpnXD_qPkiGqFss";

    public static final String PARTSEAZY_APP_KEY = "AIzaSyDNIVJFTNOhmn5RWO2hllWYwDNeSWxC4gY";
    public static final String PARTSEAZY_WEB_LINK = "http://partseazy.com/";
    public static final String TERMS_AND_CONDITION_URL = PARTSEAZY_WEB_LINK+"term-use?mobile=true";
    public static final String PRIVACY_POLICY_URL = PARTSEAZY_WEB_LINK+"privacy-policy?mobile=true";
    public static final String FAQ_URL = PARTSEAZY_WEB_LINK+"faqs?mobile=true";
    public static final String CONTACT_US_URL= PARTSEAZY_WEB_LINK+"contact?mobile=true";

    public static final String ABOUT_US_URL = PARTSEAZY_WEB_LINK+"about_us.php?mobile=true";
    public static final String EPAY_LATER_TERMS_CONDITION_URL = "http://wwww.epaylater.in/merchants/landingpage";
    public static final String STOCK_CORRECTION_URL= PARTSEAZY_WEB_LINK+"stock_correction.php?mobile=true";
    public static final String REFURBISH_URL= PARTSEAZY_WEB_LINK+"refurbished_quality_grade.php?mobile=true";
    public static final String INQUIRE_PRODUCT = "inquire";

    public static final int MAX_QTY_LENGTH = 5;

    public static final int REFURBISH_CAT_ID = 85;
    public static final int REFURBISH_CAT_2_ID = 86;

    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

}

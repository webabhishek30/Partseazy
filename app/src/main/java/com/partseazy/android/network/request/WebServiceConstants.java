package com.partseazy.android.network.request;

import android.os.Build;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.utility.CommonUtility;

/**
 * Created by Pumpkin Guy on 20/11/16.
 */


//Interface for all Network Request constants
public interface WebServiceConstants {


    /**
     * ******************************** HEADER SPECIFIC *************************
     */

    //TODO : Should be in gradle flavor or by server config

    String X2_PLATFORM = "X2-PLATFORM";
    String X2_SESSIONID = "X2-Sid";
    String X2_FINGERPRINT = "fingerprint";
    String X2_RESULT_WRAP = "X2-Result-Wrap";
    String X2_APP_INFO = "X2-Appinfo";


    String ANDROID_HEADER_APPINFO_VERSION_NUMBER = BuildConfig.APP_TYPE + ":" + String.valueOf(BuildConfig.VERSION_CODE);
    String ANDROID_HEADER_PLATFORM = "Android" + " " + Build.VERSION.RELEASE;
    String ANDROID_DEVICE_ID = CommonUtility.getDeviceId(PartsEazyApplication.getInstance());


    String API_V1 = "api/v1/";

    String API_V1_1 = "api/v1.1/";

    String API_GOOGLE_MAP = "https://maps.googleapis.com/maps/api/";


    /**
     * ******************************** AUTOCOMPLETE PLACE API*************************
     * GET : /
     */
    String AUTOCOMPLETE_PLACE = API_GOOGLE_MAP + "place/autocomplete/json?key=" + AppConstants.AUTOCOMPLETE_PLACE_KEY;


    /**
     * ********************************  GEO CODE  API*************************
     * GET : / geocode/json?&address={placename(Vasant kunj)}
     */
    String GEO_LOCATION = API_GOOGLE_MAP + "geocode/json?&address=";

    /**
     * ********************************  UPLOAD IMAGE  API*************************
     * POST : /http://139.59.83.177/Suite78/index.php?entryPoint=VliAppEntryPoint
     */
    String ORDER_BY_UPLOAD_IMAGE = "http://139.59.83.177/Suite78/index.php?entryPoint=VliAppEntryPoint";


    /**
     * ******************************** CREATE SESSION *************************
     * POST : /api/v1/session/create/app
     */
    String SESSION_URL = API_V1 + "session/create/app";

    /**
     * ******************************** STATIC MAP *************************
     * GET : /api/v1/mobile/static-map
     */
    String STATIC_MAP = API_V1 + "mobile/static-map/retailer";

    /**
     * ******************************** FEATURE MAP *************************
     * GET : /api/v1/mobile/feature-map
     */
    String FEATURE_MAP = API_V1 + "mobile/feature-map/retailer";

    /**
     * ******************************** START UP BANNER *************************
     * GET : /api/v1/mobile/banners/startup
     */
    String START_UP_BANNER = API_V1 + "mobile/banners/startup";

    /**
     * ******************************** CATEGORY LIST *************************
     * GET : /api/v1/category/list/1/2
     */
    String CATEGORY_LIST = API_V1 + "category/list/1/2";

    /**
     * ******************************** CATEGORY L3 LIST *************************
     * GET : /api/v1/category/list/L3
     */
    String L3_LIST = API_V1 + "category/list/L3";

    /**
     * ********************************  GET BRANDS *************************
     * GET : /api/v1/brand/list
     */
    String GET_BRANDS = API_V1 + "brand/list";

    /**
     * ******************************** SEND OTP *************************
     * POST : /api/v1/user/send-otp/retailer
     */
    String SEND_OTP = API_V1 + "user/send-otp";


    /**
     * ******************************** VERIFY OTP *************************
     * POST : /api/v1/user/verify-otp
     */
    String OTP_VERIFY = API_V1 + "user/signup/verify-otp";

    /**
     * ******************************** SET USER ROLES *************************
     * POST : /api/v1/user/set-roles
     */
    String USER_ROLES = API_V1 + "user/set-roles";

    /**
     * ******************************** USER REGISTRATION *************************
     * POST : /api/v1/user/registration
     */
    String USER_REGISTRATION = API_V1 + "user/registration";

    /**
     * ******************************** CATEGORY TREE LIST *************************
     * GET : /api/v1/category/tree
     */
    String CATEGORY_NTREE_1_2_LIST = API_V1 + "category/tree/1/2?onboarding=1&tag=commerce";

    /**
     * ******************************** CATEGORY STORE L3 LIST *************************
     * GET : /api/v1/category/tree
     */
    String CATEGORY_STORE_L3 = API_V1 + "shop/L3/";

    /**
     * ******************************** GET_CART *************************
     * GET : /api/v1/cart
     */
    String GET_CART = API_V1 + "cart";


    /**
     * ******************************** UPDATE_CART *************************
     * GET : /api/v1/cart
     */
    String UPDATE_CART = API_V1 + "cart/modify";

    /**
     * ******************************** REMOVED_CART *************************
     * PUT : /api/v1/cart/remove-item/{$}
     */
    String REMOVE_CART_ITEM = API_V1 + "cart/remove-many";

    /**
     * ******************************** GET FAVOURITE *************************
     * GET : /api/v1/favorite/detail
     */
    String FAV_DETAIL_LIST_ITEM = API_V1 + "favorite/detail";

    /**
     * ******************************** GET FAVOURITE *************************
     * GET : /api/v1/favorite
     */
    String FAV_ITEM = API_V1 + "favorite/detail";

    /**
     * ******************************** MOVE TO FAVOURITE *************************
     * PUT : /api/v1/cart/move-to-fav
     */
    String MOVE_FAV_ITEM = API_V1 + "cart/move-to-fav";


    /**
     * ******************************** REMOVE FAVOURITE *************************
     * PUT : /api/v1/fav/remove-item/1
     */
    String REMOVE_FAV_ITEM = API_V1 + "favorite/remove";

    /**
     * ******************************** ITEM_COUNT_ID *************************
     * GET : /
     */
    String ITEM_COUNT = API_V1 + "cart/count";

    /**
     * ******************************** ON BOARDING STATUS *************************
     * GET : /api/v1/category/tree
     */
    String ON_BOARDING_STATUS = API_V1 + "mobile/user/onboard-status";

    /**
     * ******************************** ON BOARDING STATUS *************************
     * GET : /api/v1/category/tree
     */
    String POST_L2_STORE_LIST = API_V1 + "shop/";
    /**
     * ******************************** ON BOARDING STATUS *************************
     * GET : /api/v1/category/tree
     */
    String POST_L1_SET_USER_CATEGORY = API_V1 + "user/set-user-category";

    /**
     * ******************************** ON BOARDING STATUS *************************
     * GET : /api/v1/document-type/list
     */
    String FINANCIAL_DOCUMENT_LIST = API_V1 + "document-type/list";

    /**
     * ******************************** ON BOARDING STATUS *************************
     * POST : /api/v1/document
     */
    String UPLOAD_FINANCIAL_DOCUMENT = API_V1 + "document";

    /**
     * ******************************** ON BOARDING STATUS *************************
     * POST : /api/v1/finance-application
     */
    String FINANCIAL_APPLICATION = API_V1 + "finance-application";

    /**
     * ******************************** GET_PRODUCT *************************
     * GET : /api/v1/cart
     */
    String GET_PRODUCT = API_V1 + "product-master/fetch/";

    /**
     * ******************************** ON SALE INQUIRY STATUS *************************
     * POST : /api/v1/inquiry-product
     */
    String INQUIRY_PRODUCT = API_V1 + "inquiry-product";

    /**
     * ******************************** ADD NEW SHIPPING ADDRESS *************************
     * POST : /api/v1/user/address
     */
    String POST_NEW_ADDRESS = API_V1 + "user/address";

    /**
     * ******************************** PUT  ADDRESS *************************
     * PUT : /api/v1/address/{addressId}
     */
    String UPDATE_ADDRESS = API_V1 + "address/";


    /**
     * ******************************** GET SHIPPING ADDRESS LIST *************************
     * GET : /api/v1/user/address
     */
    String GET_ADDRESS_LIST = API_V1 + "user/address/list";

    /**
     * ******************************** GET PINCODE CITY LIST *************************
     * GET : /api/v1/user/address
     */
    String GET_CITY = API_V1 + "address/lookup/pincode/";

    /**
     * ******************************** PUT CART/CHECKOUT DATA *************************
     * PUT : /api/v1/calculator
     */
    String PUT_CART_CHECKOUT = API_V1 + "calculator";

    /**
     * ******************************** GET CREDIT OPTION DATA *************************
     * GET : /api/v1/credit/options
     */
    String GET_PAYMENT_CREDIT = API_V1 + "finance/options";

    /**
     * ******************************** PUT_ADD_MULTI_TO_CART *************************
     * PUT : /api/v1/cart
     */
    String PUT_ADD_MULTI_TO_CART = API_V1 + "cart/add-multi";

    /**
     * ******************************** PUT_ADD_MULTI_TO_CART *************************
     * PUT : /api/v1/cart
     */
    String PUT_ADD_TO_CART = API_V1 + "cart/add";

    /**
     * ******************************** PUT_ADD_MULTI_TO_CART *************************
     * PUT : /api/v1/fav/add
     */
    String PUT_PRODUCT_TO_FAV = API_V1 + "favorite/add";

    /**
     * ******************************** GET_PRODUCT *************************
     * POST : /api/v1/index/browse/
     */
    String POST_BROWSE = API_V1 + "index/browse/";

    /**
     * ******************************** GET SEARCH RESULT *************************
     * POST : /api/v1/index/search/
     */
    String POST_SEARCH = API_V1 + "index/search";

    /**
     * ******************************** CREATE ORDER *************************
     * POST : /api/v1/order/create
     */
    String CREATE_ORDER = API_V1 + "order/create";

    /**
     * ******************************** RE-ORDER *************************
     * POST : order-sub/reorder
     */
    String REORDER = API_V1 + "order-sub/reorder";
    /**
     * ******************************** RE-ORDER *************************
     * PUT : order-sub/reorder/products
     */
    String REORDER_PRODUCT = API_V1 + "order-sub/reorder/product";

    /**
     * ******************************** Download Order Invoice *************************
     * GET : order-shipment/invoice/customer
     */
    String ORDER_INVOICE = API_V1 + "order-shipment/invoice/customer/";

    /**
     * ******************************** GET PAYU CHECKSUM *************************
     * GET : /api/v1/payment/<PTIN>
     */
    String PAYU_CHECECKSUM = API_V1 + "payment/initiate/";
    /**
     * ******************************** CATEGORY TREE LIST *************************
     * GET : /mobile/l2/?/home
     */
    String HOME_CATEGORY = API_V1 + "widget/category/";

    /**
     * ******************************** CGET USER SHOP DATA*************************
     * GET : /mobile/l2/?/home
     */
    String GET_USER_L123_LIST = API_V1 + "shop/L123";
    /**
     * ******************************** CGET USER SHOP DATA*************************
     * GET : /category/list/L1
     */
    String GET_USER_L1_LIST = API_V1 + "category/tree/1/1";

    /**
     * ******************************** COD OTP REQUEST *************************
     * PUT : /api/v1/user/send-otp
     */
    String SEND_COD_OTP = API_V1 + "order/send-confirmation-sms/";

    /**
     * ******************************** COD OTP VERIFY REQUEST *************************
     * PUT : /api/v1/user/send-otp
     */
    String VERIFY_COD_OTP = API_V1 + "payment/confirm";

    /**
     * ******************************** COD EPAY REQUEST *************************
     * PUT : /api/v1/epay/send-otp
     */
    String SEND_EPAY_OTP = API_V1 + "epay/send-otp";

    /**
     * ******************************** COD OTP VERIFY REQUEST *************************
     * PUT : /api/v1/user/send-otp
     */
    String VERIFY_EPAY_OTP = API_V1 + "epay/verify-otp";

    /**
     * ******************************** ORDER DETAILS REQUEST *************************
     * GET : /api/v1/order/summary/<ODIN></ODIN>
     */
    String ORDER_SUMMARY = API_V1 + "order/order-master/summary/";

    /**
     * ******************************** RECENT ORDER  LIST *************************
     * GET : /order/order-sub/recent
     */
    String GET_ORDER_RECENT = API_V1 + "order/order-sub/recent";

    /**
     * ******************************** RECENT ORDER  LIST *************************
     * GET : order/order-sub/{orderId}
     */
    String GET_ORDER_DETAILS = API_V1 + "order/order-sub/";

    /**
     * ******************************** GET FINANCE APP STATUS LIST *************************
     * GET : mobile/finance/status
     */
    String GET_FINANCE_APP_STATUS = API_V1 + "mobile/finance/status";

    /**
     * ******************************** POST CHAT SELLER REGISTER *************************
     * POST : api/v1/supplier/{id}/chat/register
     */
    String REGISTER_SELLER_CHAT = API_V1 + "supplier/";
    String REGSITER_SELLER_CHAT2 = "/chat/register";

    /**
     * ******************************** GET AGENT SEARCH RETAILER *************************
     * GET : api/v1/agent/search/{mobileNumber}
     */
    String GET_AGENT_SEARCH_CHAT = API_V1 + "agent/search/";

    /**
     * ******************************** GET AGENT SEARCH RETAILER *************************
     * GET : api/v1/index/autosuggest/{keyword}
     */
    String GET_INDEX_AUTOSUGGESTION = API_V1 + "index/autosuggest";

    /**
     * ******************************** PUT  ASSOCIATE RETAILER *************************
     * PUT : api/v1/agent/associate-retailer/{mobileNumber}
     */
    String ASSOCIATE_RETAILER = API_V1 + "agent/associate-retailer/";

    /**
     * ******************************** PUT  DISASSOCIATE RETAILER *************************
     * PUT : api/v1/agent/disassociate-retailer/{mobileNumber}
     */
    String DISASSOCIATE_RETAILER = API_V1 + "agent/disassociate-retailer";

    /**
     * ******************************** POST finance/available *************************
     * POST : api/v1/finance/available
     */
    String APPLY_CREDIT = API_V1 + "finance/available";

    /**
     * ******************************** USER DETAILS *************************
     * GET : /
     */
    String USER_DETAILS = API_V1 + "user/";

    /**
     * ******************************** DEAL CATEGORY  LIST *************************
     * GET : /api/v1/category/list/L2
     */
    String DEAL_CATEGORY_LIST = API_V1 + "category/list/L2";


    /**
     * ******************************** DEAL SUB CATEGORY  LIST *************************
     * GET : /api/v1/category/category/{l2Id}/children
     */
    String DEAL_SUBCATEGORY_LIST = API_V1 + "category/";

    String FILE_UPLOAD = API_V1_1 + "file";

    /**
     * ******************************** CREATE DEALS *************************
     * POST : /api/v1/index/trade/
     */
    String CREATE_DEAL = API_V1 + "trade";

    /**
     * ******************************** UPDATE DEALS *************************
     * PUT : /api/v1/index/trade/{tradeId}
     */
    String UPDATE_DEAL = API_V1 + "trade/";


    /**
     * ******************************** DEAL FILTER  LIST *************************
     * GET : /api/v1/trade/filters
     */
    String DEAL_FILTER_LIST = API_V1 + "trade/filters";

    /**
     * ******************************** CREATE DEALS *************************
     * GET : /api/v1/index/trade/detail/{}
     */
    String GET_DEAL = API_V1 + "trade/summary/";

    /**
     * ******************************** GET B2C2 Contact List  *************************
     * POST : /api/v1/index/user-info/get-b2c2-contacts
     */
    String POST_PARTSEAZY_CONTACTS = API_V1 + "user-info/active-users";

    /**
     * ******************************** GET B2C2 Contact List  *************************
     * POST : /api/v1/index/user-info/get-b2c2-contacts
     */
    String BROADCAST_DEAL = API_V1 + "trade_broadcast";

    /**
     * ******************************** GET SELL TRADE  *************************
     * GET : /api/v1/index/trade/get-sell-trades
     */
    String GET_SELL_TRADE = API_V1 + "trade/sell";

    /**
     * ******************************** GET REPORT CARD  *************************
     * GET : /api/v1/trade-report/summary?user=id
     */
    String GET_REPORT_CARD = API_V1 + "trade-report/summary";

    /**
     * ******************************** GET BUY TRADE  *************************
     * GET : /api/v1/index/trade/get-buy-trades
     */
    String GET_BUY_TRADE = API_V1 + "trade/buy";

    /**
     * ******************************** CREATE BOOKING DEAL  *************************
     * POST : /api/v1/ trade-booking/create/{tradeId}
     */
    String POST_BOOKING_DEAL = API_V1 + "trade-booking/create/";

    /**
     * ******************************** PRODUCT SKU PRICE DETAILS *************************
     * GET : /
     */
    String PRODUCT_SKU_PRICE_DETAILS = API_V1 + "product-sku/price-detail/";

    /**
     * ******************************** UPDATE PRODUCT SKU PRICE DETAILS *************************
     * PUT : /
     */
    String UPDATE_PRODUCT_SKU_PRICE = API_V1 + "product-sku/";

    /**
     * ******************************** UPDATE DEAL METRICS *************************
     * PUT : /
     */
    String UPDATE_DEAL_METRICS = API_V1 + "trade-metrics/increment";

    /**
     * ********************************  GET LIST OF USERS WHO VIEWED A TRADE *************************
     * GET : /api/v1/trade-metrics/
     */
    String GET_TRADE_VIEWERS = API_V1 + "trade-metrics/";

    /**
     * ******************************** RECENT BOOKING  LIST *************************
     * GET : /trade-booking/recent
     */
    String GET_BOOKING_RECENT = API_V1 + "trade-booking/recent";

    /**
     * ********************************  TRADE BOOKING  DETAIL *************************
     * GET :trade-booking/detail/{bcin}
     */
    String GET_BOOKING_DETAILS = API_V1 + "trade-booking/detail/";

    /**
     * ********************************  UPDATE TRADE BOOKING *************************
     * PUT :trade-booking/{bkin}
     */
    String UPDATE_BOOKING = API_V1 + "trade-booking/";

    /**
     * ******************************** GET TRADE ADDRESS *************************
     * GET : /api/v1/index/trade/
     */
    String DEAL_SHIPPING_ADDRESS = API_V1 + "trade/address";

    /**
     * ******************************** GET ALLOW BOOKING *************************
     * GET : /api/v1/trade-booking/{trin}
     */
    String GET_ALLOW_BOOKING = API_V1 + "trade-booking/allow/";

    /**
     * ******************************** UPDATE DEALS *************************
     * POST : /api/v1/trade-sku//{skuId}
     */
    String UPDATE_DEAL_SKU = API_V1 + "trade-sku/";

    /**
     * ******************************** GET PAYU CHECKSUM *************************
     * GET : /api/v1/payment/booking/initiate?bkin="BKIN"&method="cc";
     */
    String BOOKING_INITIATE_PAYMENT = API_V1 + "payment/booking/initiate";

    /**
     * ******************************** GET PENDING CREDIT *************************
     * GET : /api/v1/finance/pending-credit;
     */
    String PENDING_CREDIT = API_V1 + "finance/pending-credit";

    /**
     * ******************************** GET PENDING CREDIT *************************
     * GET : /api/v1/finance/pending-credit;
     */
    String POST_DEMO_MEETING = API_V1 + "trade-demo";

    /**
     * ******************************** GET PROMOTION RETAILER CLUSTER  *************************
     * GET : /api/v1/promotion-retailer/clusters
     */
    String PROMOTION_RETAIL_CLUSTER = API_V1 + "promotion-retailer/clusters";

    /**
     * ******************************** POST PROMOTION REQUEST   *************************
     * POST : /api/v1/tpromotion-request
     */
    String POST_PROMOTION_REQUEST = API_V1 + "promotion-request";

    /**
     * ******************************** GET PROMOTION RETAILER SHOPS  *************************
     * GET : /api/v1/promotion-retailer/retailers?districts={"Central Delhi,East Delhi"}
     */
    String GET_PROMOTION_RETAIL_SHOPS = API_V1 + "promotion-retailer/retailers";


    ///////////////////  RETAILER(SHOPS API's)

    /**
     * ******************************** BROWSE SHOPS DATA *************************
     * GET : /api/v1/index/browse/shop
     */
    String BROWSE_SHOPS_DATA = API_V1 + "index/browse/shop";

    /**
     * ********************************  GEO CODE  API*************************
     * POST : / geocode/json?&address={placename(Vasant kunj)}
     */
    String POST_BROWSE_SHOP = API_V1 + "index/browse/shop";

    /* ******************************** GET SHOP DETAIL *************************
     * GET : api/v1/shop/detail/{shopId}
     */
    String GET_SHOP_DETAIL = API_V1 + "shop/detail/";

    /**
     * ********************************  SEND OFFLINE CHAT MESSAGE *************************
     * POST : /api/v1/user/send-chat-sms/21
     */
    String POST_CHAT_OFFLINE = API_V1 + "user/send-chat-sms/";

    /**
     * ********************************  GET SUPPLIER DETAILS *************************
     * POST : /api/v1/retailer-customer/detail
     */
    String GET_SUPPLIER_DETAILS = API_V1 + "retailer-customer/detail";

    /**
     * ********************************  UPDATE ADDRESS *************************
     * PUT : /api/v1/address/1
     */
    String PUT_ADDRESS = API_V1 + "address/";

    /**
     * ********************************  REGISTER CUSTOMER *************************
     * PUT : /api/v1/retailer-customer/register
     */
    String POST_REGISTER_CUSTOMER = API_V1 + "retailer-customer/register";

    /**
     * ********************************  GET PRODUCT MASTERS *************************
     * GET : /api/v1/product-master/search
     */
    String GET_PRODUCT_MASTERS = API_V1 + "product-master/search";

    /**
     * ********************************  GET PRODUCTS *************************
     * GET : /api/v1/product/find
     */
    String GET_PRODUCTS = API_V1 + "product/find";

    /**
     * ********************************  POST SKU *************************
     * GET : /api/v1/product-sku
     */
    String POST_SKU = API_V1 + "product-sku";

    /**
     * ********************************  PUT SKU *************************
     * GET : /api/v1/product-sku/4
     */
    String PUT_SKU = API_V1 + "product-sku/";

    /**
     * ********************************  PUT ITEM *************************
     * GET : /api/v1/product-item/4
     */
    String PUT_ITEM = API_V1 + "product-item/";

    /**
     * ********************************  GET CUSTOMERS *************************
     * GET : /api/v1/retailer-customer/customer-list
     */
    String GET_CUSTOMERS = API_V1 + "retailer-customer/customer-list";

    /**
     * ********************************  GET CUSTOMER PRODUCTS *************************
     * GET : /api/v1/retailer-customer/product-list
     */
    String GET_CUSTOMER_PRODUCTS = API_V1 + "retailer-customer/product-list";

    /**
     * ********************************  GET CUSTOMER PRODUCTS *************************
     * GET : /api/v1/retailer-customer/product-list
     */
    String GET_GROUPS = API_V1 + "retailer-customer/groups";

    /**
     * ********************************  BLAST SMS *************************
     * GET : /api/v1/retailer-customer/blast-sms
     */
    String BLAST_SMS = API_V1 + "retailer-customer/blast-sms";

    /**
     * ********************************  SEND EPAY REGISTRATION LINK *************************
     * GET : /api/v1/epay/send-registration-link
     */
    String SEND_REGISTRATION_LINK = API_V1 + "epay/send-registration-link";

    /**
     * ********************************  GET EPAY TOKEN *************************
     * GET : /api/v1/epay/get-token
     */
    String GET_EPAY_TOKEN = API_V1 + "epay/get-token";
}

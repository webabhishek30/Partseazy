package com.partseazy.android.network.request;


/**
 * Created by Pumpkin Guy on 21/11/16.
 */

/**
 * This Enum would be accountable to increasing the request identifier for
 * each and every Web service request
 * This would be always increasing the ordinal values, so there would not be
 * any conflict while handling the request
 */
public enum RequestIdentifier {

    SESSION_REQUEST_ID,
    FEATURE_MAP_REQUEST_ID,
    STATIC_MAP_REQUEST_ID,
    STARTUP_BANNER_REQUEST_ID,
    OTP_REQUEST_ID,
    OTP_VERIFICATION_ID,
    USER_ROLES,
    USER_REGISTRATION_ID,
    CATEGORY_STORE_L3_LIST_REQUEST_ID,
    POST_L1_SUBMIT_ID,
    POST_L2_STORE_REQUEST_ID,
    POST_L3_STORE_REQUEST_ID,
    GET_CART_ID,
    CATEGORY_NTREE_1_2_REQUEST_ID,
    CATEGORY_NTREE_1_REQUEST_ID,
    ON_BOARDING_ST_REQ_ID,
    ITEM_COUNT_ID,
    FAV_ID,
    REMOVE_FAV_PRODUCT_REQUEST_ID,
    ADD_FAV_PRODUCT_REQUEST_ID,
    ADD_ADDRESS_ID,
    GET_CITY_ID,
    SHIPPING_ADDRESS_LIST_ID,
    DEMO_MEETING_ID,
    CART_UPDATE_ID,
    CART_ITEM_REMOVED_ID,
    MOVE_TO_FAV_ID,
    FINANCIAL_DOCUMENT_TYPE_LIST_REQUEST_ID,
    UPLOAD_DOCUMENT_REQUEST_ID,
    FINANCIAL_APPLICATION_REQUEST_ID,
    PRODUCT_ENQUIRY_REQUEST_ID,
    GET_PRODUCT_ID,
    CART_CHECKOUT_ID,
    UPDATE_GSTIN_ID,
    UPDATE_SHIPPING_NAME_ID,
    Checkout_CreditOption_ID,
    ADD_TO_CART,
    ADD_PRODUCT_TO_CART,
    PRODUCT_BROWSE,
    FILTER_DATA_REQUEST_ID,
    FILTER_APPLY_REQUEST_ID,
    CHECKOUT_CREATE_ORDER,
    PAYU_CHECKSUM,
    HOME_CATEGORY_REQUEST_ID,
    HOME_SHOP_USER_TAB_REQUEST_ID,
    USER_L123_CATEGORY_REQUEST_ID,
    OTP_COD_REQUEST_ID,
    OTP_COD_VERIFY_ID,
    OTP_EPAY_REQUEST_ID,
    OTP_EPAY_VERIFY_ID,
    ORDER_DETAILS_ID,
    REORDER_ID,
    ORDER_INVOICE,
    RECENT_ORDERS_LIST_ID,
    ORDER_DETAIL_SUMMARY_ID,
    FINANCE_APP_STATUS_ID,
    REGSITER_SELLER_CHAT,
    SEARCH_ID,
    AUTO_SUGGESTION_SEARCH_KEYWORD_ID,
    SEARCH_FILTER_APPLY_ID,
    AGENT_SEARCH_RETAILER_ID,
    ASSOCIATE_RETAILER_ID,
    DISASSOCIATE_RETAILER_ID,
    CART_WISHLIST_COUNT,
    ORDER_CREDIT_ID,
    GET_USER_DETAILS,
    GET_CATEGORY_LIST,
    GET_DEAL_SUBCATEGORY_LIST,
    GET_DEAL_CITY_ID,

    UPLOAD_PICTURE_ID,
    UPLOAD_CONTACTS_LIST,

    CREATE_DEAL_ID,
    GET_DEAL_ID,
    GET_PARTSEAZY_CONTACT_ID,
    BROADCAST_DEAL_ID,
    GET_SELL_DEAL,
    GET_REPORT_CARD,
    GET_BUY_DEAL,
    GET_DEAL_DETAIL_ID,
    GET_DEAL_CATEGORY_LIST,
    GET_DEAL_FILTERS_LIST,
    CREATE_BOOKING_ID,
    UPDATE_DEAL,


    GET_PRODUCT_SKU_PRICE_DETAILS,
    PUT_PRODUCT_SKU_PRICE_DETAILS,

    RECENT_BOOKING_LIST_ID,
    BOOKIND_DETAIL_SUMMARY,
    BOOKING_INITIATE_PAYMENT,
    UPDATE_DEAL_OPEN_METRICS,

    UPDATE_BOOKING,

    UPDATE_SELL_DEAL,

    GET_DEAL_SHIPPING_ADDRESS,

    GET_ALLOW_BOOKING,

    UPDATE_DEAL_SKU_ID,

    GET_USER_PENDING_CREDIT,

    POST_DEMO_MEETING_ID,
    TAKE_CARD_PIC_ID,

    PROMOTION_RETAILER_CLUSTER_ID,
    PROMOTION_REQUIRED_ID,
    PROMOTION_RETAILER_SHOPS_ID,
    PROMOTION_USER_CREDIT_ID,


    GET_BROWSE_SHOPS_ID,
    APPLY_SEARCH_SHOPS_REQUEST_ID,
    GEO_CODE_LOCATION_ID,
    GET_SHOP_DETAILS_ID,


    POST_CHAT_OFFLINE_MESSAGE,

    GET_TRADE_VIEW_USERS,

    GET_SUPPLIER_DETAILS,
    UPDATE_ADDRESS,
    REGISTER_CUSTOMER,
    GET_L3_CATS,
    GET_BRANDS,
    GET_PRODUCT_MASTERS,
    GET_PRODUCTS,
    POST_SKU,
    GET_CUSTOMERS,
    GET_CUSTOMER_PRODUCTS,
    PUT_SKU,
    PUT_ITEM,
    SWITCH_SKU,
    GET_GROUPS,
    BLAST_SMS,

    PUT_SEND_REGISTRATION_LINK,
    GET_EPAY_TOKEN,
}

package com.partseazy.android.network.request;

/**
 * Created by naveen on 12/12/16.
 */

public class WebParamsConstants {

    public static final String Mobile = "mobile";
    public static final String Otp = "otp";
    public static final String SessionId = "session_id";

    // for user roles
    public static final String Roles = "roles";

    // for retailer details
    public static final String Name = "name";
    public static final String InviteCode = "invite";
    public static final String ShopName = "shop_name";
    public static final String Email = "email";
    public static final String StoreSize = "size";
    public static final String L1Ids = "l1";
    public static final String L2Ids = "l2";
    public static final String L3Segments = "l3";
    public static final String CompanyName = "company_name";

    // for add address ....
    public static final String CityId = "city_id";
    public static final String Pincode = "pincode";
    public static final String PincodeLock = "pincode_lock";
    public static final String AddType = "type";
    public static final String StateId = "state_id";
    public static final String Address = "street";
    public static final String Landmark = "landmark";
    public static final String DeliveryOption = "type";
    //For Address
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";

    public static final String GSTN = "gstn";
    public static final String BillingName = "billing_name";
    //for cart

    public static final String CART_UPDATE_PRODUCT_SKUID = "product_sku_id";
    public static final String CART_UPDATE_QTY = "quantity";
    public static final String CART_IS_PRODUCT_SET = "is_product_set";

    // for financial documents
    public static final String IMAGE = "image";
    public static final String DOCUMENT_TYPE_ID = "document_type_id";
    public static final String IDENTITY_DOC_ID = "identity_document_id";
    public static final String ADDRESS_DOC_ID = "address_document_id";
    public static final String BUSINESS_DOC_ID = "business_document_id";
    public static final String DESIRED_LIMIT = "desired_limit";
    public static final String AGREE_SCRUTINY = "agree_scrutiny";
    public static final String DEVICE_INFO = "device_info";

    public static final String RETAILER_FULL_NAME = "full_name";
    public static final String PAN_NUMBER = "pan_number";
    public static final String DATE_OF_BIRTH = "dob";
    public static final String GENDER = "gender";
    public static final String EDUCATION = "education";
    public static final String COMPANY_NAME = "company_name";
    public static final String BUSINESS_PAN = "business_pan";
    public static final String DATE_OF_INCORPORATION = "date_of_incorporation";
    public static final String SHOP_ESTABLISHMENT_NUMBER = "shop_establishment_number";
    public static final String BUSINESS_ADDRESS_STREET = "shop_address_street";
    public static final String BUSINESS_ADDRESS_CITY = "shop_address_city";
    public static final String BUSINESS_ADDRESS_STATE = "shop_address_state";
    public static final String BUSINESS_ADDRESS_COUNTRY = "shop_address_country";
    public static final String BUSINESS_ADDRESS_PINCODE = "shop_address_pincode";
    public static final String RESIDENCIAL_ADDRESS_STREET = "residential_address_street";
    public static final String RESIDENCIAL_ADDRESS_CITY = "residential_address_city";
    public static final String RESIDENCIAL_ADDRESS_STATE = "residential_address_state";
    public static final String RESIDENCIAL_ADDRESS_COUNTRY = "residential_address_country";
    public static final String RESIDENCIAL_ADDRESS_PINCODE = "residential_address_pincode";


    // for send enquiry
    public static final String SUPPLIER_ID = "supplier_id";
    public static final String COMMENTS = "comments";
    public static final String DURATION = "frequency";
    public static final String PRODUCT_MASTER_ID = "product_master_id";
    public static final String QUANTITY = "qty";

    public static final String CART_PRODUCT_ID = "product_master_id";
    public static final String CART_PRODUCT_TYPE = "type";

    //for cart-checkout ..
    public static final String SHIPPING_METHOD = "shipping";
    public static final String CFORM = "cform";
    public static final String PINCODE = "pincode";
    public static final String PROMOCODE = "coupon";
    public static final String ADD_ID = "address_id";
    public static final String PAYMENT = "payment";

    public static final String SUBODIN = "subodin";

    public static final String BILLING_ADD_ID = "billing_address_id";
    public static final String BILLING_PINCODE = "billing_pincode";


    ///for add to cart ..
    public static final String PRODUCT_SKU_IDS = "product_sku_id";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String TYPE = "type";
    public static final String SAMPLE = "sample";

    //for sort filter
    public static final String SORT_FILTER = "sort";

    //for fAV ..
    public static final String REMOVE_FAV_PRODUCT = "product_master_id";

    //For Pages
    public static String PAGE = "page";


    //OTP Verify
    public static String OTP = "otp";
    public static String PTIN = "ptin";

    //For Search query
    public static String QUERY = "q";

    //For OnBoarding
    public static String FORCE = "force";


    //For Credit
    public static String AMOUNT = "amount";
    public static String ADDRESSID = "address_id";
    public static String PROD_MASTER_ID = "product_master_id";

    public static final String ENTITY = "entity";


    //For Creating Deal
    public static String SKUS = "skus";
    public static String STREET = "street";
    public static String DEAL_PINCODE = "pincode";
    public static String IMAGES = "images";
    public static String DEAL_NAME = "name";
    public static String DEAL_L2 = "l2";
    public static String DEAL_L3 = "l3";
    public static String DEAL_DESC = "desc";
    public static String ENDING_DATE = "ending_at";
    public static String STARTING_DATE = "starting_at";
    public static String ESCROW = "escrow";
    public static String COURIER_FEE = "courier_fee";
    public static String DISPATCH = "dispatch_in";
    public static String ALLOW_DEMO = "allow_demo";

    public static String PAY_METHODS = "pay_methods";
    public static String IS_PUBLIC = "public";
    public static String DEAL_TERMS_CONDITION = "terms";

    public static String NUMBERS = "numbers";

    public static final String SHIP_METHODS = "ship_methods";

    public static String TRADE_ID = "trade_id";
    public static String APP_CONTACTS = "app_contacts";
    public static String SMS_CONTACTS = "sms_contacts";
    public static String APP_MESSAGE = "app_message";
    public static String SMS_MESSAGE = "sms_message";


    public static String PAGE_COUNT = "page";
    public static String PUBLIC_DEAL = "public";
    public static String L2_CATEGORY = "l2";

    public static String USER = "user";

    //For Sku Price update
    public static String COST = "cost";
    public static String PRICE = "price";
    public static String SOURCE = "source";


    // For Create Booking
    public static String ADDRESS_ID = "address_id";
    public static String SHIP_METHOD = "ship_method";
    public static String BOOKING_ITEMS = "items";

    // For Deal Metrics
    public static String VIEWS = "views";
    public static String OPENS = "opens";

    // For Booking Update
    public static String PAY_METHOD = "pay_method";

    // For Deactive the Sell Deal
    public static String ACTIVE = "active";

    //For Updating Deal SKU
    public static String SKU_DESC = "desc";
    public static String SKU_PRICE = "price";
    public static String SKU_MRP = "mrp";
    public static String SKU_STOCK = "stock";
    public static String SKU_MIN_QTY = "min_qty";
    public static String SKU_REMAINING = "remaining";


    //For Updating Deal SKU
    public static String DEMO_NAME = "name";
    public static String DEMO_ADDRESS = "address";
    public static String DEMO_MOBILE = "mobile";
    public static String DEMO_IMAGE = "image";
    public static String DEMO_TRADE_ID = "trade_id";

    //For Posting Promotion Request
    public static String CREDITS_REQUIRED = "credits_required";
    public static String CLUSTERS = "clusters";

    public static String DISTRICTS = "districts";

    //For MAP
    public static String MAP = "map";

    //For Offline Chat Message
    public static String MSG = "message";

    //For Booster
    public static String SEARCH_GEOPOINTS = "_f.geo_points";
    public static String SEARCH_GEOBOXES = "_f.geo_boxes";
    public static String SEARCH_CATEGORY = "_f.category";
    public static String SEARCH_TURNOVER = "_f.turnover";
    public static String SEARCH_FOOTFALL = "_f.footfall";
    public static String SEARCH_BRANDS = "_f.brands";
    public static String SEARCH_SIZE = "_f.size";
    public static String PAGES = "page";
    public static String SORT_CODE = "sort";

    //For customer registration
    public static String ANNIVERSARY = "anniversary";
    public static String GROUP = "groups";

    //For inserting SKU
    public static String PRODUCT_ID = "product_id";
    public static String CUSTOMER_PRICE = "customer_price";
    public static String CUSTOMER_COST = "customer_cost";
    public static String ALLOW_CUSTOMER_SALE = "allow_customer_sale";
    public static String CUSTOMER = "customer";

    public static String BUY_STOCK = "buy_stock";

    public static String USER_IDS = "user_ids";
}

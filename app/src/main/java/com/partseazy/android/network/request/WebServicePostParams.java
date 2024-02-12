package com.partseazy.android.network.request;


import com.android.volley.toolbox.MultipartRequestMap;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.ui.fragments.cart.CartFragment;
import com.partseazy.android.ui.fragments.cart_checkout.CartCheckoutBaseFragment;
import com.partseazy.android.ui.fragments.deals.create_deal.NewDealBaseFragment;
import com.partseazy.android.ui.fragments.finance.RetailerData;
import com.partseazy.android.ui.fragments.shippingaddress.AddNewShippingAddressFragment;
import com.partseazy.android.ui.model.deal.SkuDealModel;
import com.partseazy.android.ui.model.deal.booking.BookingItem;
import com.partseazy.android.ui.model.deal.deal_detail.Address;
import com.partseazy.android.ui.model.deal.demo.DemoPostData;
import com.partseazy.android.ui.model.deal.promotion.PromotionPostData;
import com.partseazy.android.ui.model.supplier.search.FinalSearchData;
import com.partseazy.android.utility.CommonUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.partseazy.android.network.request.WebParamsConstants.ADD_ID;
import static com.partseazy.android.network.request.WebParamsConstants.BILLING_ADD_ID;
import static com.partseazy.android.network.request.WebParamsConstants.BILLING_PINCODE;
import static com.partseazy.android.network.request.WebParamsConstants.CFORM;
import static com.partseazy.android.network.request.WebParamsConstants.OTP;
import static com.partseazy.android.network.request.WebParamsConstants.PAYMENT;
import static com.partseazy.android.network.request.WebParamsConstants.PINCODE;
import static com.partseazy.android.network.request.WebParamsConstants.PROMOCODE;
import static com.partseazy.android.network.request.WebParamsConstants.PTIN;
import static com.partseazy.android.network.request.WebParamsConstants.SHIPPING_METHOD;

/**
 * Created by Pumpkin Guy on 21/11/16.
 */

public class WebServicePostParams {


    public static void addDeafultHeaderParams(Map<String, String> params) { //

        // Common params

        String sessionId = DataStore.getSessionID(PartsEazyApplication.partsEazyAppContext);

        if (sessionId != null)
            CustomLogger.d("Header - The session is  " + sessionId);

        if (sessionId != null)
            params.put(WebServiceConstants.X2_SESSIONID, sessionId);

        CustomLogger.d("The Header Map is " + params.toString());

    }

    public static void addExtraHeaders(Map<String, String> params, RequestParams requestParams) {

        if (requestParams != null && requestParams.headerMap != null) {

            for (Map.Entry<String, String> entry : requestParams.headerMap.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }

        }

    }

    public static void removeExtraHeaders(Map<String, String> params, RequestParams requestParams) {

        if (requestParams != null && requestParams.headerMap != null) {

            for (Map.Entry<String, String> entry : requestParams.headerMap.entrySet()) {
                params.remove(entry.getKey());
            }

        }

    }


    public static void addResultWrapHeader(Map<String, String> params) {

        params.put(WebServiceConstants.X2_RESULT_WRAP, "1");

    }


    public static Map<String, Object> mapWithCommonParams() { //
        Map<String, Object> params = new HashMap<String, Object>();
        return params;
    }

    public static Map<String, Object> sessionParams() {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebServiceConstants.X2_FINGERPRINT, WebServiceConstants.ANDROID_DEVICE_ID);
        return params;
    }

    public static Map<String, Object> OTPVerifyParams(String mobile, String otpCode) {
        Map<String, Object> params = mapWithCommonParams();
        String sessionId = DataStore.getSessionID(PartsEazyApplication.partsEazyAppContext);
        if (sessionId != null) {
            //  params.put(WebServiceConstants.X2_FINGERPRINT, WebServiceConstants.ANDROID_DEVICE_ID);
            // params.put(WebParamsConstants.SessionId, sessionId);
            params.put(WebParamsConstants.Mobile, mobile);
            params.put(WebParamsConstants.Otp, otpCode);
        }
        return params;
    }

    public static Map<String, Object> SendOTPParams(String mobile) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.Mobile, mobile);
        return params;
    }

    public static Map<String, Object> userTypeParams(ArrayList roles) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.Roles, roles);
        return params;
    }

    public static Map<String, Object> retailerParams(String name, String shopName, String email, String storeSize, String pincode, String inviteCode, String companyName) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.Name, name);
        params.put(WebParamsConstants.InviteCode, inviteCode);
        params.put(WebParamsConstants.ShopName, shopName);
        params.put(WebParamsConstants.Email, email);
        params.put(WebParamsConstants.StoreSize, storeSize);
        params.put(WebParamsConstants.PincodeLock, pincode);
        params.put(WebParamsConstants.CompanyName, companyName);
        return params;
    }

    public static Map<String, Object> sendReorderOdinNumber(String odin) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.SUBODIN, odin);
        return params;
    }
    public static Map<String, Object> sendL2CategoryListParams(String subCategoryList) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.L2Ids, subCategoryList);
        return params;
    }
    public static Map<String, Object> sendL1CategoryParams(String subCategoryList) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.L1Ids, subCategoryList);
        return params;
    }

    public static Map<String, Object> sendL3CategoryListParams(JSONArray jsonArray) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.L3Segments, jsonArray);
        return params;
    }

    public static Map<String, Object> sendAddressParam(AddNewShippingAddressFragment.AddressModel addressModel) {
        Map<String, Object> params = mapWithCommonParams();
        if (addressModel.deliveryName != null && !addressModel.deliveryName.equals("")) {
            params.put(WebParamsConstants.Name, addressModel.deliveryName);
        }
        params.put(WebParamsConstants.Mobile, addressModel.mobileNo);
        params.put(WebParamsConstants.Address, addressModel.address);
        if (addressModel.landmark != null) {
            params.put(WebParamsConstants.Landmark, addressModel.landmark);
        }
        //params.put(WebParamsConstants.DeliveryOption,deliveryOption);
        params.put(WebParamsConstants.CityId, addressModel.cityId);
        params.put(WebParamsConstants.StateId, addressModel.stateId);
        params.put(WebParamsConstants.Pincode, addressModel.pincode);
        params.put(WebParamsConstants.AddType, addressModel.deliveryOption);
        if (addressModel.GSTN != null && !addressModel.GSTN.equals(""))
            params.put(WebParamsConstants.GSTN, addressModel.GSTN);

        if (addressModel.billingName != null && !addressModel.billingName.equals(""))
            params.put(WebParamsConstants.BillingName, addressModel.billingName);

        return params;
    }


    public static Map<String, Object> updateCartParams(CartCheckoutBaseFragment.FinalCartAttributes finalCartAttributes, int productId, int quantity, boolean isProductSet) {
        Map<String, Object> params = cartCheckOutRequest(finalCartAttributes);
        params.put(WebParamsConstants.CART_UPDATE_PRODUCT_SKUID, productId);
        params.put(WebParamsConstants.CART_UPDATE_QTY, quantity);
        // params.put(WebParamsConstants.CART_IS_PRODUCT_SET, isProductSet);

        return params;
    }

    public static Map<String, Object> removeFavProduct(int productId) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.REMOVE_FAV_PRODUCT, productId);
        return params;
    }


    public static Map<String, Object> removeCartItems(CartCheckoutBaseFragment.FinalCartAttributes finalCartAttributes, String skuId, String type) {
        Map<String, Object> params = cartCheckOutRequest(finalCartAttributes);
        params.put(WebParamsConstants.CART_PRODUCT_ID, skuId);
        params.put(WebParamsConstants.CART_PRODUCT_TYPE, type);
        return params;
    }


    public static Map<String, Object> cartCheckOutRequest(CartFragment.FinalCartAttributes cartAttributes) {
        Map<String, Object> params = mapWithCommonParams();
        if (cartAttributes == null)
            return params;

        if (cartAttributes.couponCode != null)
            params.put(PROMOCODE, cartAttributes.couponCode);
        if (DataStore.getPincode(PartsEazyApplication.partsEazyAppContext) != null)
            params.put(PINCODE, DataStore.getPincode(PartsEazyApplication.partsEazyAppContext));

        int value = (cartAttributes.cForm != null && cartAttributes.cForm != 0) ? 1 : 0;
        params.put(CFORM, value);

        if (cartAttributes.ShippingMethod != null)
            params.put(SHIPPING_METHOD, cartAttributes.ShippingMethod);

        if (cartAttributes.billingPincode != null)
            params.put(BILLING_PINCODE, cartAttributes.billingPincode);


        return params;
    }

    public static Map<String, Object> updateGSTIN(String gstin, String billingName) {
        Map<String, Object> params = mapWithCommonParams();
        if (gstin != null) {
            params.put(WebParamsConstants.GSTN, gstin);
        }
        params.put(WebParamsConstants.BillingName, billingName);
        return params;
    }

    public static Map<String, Object> updateShippingName(String shippingName) {
        Map<String, Object> params = mapWithCommonParams();
        if (shippingName != null) {
            params.put(WebParamsConstants.Name, shippingName);
        }
        return params;
    }


    public static Map<String, Object> createOrderParams(CartFragment.FinalCartAttributes cartAttributes) {
        Map<String, Object> params = cartCheckOutRequest(cartAttributes);

        if (cartAttributes.addressId != null)
            params.put(ADD_ID, cartAttributes.addressId);

        if (cartAttributes.payMethod != null)
            params.put(PAYMENT, cartAttributes.payMethod);

        if (cartAttributes.billingAddressId != null)
            params.put(BILLING_ADD_ID, cartAttributes.billingAddressId);

        if (cartAttributes.billingPincode != null)
            params.put(BILLING_PINCODE, cartAttributes.billingPincode);


        return params;
    }


    public static Map<String, Object> verifyCODParams(String otp, String ptin) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(OTP, otp);
        params.put(PTIN, ptin);

        return params;
    }

    public static Map<String, Object> verifyEpayOTPParams(String otp) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(OTP, otp);
        return params;
    }

    public static Map<String, Object> financeApplicationParams(int identityDocId, int addressDocId, int businessDocId, int limit, boolean agreeScrutiny, String deviceInfo, RetailerData retailerData) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.IDENTITY_DOC_ID, identityDocId);
        params.put(WebParamsConstants.ADDRESS_DOC_ID, addressDocId);
        params.put(WebParamsConstants.BUSINESS_DOC_ID, businessDocId);
        params.put(WebParamsConstants.DESIRED_LIMIT, limit);
        params.put(WebParamsConstants.DEVICE_INFO, deviceInfo);

        params.put(WebParamsConstants.RETAILER_FULL_NAME, retailerData.name);
        params.put(WebParamsConstants.PAN_NUMBER, retailerData.panNumber);
        params.put(WebParamsConstants.DATE_OF_BIRTH, retailerData.dob);
        params.put(WebParamsConstants.GENDER, retailerData.gender);
        params.put(WebParamsConstants.EDUCATION, retailerData.education);
        params.put(WebParamsConstants.COMPANY_NAME, retailerData.companyName);
        params.put(WebParamsConstants.BUSINESS_PAN, retailerData.shopPanNumber);
        params.put(WebParamsConstants.DATE_OF_INCORPORATION, retailerData.incorporationDate);
        if (retailerData.shopEstablishmentNumber != null) {
            params.put(WebParamsConstants.SHOP_ESTABLISHMENT_NUMBER, retailerData.shopEstablishmentNumber);
        } else {
            params.put(WebParamsConstants.SHOP_ESTABLISHMENT_NUMBER, "");
        }

        params.put(WebParamsConstants.BUSINESS_ADDRESS_STREET, retailerData.shopAddress);
        params.put(WebParamsConstants.BUSINESS_ADDRESS_CITY, retailerData.shopCityId);
        params.put(WebParamsConstants.BUSINESS_ADDRESS_STATE, retailerData.shopStateId);
        params.put(WebParamsConstants.BUSINESS_ADDRESS_COUNTRY, retailerData.shopCountryId);
        params.put(WebParamsConstants.BUSINESS_ADDRESS_PINCODE, retailerData.shopPincode);

        params.put(WebParamsConstants.RESIDENCIAL_ADDRESS_STREET, retailerData.address);
        params.put(WebParamsConstants.RESIDENCIAL_ADDRESS_CITY, retailerData.cityId);
        params.put(WebParamsConstants.RESIDENCIAL_ADDRESS_STATE, retailerData.stateId);
        params.put(WebParamsConstants.RESIDENCIAL_ADDRESS_COUNTRY, retailerData.countryId);
        params.put(WebParamsConstants.RESIDENCIAL_ADDRESS_PINCODE, retailerData.pincode);


        if (agreeScrutiny) {
            params.put(WebParamsConstants.AGREE_SCRUTINY, 1);
        }
        return params;
    }

    public static MultipartRequestMap multiRequestMap(int documentId, File file) {

        MultipartRequestMap params = new MultipartRequestMap();

        params.put(WebParamsConstants.DOCUMENT_TYPE_ID, documentId + "");
        params.put(WebParamsConstants.IMAGE, file);

        return params;
    }

    public static MultipartRequestMap uploadDocumentMultiParts(int documentId, File file) {
        MultipartRequestMap params = new MultipartRequestMap();
//        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.DOCUMENT_TYPE_ID, documentId + "");
        params.put(WebParamsConstants.IMAGE, file);
        return params;
    }

    public static Map<String, Object> sendEnquiryParams(int supplierId, String comment, String duration, int productMasterId, String quantity) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.SUPPLIER_ID, supplierId);
        params.put(WebParamsConstants.COMMENTS, comment);
        params.put(WebParamsConstants.DURATION, duration);
        params.put(WebParamsConstants.PRODUCT_MASTER_ID, productMasterId);
        params.put(WebParamsConstants.QUANTITY, quantity);
        return params;
    }

    public static Map<String, Object> productsToAddinCart(List<Integer> selectedProductSKUIds, List<Integer> selectedProductQuantity) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.PRODUCT_SKU_IDS, selectedProductSKUIds);
        params.put(WebParamsConstants.PRODUCT_QUANTITY, selectedProductQuantity);
        return params;
    }

    public static Map<String, Object> productToAddinCart(int selectedProductSKUId, int selectedProductQuantity, boolean isSample) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.PRODUCT_SKU_IDS, selectedProductSKUId);
        params.put(WebParamsConstants.PRODUCT_QUANTITY, selectedProductQuantity);
        if (isSample) {
            params.put(WebParamsConstants.TYPE, WebParamsConstants.SAMPLE);
        }
        return params;
    }

    public static Map<String, Object> addProductToFavouriteParams(int productMasterId) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.PRODUCT_MASTER_ID, productMasterId + "");
        return params;
    }

    public static Map<String, Object> getPageParams(int page) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.PAGE, page);
        return params;
    }

    public static Map<String, Object> getSearchParams(String searchQuery, int page) {
            Map<String, Object> params = mapWithCommonParams();
            params.put(WebParamsConstants.PAGE, page);
            params.put(WebParamsConstants.QUERY, searchQuery);
            return params;
    }

    public static Map<String, Object> getReorderData(String odin) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.SUBODIN, odin);
        return params;
    }
    public static Map<String, Object> getReorderProductData(int id) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.PROD_MASTER_ID, id);
        return params;
    }

    public static Map<String, String> onBoardingParams() {
        Map<String, String> params = new HashMap<>();
        int st = (DataStore.isOTOVerified(PartsEazyApplication.partsEazyAppContext)) ? 0 : 1;
        params.put(WebParamsConstants.FORCE, String.valueOf(st));
        return params;
    }


    public static Map<String, Object> getFinanceParams(CartFragment.FinalCartAttributes cartAttributes) {
        Map<String, Object> params = mapWithCommonParams();
        List<Integer> productList = new ArrayList<>();
        productList.add(cartAttributes.firstProductMasterId);
        params.put(WebParamsConstants.AMOUNT, cartAttributes.amount);
        params.put(WebParamsConstants.ADDRESSID, cartAttributes.addressId);
        params.put(WebParamsConstants.PROD_MASTER_ID, productList);
        params.put(WebParamsConstants.DEVICE_INFO, cartAttributes.deviceInfo);
        return params;
    }

    public static MultipartRequestMap onBoardShop(int documentId, File file, String fileType) {

        MultipartRequestMap params = new MultipartRequestMap();

        params.put(WebParamsConstants.DOCUMENT_TYPE_ID, documentId + "");
        params.put(WebParamsConstants.IMAGE, file);
        params.put(WebParamsConstants.ENTITY, fileType);

        return params;
    }

    public static Map<String, Object> getCreateDealParams(NewDealBaseFragment.DealData dealData) {
        Map<String, Object> params = mapWithCommonParams();

        params.put(WebParamsConstants.DEAL_NAME, dealData.productName);
        params.put(WebParamsConstants.DEAL_DESC, dealData.productDescription);
        if (dealData.termsCondition != null && !dealData.termsCondition.equals("")) {
            params.put(WebParamsConstants.DEAL_TERMS_CONDITION, dealData.termsCondition);
        }

        params.put(WebParamsConstants.DEAL_L2, dealData.l2Id);
        params.put(WebParamsConstants.DEAL_L3, dealData.l3Id);
        params.put(WebParamsConstants.IMAGES, new Gson().toJson(dealData.fileDataList));
        params.put(WebParamsConstants.STARTING_DATE, CommonUtility.getCurrentDateTimeYYYYMMDDHHMMSS());
        params.put(WebParamsConstants.ENDING_DATE, dealData.endDateTime);
        params.put(WebParamsConstants.IS_PUBLIC, 1);

        params.put(WebParamsConstants.SKUS, new Gson().toJson(dealData.skuDataList));
        params.put(WebParamsConstants.PAY_METHODS, dealData.paymentMethodList);

        params.put(WebParamsConstants.ESCROW, dealData.escrow);
        params.put(WebParamsConstants.SHIP_METHODS, dealData.shipMethodList);


        params.put(WebParamsConstants.DEAL_PINCODE, dealData.pincode);
        params.put(WebParamsConstants.STREET, dealData.address);
        params.put(WebParamsConstants.COURIER_FEE, dealData.shippingCharge * 100);
        params.put(WebParamsConstants.DISPATCH, dealData.dispatchDays);
        params.put(WebParamsConstants.ALLOW_DEMO, dealData.allowDemo);


        return params;
    }

    public static Map<String, Object> updateDeal(int isPublic) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.IS_PUBLIC, isPublic);
        return params;
    }


    public static Map<String, Object> activeDealParams(int isActive) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.ACTIVE, isActive);
        return params;
    }


    public static Map<String, Object> getB2C2ContactParams(List<String> contactList) {
        Map<String, Object> params = mapWithCommonParams();

        params.put(WebParamsConstants.NUMBERS, contactList);
        return params;
    }

    public static Map<String, Object> broadcastDealParams(int dealId, String appMessage, String smsMessage, List<String> appContactList, List<String> smsContactsList) {
        Map<String, Object> params = mapWithCommonParams();

        params.put(WebParamsConstants.TRADE_ID, dealId);
        params.put(WebParamsConstants.APP_MESSAGE, appMessage);
        params.put(WebParamsConstants.SMS_MESSAGE, smsMessage);
        params.put(WebParamsConstants.APP_CONTACTS, appContactList);
        params.put(WebParamsConstants.SMS_CONTACTS, smsContactsList);

        return params;
    }


    public static Map<String, String> buyDealParams(int pageCount, int isPublic) {
        Map<String, String> params = new HashMap<>();
        params.put(WebParamsConstants.PAGE_COUNT, pageCount + "");
        params.put(WebParamsConstants.PUBLIC_DEAL, isPublic + "");
//        if (l2Category != null && !l2Category.equals("")) {
//            params.put(WebParamsConstants.L2_CATEGORY, l2Category);
//        }

        return params;
    }

    public static Map<String, String> sellDealParams(int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put(WebParamsConstants.PAGE_COUNT, pageCount + "");
        return params;
    }

    public static Map<String, String> reportCardParams(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put(WebParamsConstants.USER, userId);
        return params;
    }

    public static Map<String, Object> updateProductSkuPrice(int tp, int sp, String source) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.COST, tp);
        params.put(WebParamsConstants.PRICE, sp);
        params.put(WebParamsConstants.SOURCE, source);
        return params;
    }


    public static Map<String, Object> createBookingParams(int addressId, String shipMethod, List<BookingItem> bookingItemList) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.ADDRESS_ID, addressId);
        params.put(WebParamsConstants.SHIP_METHOD, shipMethod);
        params.put(WebParamsConstants.BOOKING_ITEMS, new Gson().toJson(bookingItemList));
        return params;
    }

    public static Map<String, Object> dealViewOpenMetricsParams(int tradeId, boolean isViewed) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.TRADE_ID, tradeId);
        if (isViewed) {
            params.put(WebParamsConstants.VIEWS, 1);
        } else {
            params.put(WebParamsConstants.OPENS, 1);
        }
        return params;
    }

    public static Map<String, Object> updateBookingParams(String payMethod) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.PAY_METHOD, payMethod);
        return params;
    }

    public static Map<String, Object> updateDealSKUParams(SkuDealModel skuData) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.SKU_DESC, skuData.desc);
        params.put(WebParamsConstants.SKU_PRICE, skuData.price);
        params.put(WebParamsConstants.SKU_REMAINING, skuData.remainingQty);
        params.put(WebParamsConstants.SKU_MRP, skuData.mrp);
        if (skuData.stock > 0) {
            params.put(WebParamsConstants.SKU_STOCK, skuData.stock);
        }
        if (skuData.remainingQty > 0) {
            params.put(WebParamsConstants.SKU_MIN_QTY, skuData.minQty);
        }
        return params;
    }

    public static MultipartRequestMap postDemoMeeting(DemoPostData demoPostData) {
//        Map<String, Object> params = mapWithCommonParams();

        MultipartRequestMap params = new MultipartRequestMap();

        params.put(WebParamsConstants.DEMO_NAME, demoPostData.name);
        params.put(WebParamsConstants.DEMO_MOBILE, demoPostData.mobile);
        params.put(WebParamsConstants.DEMO_TRADE_ID, demoPostData.tradeId + "");
        if (demoPostData.address != null) {
            params.put(WebParamsConstants.DEMO_ADDRESS, demoPostData.address);
        }
        if (demoPostData.file != null) {
            params.put(WebParamsConstants.DEMO_IMAGE, demoPostData.file);
        }
        return params;
    }


    public static Map<String, Object> getPromotionRequestParams(PromotionPostData promotionPostData) {
        Map<String, Object> params = mapWithCommonParams();

        params.put(WebParamsConstants.CREDITS_REQUIRED, promotionPostData.creditRequired);
        params.put(WebParamsConstants.CLUSTERS, promotionPostData.clusterList);
        params.put(WebParamsConstants.TRADE_ID, promotionPostData.tradeId);

        return params;
    }

    public static Map<String, String> getRetailerDistrictParams(int page, String district) {
        Map<String, String> params = new HashMap<>();

        params.put(WebParamsConstants.PAGE, page + "");
        params.put(WebParamsConstants.DISTRICTS, district);

        return params;
    }


    public static Map<String, Object> searchShopsParams(FinalSearchData searchData) {
        Map<String, Object> params = mapWithCommonParams();
        if (searchData.geoLocation != null && !searchData.geoLocation.equals("")) {
            params.put(WebParamsConstants.SEARCH_GEOPOINTS, searchData.geoLocation);
        }
        if (searchData.geoBox != null && !searchData.geoBox.equals("")) {
            params.put(WebParamsConstants.SEARCH_GEOBOXES, searchData.geoBox);
        }
        if (searchData.categoryList != null && searchData.categoryList.size() > 0) {
            params.put(WebParamsConstants.SEARCH_CATEGORY, searchData.categoryList);
        }
        if (searchData.turnOverList != null && searchData.turnOverList.size() > 0) {
            params.put(WebParamsConstants.SEARCH_TURNOVER, searchData.turnOverList);
        }
        if (searchData.shopSizeList != null && searchData.shopSizeList.size() > 0) {
            params.put(WebParamsConstants.SEARCH_SIZE, searchData.shopSizeList);
        }
        if (searchData.footfallList != null && searchData.footfallList.size() > 0) {
            params.put(WebParamsConstants.SEARCH_FOOTFALL, searchData.footfallList);
        }

        if (searchData.sortCode != null && !searchData.sortCode.equals("")) {
            params.put(WebParamsConstants.SORT_CODE, searchData.sortCode);
        }
        params.put(WebParamsConstants.PAGE, searchData.page);
        params.put(WebParamsConstants.MAP, (searchData.isMap) ? 1 : 0);
        return params;

    }

    public static Map<String, Object> offlineChatMessage(String message) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.MSG, message);
        return params;
    }

    public static Map<String, Object> updateAddress(Address address) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.LONGITUDE, address.longitude);
        params.put(WebParamsConstants.LATITUDE, address.latitude);
        return params;
    }

    public static Map<String, Object> registerCustomer(String mobile, String name, String email, String dob, String anni, String group) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.Mobile, mobile);
        if (name != null && !"".equals(name)) {
            params.put(WebParamsConstants.Name, name);
        }
        if (email != null && !"".equals(email)) {
            params.put(WebParamsConstants.Email, email);
        }
        if (dob != null && !"".equals(dob)) {
            params.put(WebParamsConstants.DATE_OF_BIRTH, dob);
        }
        if (anni != null && !"".equals(anni)) {
            params.put(WebParamsConstants.ANNIVERSARY, anni);
        }
        if (group != null && !"".equals(group)) {
            params.put(WebParamsConstants.GROUP, group);
        }
        return params;
    }

    public static Map<String, Object> insertSku(int pId, int suppId, int price, int cost, int stock) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.PRODUCT_ID, pId);
        params.put(WebParamsConstants.CUSTOMER_PRICE, price);
        params.put(WebParamsConstants.CUSTOMER_COST, cost);
        params.put(WebParamsConstants.SUPPLIER_ID, suppId);
        params.put(WebParamsConstants.ALLOW_CUSTOMER_SALE, 1);
        params.put(WebParamsConstants.CUSTOMER, 1);
        params.put(WebParamsConstants.SKU_STOCK, stock);
        return params;
    }

    public static Map<String, Object> updateSku(int price, int cost) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.CUSTOMER_PRICE, price);
        params.put(WebParamsConstants.CUSTOMER_COST, cost);
        return params;
    }

    public static Map<String, Object> changeActiveSku(int active) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.ALLOW_CUSTOMER_SALE, active);
        return params;
    }

    public static Map<String, Object> updateItem(int stock) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.BUY_STOCK, stock);
        return params;
    }

    public static Map<String, Object> postSMS(String message, ArrayList<Integer> userIDs) {
        Map<String, Object> params = mapWithCommonParams();
        params.put(WebParamsConstants.MSG, message);
        params.put(WebParamsConstants.USER_IDS, userIDs);
        return params;
    }
}

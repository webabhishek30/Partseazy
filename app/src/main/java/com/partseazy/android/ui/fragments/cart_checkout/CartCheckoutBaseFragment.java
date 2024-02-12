package com.partseazy.android.ui.fragments.cart_checkout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.cart.CartAdapter;
import com.partseazy.android.ui.fragments.cart.CartFragment;
import com.partseazy.android.ui.fragments.cart.CartHomeFragment;
import com.partseazy.android.ui.fragments.checkout.CheckoutFragment;
import com.partseazy.android.ui.fragments.shippingaddress.AddNewShippingAddressFragment;
import com.partseazy.android.ui.fragments.shippingaddress.BillingAddressFragment;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.cart_checkout.ItemsData;
import com.partseazy.android.ui.model.cart_checkout.SubItemData;
import com.partseazy.android.ui.model.createorder.CreateOrder;
import com.partseazy.android.ui.model.createorder.CreditOrder;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.AddAddress;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressList;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.android.utility.error.ErrorHandlerUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by CAN on 1/3/2017.
 */

/**
 * Base Class to handle common functioanlty of Cart and Checkout Screen
 */
public abstract class CartCheckoutBaseFragment extends BaseFragment {

    protected static FinalCartAttributes finalCartAttributes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finalCartAttributes = getFinalCartAttributes();

    }

    public FinalCartAttributes getFinalCartAttributes() {

        if (finalCartAttributes == null)
            finalCartAttributes = new FinalCartAttributes();

        return finalCartAttributes;

    }

    protected void callCartCheckoutRequest(CartFragment.FinalCartAttributes finalCartAttributes) {

        //moengage CHECKOUT CART  event
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrInt(MoengageConstant.Events.AMOUNT, finalCartAttributes.amount)
                .putAttrString(MoengageConstant.Events.ADDRESS_ID,finalCartAttributes.addressId)
                .putAttrString(MoengageConstant.Events.BILLING_ADDRESS, finalCartAttributes.billingAddressId)
                .putAttrString(MoengageConstant.Events.BILLING_PINCODE, finalCartAttributes.billingPincode)
                .putAttrString(MoengageConstant.Events.COUPON_CODE, finalCartAttributes.couponCode)
                .putAttrString(MoengageConstant.Events.DEVICE_INFO, finalCartAttributes.deviceInfo)
                .putAttrString(MoengageConstant.Events.ODIN, finalCartAttributes.oDIN)
                .putAttrString(MoengageConstant.Events.PAYMETHOD, finalCartAttributes.payMethod)
                .putAttrString(MoengageConstant.Events.PINCODE, finalCartAttributes.pincode)
                .putAttrString(MoengageConstant.Events.SHIPPING_METHOD, finalCartAttributes.ShippingMethod)
                .putAttrString(MoengageConstant.Events.SUB_PAY_METHOD, finalCartAttributes.subPayMethod)
                .putAttrInt(MoengageConstant.Events.FIRST_PRODUCT_MASTER_ID, finalCartAttributes.firstProductMasterId);
        MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.CHECKOUT, builder);
        Map<String, Object> params = WebServicePostParams.cartCheckOutRequest(finalCartAttributes);
        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.CART_CHECKOUT_ID.ordinal(),
                WebServiceConstants.PUT_CART_CHECKOUT, params, null, this, this, false);
    }

    protected void callupdateGSTNRequest(String gstn, int addressId, String billingName) {

        Map<String, Object> params = WebServicePostParams.updateGSTIN(gstn, billingName);
        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.UPDATE_GSTIN_ID.ordinal(),
                WebServiceConstants.UPDATE_ADDRESS + addressId, params, null, this, this, false);
    }

    protected void callupdateShippingNameRequest(int addressId, String billingName) {

        Map<String, Object> params = WebServicePostParams.updateShippingName(billingName);
        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.UPDATE_SHIPPING_NAME_ID.ordinal(),
                WebServiceConstants.UPDATE_ADDRESS + addressId, params, null, this, this, false);
    }


    protected void callOrderRequest(CartFragment.FinalCartAttributes finalCartAttributes) {
        showProgressBar();
        Map<String, Object> params = WebServicePostParams.createOrderParams(finalCartAttributes);
        getNetworkManager().PostRequest(RequestIdentifier.CHECKOUT_CREATE_ORDER.ordinal(),
                WebServiceConstants.CREATE_ORDER, params, null, this, this, false);

        //Moengage call order request  , moengage CHECKOUT CART create order event
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrInt(MoengageConstant.Events.AMOUNT, finalCartAttributes.amount)
                .putAttrString(MoengageConstant.Events.ADDRESS_ID,finalCartAttributes.addressId)
                .putAttrString(MoengageConstant.Events.BILLING_ADDRESS, finalCartAttributes.billingAddressId)
                .putAttrString(MoengageConstant.Events.BILLING_PINCODE, finalCartAttributes.billingPincode)
                .putAttrString(MoengageConstant.Events.COUPON_CODE, finalCartAttributes.couponCode)
                .putAttrString(MoengageConstant.Events.DEVICE_INFO, finalCartAttributes.deviceInfo)
                .putAttrString(MoengageConstant.Events.ODIN, finalCartAttributes.oDIN)
                .putAttrString(MoengageConstant.Events.PAYMETHOD, finalCartAttributes.payMethod)
                .putAttrString(MoengageConstant.Events.PINCODE, finalCartAttributes.pincode)
                .putAttrString(MoengageConstant.Events.SHIPPING_METHOD, finalCartAttributes.ShippingMethod)
                .putAttrString(MoengageConstant.Events.SUB_PAY_METHOD, finalCartAttributes.subPayMethod)
                .putAttrInt(MoengageConstant.Events.CFORM, finalCartAttributes.cForm)
                .putAttrInt(MoengageConstant.Events.FIRST_PRODUCT_MASTER_ID, finalCartAttributes.firstProductMasterId);
        MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.CHECKOUT, builder);
    }


    protected void callCreditOrder(CartFragment.FinalCartAttributes finalCartAttributes) {
        showProgressBar();
        Map<String, Object> params = WebServicePostParams.getFinanceParams(finalCartAttributes);
        getNetworkManager().PostRequest(RequestIdentifier.ORDER_CREDIT_ID.ordinal(),
                WebServiceConstants.APPLY_CREDIT, params, null, this, this, false);
    }


    /**
     * callCartUpdate()
     *
     * @param finalCartAttributes
     * @param productSKUID        product ID...
     * @param qnty                cart product quantity...
     * @param isProductSet
     */

    protected void callCartUpdate(FinalCartAttributes finalCartAttributes, int productSKUID, int qnty, boolean isProductSet) {
        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.CART_UPDATE_ID.ordinal(),
                WebServiceConstants.UPDATE_CART, WebServicePostParams.updateCartParams(finalCartAttributes, productSKUID, qnty, isProductSet), null, this, this, false);

    }


    /**
     * removeCartItemRequest()
     *
     * @param finalCartAttributes
     * @param product_master_id   product ID...
     * @param type                cart product type...
     */

    protected void removeCartItemRequest(FinalCartAttributes finalCartAttributes, String product_master_id, String type) {
        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.CART_ITEM_REMOVED_ID.ordinal(),
                WebServiceConstants.REMOVE_CART_ITEM, WebServicePostParams.removeCartItems(finalCartAttributes, product_master_id, type), null, this, this, false);
    }

    /**
     * removeCartItemRequest()
     *
     * @param finalCartAttributes
     * @param product_master_id   product ID...
     * @param type                cart product type...
     */

    protected void moveCartItemToFavRequest(FinalCartAttributes finalCartAttributes, String product_master_id, String type) {
        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.MOVE_TO_FAV_ID.ordinal(),
                WebServiceConstants.MOVE_FAV_ITEM, WebServicePostParams.removeCartItems(finalCartAttributes, product_master_id, type), null, this, this, false);
    }

    /**
     * shippingAddressListRequest...
     */

    protected void shippingAddressListRequest() {
        CustomLogger.d("Time to load Favourite");
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.SHIPPING_ADDRESS_LIST_ID.ordinal(),
                WebServiceConstants.GET_ADDRESS_LIST, null, null, this, this, false);
    }


    @Override
    final public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        hideProgressBar();
        handleCartCheckoutError(request, ErrorHandlerUtility.getAPIErrorData(error));
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, final Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.CART_CHECKOUT_ID.ordinal()) {
            hideProgressBar();

            getGsonHelper().parse(responseObject.toString(), CartCheckoutBaseData.class, new OnGsonParseCompleteListener<CartCheckoutBaseData>() {

                        @Override
                        public void onParseComplete(CartCheckoutBaseData data) {
                            //TODO: Have to change after discuss
//                            updateCartCountToolBar(data.list.size());
                            fetchCartCountHandler();

                            if (data.couponData != null && data.couponData.applied)
                                finalCartAttributes.couponCode = data.couponData.coupon;
                            else
                                finalCartAttributes.couponCode = null;

                            if (data.shipping != null)
                                finalCartAttributes.ShippingMethod = data.shipping.selected;
                            handleCartCheckoutResponse(data);

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            APIError er = new APIError();
                            er.message = exception.getMessage();
                            handleCartCheckoutError(request, er);

                        }
                    }
            );
        } else if (request.getIdentifier() == RequestIdentifier.CART_UPDATE_ID.ordinal() ||
                request.getIdentifier() == RequestIdentifier.CART_ITEM_REMOVED_ID.ordinal()) {
            callCartCheckoutRequest(finalCartAttributes);
            return true;
        } else if (request.getIdentifier() == RequestIdentifier.MOVE_TO_FAV_ID.ordinal())

        {
            PartsEazyEventBus.getInstance().postEvent(EventConstant.UPDATE_FAV_COUNT_ID);
            callCartCheckoutRequest(finalCartAttributes);
            return true;
        } else if (request.getIdentifier() == RequestIdentifier.UPDATE_GSTIN_ID.ordinal() || request.getIdentifier() == RequestIdentifier.UPDATE_SHIPPING_NAME_ID.ordinal()) {
            hideProgressBar();
            hideProgressDialog();

            getGsonHelper().parse(responseObject.toString(), AddAddress.class, new OnGsonParseCompleteListener<AddAddress>() {
                        @Override
                        public void onParseComplete(AddAddress data) {
                            handleGSTUpdateResponse(data);

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            KeyPadUtility.hideSoftKeypad(getActivity());
                            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.err_somthin_wrong));
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );

        } else if (request.getIdentifier() == RequestIdentifier.SHIPPING_ADDRESS_LIST_ID.ordinal())

        {

            hideProgressBar();
            getGsonHelper().parse(responseObject.toString(), ShippingAddressList.class, new OnGsonParseCompleteListener<ShippingAddressList>() {
                        @Override
                        public void onParseComplete(ShippingAddressList data) {
                            hideProgressDialog();
                            handleShippingListResponse(data);
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            APIError er = new APIError();
                            er.message = exception.getMessage();
                            handleShippingListError(request, er);
                        }
                    }
            );
        } else if (request.getIdentifier() == RequestIdentifier.CHECKOUT_CREATE_ORDER.ordinal()) {


            getGsonHelper().parse(responseObject.toString(), CreateOrder.class, new OnGsonParseCompleteListener<CreateOrder>() {
                        @Override
                        public void onParseComplete(CreateOrder data) {
                            hideProgressBar();
                            PartsEazyApplication.getInstance().setCartCount(0);
                            handleOrderCreateResponse(data);
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressBar();
                            APIError er = new APIError();
                            er.message = exception.getMessage();
                            handleCreateOrderError(request, er);

                        }
                    }
            );

            return true;
        } else if (request.getIdentifier() == RequestIdentifier.ORDER_CREDIT_ID.ordinal()) {
            hideProgressBar();

            getGsonHelper().parse(responseObject.toString(), CreditOrder.class, new OnGsonParseCompleteListener<CreditOrder>() {

                        @Override
                        public void onParseComplete(CreditOrder data) {
                            handleCreditOrderCreateResponse(data);
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            APIError er = new APIError();
                            er.message = exception.getMessage();
                            handleCartCheckoutError(request, er);

                        }
                    }
            );
        }


        return true;
    }


    protected boolean isOnlyServiceableError(CartCheckoutBaseData cartCheckoutBaseData) {


        if (!cartCheckoutBaseData._continue) {


            for (ItemsData list : cartCheckoutBaseData.list) {

                //Now parent list can contain this type of errors - Max, MOQ and Stock
                if (!TextUtils.isEmpty(list.error_type)) {
                    return false;
                }

                for (SubItemData data : list.rows) {

                    if (!data.error_type.equals(CartAdapter.ERROR_TYPE.SERVICEABLE.getErrorType()) && !TextUtils.isEmpty(data.error_type)) {
                        return false;
                    }
                }

            }
        }

        return true;

    }


    //Should be overrided by subclasses

    protected boolean handleCartCheckoutResponse(CartCheckoutBaseData cartCheckoutBaseData) {
        return false;
    }

    protected boolean handleGSTUpdateResponse(AddAddress billingAddress) {
        return false;
    }

    protected boolean handleOrderCreateResponse(CreateOrder cartCheckoutBaseData) {
        return false;
    }

    protected boolean handleCreditOrderCreateResponse(CreditOrder creditOrder) {
        return false;
    }

    protected boolean handleCartCheckoutError(Request request, APIError error) {
        return false;
    }


    protected boolean handleShippingListResponse(ShippingAddressList data) {
        return false;
    }

    protected boolean handleShippingListError(Request request, APIError error) {
        return false;
    }

    protected boolean handleCreateOrderError(Request request, APIError error) {
        return false;
    }


    public static class FinalCartAttributes {
        public String couponCode;
        public Integer cForm;
        public String ShippingMethod;
        public String payMethod;
        public String pincode;
        public String addressId;
        public String subPayMethod;
        public String oDIN;
        public int firstProductMasterId;
        public int amount;
        public String deviceInfo;
        public String billingAddressId;
        public String billingPincode;

    }


    protected void showAlertDialog(String notServiableList, final boolean isFromCheckout) {

        String strMsg = getString(R.string.not_servicable) + notServiableList;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setMessage(strMsg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.change_address), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        BaseFragment fragment = AddNewShippingAddressFragment.newInstance(AddNewShippingAddressFragment.FROM_CART);

                        if (!isFromCheckout) {
                            BaseFragment.addToBackStack((BaseActivity) getActivity(), fragment, AddNewShippingAddressFragment.getTagName());
                            fragment.setTargetFragment(CartCheckoutBaseFragment.this, 0);
                        } else {
                            BaseFragment.removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, AddNewShippingAddressFragment.getTagName());
                        }
                    }
                })
                .setNegativeButton(getString(R.string.go_to_cart), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        BaseFragment fragment = CartHomeFragment.newInstance(false);
                        if (!isFromCheckout) {
                            BaseFragment.addToBackStack((BaseActivity) getActivity(), fragment, CartHomeFragment.getTagName());
                            fragment.setTargetFragment(CartCheckoutBaseFragment.this, 0);
                        } else {
                            BaseFragment.removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, CartHomeFragment.getTagName());

                        }
                        //context.onPopBackStack(true);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        AddNewShippingAddressFragment fragment = (AddNewShippingAddressFragment) getFragmentManager().findFragmentByTag(AddNewShippingAddressFragment.getTagName());

        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
        }

    }

    protected boolean isListContainMethod(List<ItemsData> itemsDataList) {

        for (ItemsData list : itemsDataList) {

            for (SubItemData data : list.rows) {

                if (data.error_type.equals(CartAdapter.ERROR_TYPE.SERVICEABLE.getErrorType())) {
                    return false;
                }
            }

        }
        return true;

    }


    protected boolean isShippingValid(CartCheckoutBaseData cartCheckoutBaseData) {

        List<ItemsData> itemsDataList = cartCheckoutBaseData.list;

        if (itemsDataList != null) {

            return isListContainMethod(itemsDataList);
        }

        return false;
    }

    protected void checkForShipeableItem(CartCheckoutBaseData cartCheckoutBaseData, ShippingAddressDetail addressDetails, int index, boolean isFromCart, boolean isSameBillingAdd) {

        if (isShippingValid(cartCheckoutBaseData) && cartCheckoutBaseData._continue) {

            context.onPopBackStack(true);
            if (isSameBillingAdd) {
                CheckoutFragment checkoutFragment = CheckoutFragment.newInstance(addressDetails, index, addressDetails, index);
                if (getActivity() != null)
                    addToBackStack((BaseActivity) getActivity(), checkoutFragment, checkoutFragment.getTagName());

            } else {
                BillingAddressFragment billingAddressFragment = BillingAddressFragment.newInstance(addressDetails, index);
                if (getActivity() != null)
                    addToBackStack((BaseActivity) getActivity(), billingAddressFragment, billingAddressFragment.getTagName());
            }
        } else {

            launchNonShippableItemsAlertDialog(cartCheckoutBaseData, isFromCart);
        }

    }


    protected void launchNonShippableItemsAlertDialog(CartCheckoutBaseData cartCheckoutBaseData, boolean isFromCart) {

        String serivable = null;
        List<ItemsData> itemsDataList = cartCheckoutBaseData.list;

        for (int i = 0; i < itemsDataList.size(); i++) {
            if (serivable == null)
                serivable = "\n * " + itemsDataList.get(i).name;
            else
                serivable = serivable + "\n * " + itemsDataList.get(i).name;
        }

        showAlertDialog(serivable, isFromCart);

    }
}

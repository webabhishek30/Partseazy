package com.partseazy.android.ui.fragments.checkout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.constants.IntentConstants;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.adapters.checkout.CheckoutAdapter;
import com.partseazy.android.ui.fragments.cart_checkout.CartCheckoutBaseFragment;
import com.partseazy.android.ui.fragments.complete.ThankYouFragment;
import com.partseazy.android.ui.fragments.failure.OrderFailureFragment;
import com.partseazy.android.ui.fragments.payment.CODOTPFragment;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.createorder.CreateOrder;
import com.partseazy.android.ui.model.createorder.CreditOrder;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.payment.EpayCODResult;
import com.partseazy.android.ui.model.payu.PayUData;
import com.partseazy.android.ui.model.payu.PgParams;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;

import org.json.JSONObject;

import java.io.Serializable;

import butterknife.BindView;

/**
 * Created by CAN on 1/3/2017.
 */

public class CheckoutFragment extends CartCheckoutBaseFragment {


    @BindView(R.id.scrollable)
    protected RecyclerView scrollable;




    private CheckoutAdapter checkoutAdapter;
    private ShippingAddressDetail shippingAddressDetails;
    private ShippingAddressDetail billingAddressDetails;
    private CartCheckoutBaseData data;

    private int selectedShippingIndex;
    private int selectedBillingIndex;
    private String pincode;
    private final static String SHIPPING_ADD = "shipping_address";
    private final static String SHIPPING_ADD_INDEX= "shipping_address_index";
    private final static String BILLING_ADD = "billing_address";
    private final static String BILLING_ADD_INDEX = "billing_address_index";
    public final static String PAYMENT_INDEX = "PAYMENT_INDEX";


    private Parcelable recyclerViewState;

    public static CheckoutFragment newInstance(ShippingAddressDetail shippingAddressDetails, int shippingAddressIndex,ShippingAddressDetail billingAddress,int billingAddessIndex) {
        Bundle bundle = new Bundle();
        CheckoutFragment fragment = new CheckoutFragment();
        bundle.putSerializable(SHIPPING_ADD, (Serializable) shippingAddressDetails);
        bundle.putSerializable(BILLING_ADD, (Serializable) billingAddress);
        bundle.putInt(SHIPPING_ADD_INDEX, shippingAddressIndex);
        bundle.putInt(BILLING_ADD_INDEX, billingAddessIndex);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_home;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.payment_header);
    }

    public static String getTagName() {
        return CheckoutFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(IntentConstants.OTP));
        selectedShippingIndex = getArguments().getInt(SHIPPING_ADD_INDEX);
        selectedBillingIndex = getArguments().getInt(BILLING_ADD_INDEX);
        shippingAddressDetails = (ShippingAddressDetail) getArguments().getSerializable(SHIPPING_ADD);
        billingAddressDetails = (ShippingAddressDetail)getArguments().getSerializable(BILLING_ADD);
        pincode = shippingAddressDetails.pincode;
        finalCartAttributes.addressId = "" + shippingAddressDetails.id;
        finalCartAttributes.billingAddressId = billingAddressDetails.id+"";
        finalCartAttributes.billingPincode = billingAddressDetails.pincode;
        CustomLogger.d("shippingAddressDetails ::" + finalCartAttributes.addressId + "orderIn:: " + finalCartAttributes.billingPincode);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        toolbar.setTitle(R.string.payment_header);
        loadCheckoutAPI();
    }

    private void loadCheckoutAPI() {

        if (data == null) {
            callCartCheckoutRequest(finalCartAttributes);
        } else
            initAdapter();

    }

    protected void callPayUChecKSumGenerationRequest(String pTin, String orderID) {


        //Agent Based handling
//        if (DataStore.isAgentType(context)) {
//            AgentSingleton agentSingleton = AgentSingleton.getInstance();
//            if (agentSingleton != null && agentSingleton.getRetailerData() != null) {
//                showThankYouFragment(orderID);
//            }
//        } else {
        getNetworkManager().GetRequest(RequestIdentifier.PAYU_CHECKSUM.ordinal(),
                WebServiceConstants.PAYU_CHECECKSUM + pTin, null, null, this, this, false);
        // }
    }


    private void sendEpayOTPrequest() {

        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.OTP_EPAY_REQUEST_ID.ordinal(),
                WebServiceConstants.SEND_EPAY_OTP, null, null, this, this, false);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    private void initAdapter() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        scrollable.setLayoutManager(llm);
        scrollable.setHasFixedSize(true);


        if (checkoutAdapter == null) {
            checkoutAdapter = new CheckoutAdapter(this, data,shippingAddressDetails,selectedShippingIndex,billingAddressDetails,selectedBillingIndex);
            scrollable.setAdapter(checkoutAdapter);

        } else {
            scrollable.setAdapter(checkoutAdapter);
            checkoutAdapter.updateData(data);
            if (recyclerViewState != null)
                scrollable.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        }
    }


////
//public class CartCheckoutBaseData {
//
//    @SerializedName("messages")
//    @Expose
//    public java.util.List<String> messages = null;
//    @SerializedName("list")
//    @Expose
//    public java.util.List<ItemsData> list = null;
//    @SerializedName("summary")
//    @Expose
//    public SummaryData summary;
//    @SerializedName("shipping")
//    @Expose
//    public ShippingData shipping;
//    @SerializedName("payment")
//    @Expose
//    public PaymentData payment;
//    @SerializedName("continue")
//    @Expose
//    public Boolean _continue;
//
//    @SerializedName("coupon")
//    @Expose
//    public CouponData couponData;
//
//
//    @SerializedName("count")
//    @Expose
//    public int cartCount;
//
//    public String selectedAddresssId;
//
//    public String selectedPaymentMethod;
//}

    @Override
    protected boolean handleCartCheckoutResponse(CartCheckoutBaseData cartCheckoutBaseData) {

        //Moengage payment checkout data

        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrObject(MoengageConstant.Events.ITEMS, cartCheckoutBaseData.list.toArray())
                .putAttrObject(MoengageConstant.Events.SUMMARY_DATA,cartCheckoutBaseData.summary)
                .putAttrObject(MoengageConstant.Events.SHIPPING_DATA, cartCheckoutBaseData.shipping)
                .putAttrObject(MoengageConstant.Events.PAYMENT_DATA, cartCheckoutBaseData.payment)
                .putAttrObject(MoengageConstant.Events.COUPON_DATA, cartCheckoutBaseData.couponData)
                .putAttrObject(MoengageConstant.Events.CART_COUNT, cartCheckoutBaseData.cartCount);
        MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.CART_CHECKOUT, builder);

        data = cartCheckoutBaseData;
        if (data != null) {
            initAdapter();

            if (!isShippingValid(cartCheckoutBaseData) && !cartCheckoutBaseData._continue) {

                launchNonShippableItemsAlertDialog(cartCheckoutBaseData, true);

            }
        }
        return true;
    }

    @Override
    protected boolean handleCartCheckoutError(Request request, APIError error) {
        showError(error.message, MESSAGETYPE.TOAST);
        return true;
    }

    @Override
    protected boolean handleOrderCreateResponse(CreateOrder cartCheckoutBaseData) {
        if (cartCheckoutBaseData != null && cartCheckoutBaseData.orderPayment != null && cartCheckoutBaseData.orderPayment.ptin != null)
            launchNextScreenAfterOrder(cartCheckoutBaseData.orderPayment.ptin, cartCheckoutBaseData.orderMaster.odin);
        return true;
    }

    @Override
    protected boolean handleCreateOrderError(Request request, APIError error) {
        showError(error.message, MESSAGETYPE.TOAST);
        return true;
    }

    @Override
    protected boolean handleCreditOrderCreateResponse(CreditOrder creditOrder) {
        if (creditOrder.customerApproved && creditOrder.customerRegister) {
            PartsEazyEventBus.getInstance().postEvent(EventConstant.CHECKOUT_CREDIT_ORDER, true);
        } else {
            PartsEazyEventBus.getInstance().postEvent(EventConstant.CHECKOUT_CREDIT_ORDER, false);
        }

        return true;
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.CHECKOUT_POST_SELECTED_SHIPPING) {
            String shippingMethod = (String) eventObject.objects[0];
            finalCartAttributes.ShippingMethod = shippingMethod;
            recyclerViewState = scrollable.getLayoutManager().onSaveInstanceState();
            callCartCheckoutRequest(finalCartAttributes);
        }

        if (eventObject.id == EventConstant.CHECKOUT_POST_PROMOCODE) {
            String promoCode = (String) eventObject.objects[0];
            finalCartAttributes.couponCode = promoCode;
            recyclerViewState = scrollable.getLayoutManager().onSaveInstanceState();
            callCartCheckoutRequest(finalCartAttributes);

        }


        if (eventObject.id == EventConstant.PLACE_ORDER) {
            if (!isShippingValid(data) && !data._continue) {
                launchNonShippableItemsAlertDialog(data, true);
                return;
            }


            String parentPay = (String) eventObject.objects[0];
            if (eventObject.objects[1] != null)
                finalCartAttributes.subPayMethod = (String) eventObject.objects[1];

            if (parentPay.equalsIgnoreCase(CODFragment.key)) {
                finalCartAttributes.payMethod = "cod";
            } else if (parentPay.equalsIgnoreCase(PrepaidFragment.key)) {
                String payMethod = (String) eventObject.objects[1];
                finalCartAttributes.payMethod = payMethod;
            } else if (parentPay.equalsIgnoreCase(CreditFragment.key)) {
                //Just for testing
                finalCartAttributes.payMethod = "fin";
            }


            callOrderRequest(finalCartAttributes);
        }


        if (eventObject.id == EventConstant.CREDIT_ORDER) {
            if (!isShippingValid(data) && !data._continue) {
                launchNonShippableItemsAlertDialog(data, true);
                return;
            }

            String parentPay = (String) eventObject.objects[0];
            if (eventObject.objects[1] != null)
                finalCartAttributes.subPayMethod = (String) eventObject.objects[1];

            if (parentPay.equalsIgnoreCase(CODFragment.key)) {
                finalCartAttributes.payMethod = "cod";
            } else if (parentPay.equalsIgnoreCase(PrepaidFragment.key)) {
                String payMethod = (String) eventObject.objects[1];
                finalCartAttributes.payMethod = payMethod;
            } else if (parentPay.equalsIgnoreCase(CreditFragment.key)) {
                finalCartAttributes.payMethod = "fin";
            }

            if (eventObject.objects[2] != null)
                finalCartAttributes.amount = (Integer) eventObject.objects[2];

            finalCartAttributes.firstProductMasterId = data.list.get(0).productMasterId;

            finalCartAttributes.deviceInfo = CommonUtility.financeDeviceDetail(context);

            callCreditOrder(finalCartAttributes);

        }


    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.PAYU_CHECKSUM.ordinal()) {


            getGsonHelper().parse(responseObject.toString(), PayUData.class, new OnGsonParseCompleteListener<PayUData>() {
                        @Override
                        public void onParseComplete(PayUData data) {
                            hideProgressDialog();
                            initPayUPayment(data, finalCartAttributes);

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            APIError er = new APIError();
                            er.message = exception.getMessage();

                        }
                    }
            );

        }

        if (request.getIdentifier() == RequestIdentifier.OTP_EPAY_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), EpayCODResult.class, new OnGsonParseCompleteListener<EpayCODResult>() {
                        @Override
                        public void onParseComplete(EpayCODResult data) {
                            hideProgressDialog();
                            if (data.success == 1) {
                                //initView();
                                CustomLogger.d("EPAY LAter " + data.success);
                                launchOtpScrreen("", finalCartAttributes.oDIN, finalCartAttributes.payMethod);
                            } else {
                                DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.otp_not_recieved), getString(R.string.OK)
                                        , null, null, new DialogListener() {
                                            @Override
                                            public void onPositiveButton(DialogInterface dialog) {
                                            }
                                        });
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            APIError er = new APIError();
                            er.message = exception.getMessage();

                        }
                    }
            );

        }


        return super.handleResponse(request, responseObject, response);

    }


    private void launchNextScreenAfterOrder(String pTIN, String odin) {

        finalCartAttributes.oDIN = odin;
        if (finalCartAttributes.payMethod.equals("cod")) {

            if (finalCartAttributes.subPayMethod != null)
                callPayUChecKSumGenerationRequest(pTIN, odin);
            else
                launchOtpScrreen(pTIN, odin, finalCartAttributes.payMethod);

        } else if (finalCartAttributes.payMethod.equals("fin")) {

            //  launchOtpScrreen(pTIN, odin,finalCartAttributes.payMethod);
            sendEpayOTPrequest();

        } else {//prepaid payments
            callPayUChecKSumGenerationRequest(pTIN, odin);
        }

    }

    private void launchOtpScrreen(String pTIN, String odin, String paymentMethod) {


        if (getActivity() != null) {
            CODOTPFragment fragment = CODOTPFragment.newInstance(pTIN, odin, paymentMethod);
            removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, CODOTPFragment.getTagName());

        }
    }

    private void initPayUPayment(PayUData payUData, FinalCartAttributes finalCartAttributes) {
        Payu.setInstance(getContext());
        PgParams data = payUData.pgParams;
        PaymentParams mPaymentParams = new PaymentParams();
        //Todo: just for testing
        mPaymentParams.setKey(data.key);
        mPaymentParams.setAmount(data.amount);
        mPaymentParams.setProductInfo(data.productinfo);
        mPaymentParams.setFirstName(data.firstname);
        mPaymentParams.setEmail(data.email);
        mPaymentParams.setTxnId(data.txnid);
        //Todo : for testing
        mPaymentParams.setSurl(data.surl);
        mPaymentParams.setFurl(data.furl);
        mPaymentParams.setUdf1(data.udf1);
        mPaymentParams.setUdf2(data.udf2);
        mPaymentParams.setUdf3(data.udf3);
        mPaymentParams.setUdf4(data.udf4);
        mPaymentParams.setUdf5(data.udf5);


        PayuHashes mPayUHashes = new PayuHashes();
        mPayUHashes.setPaymentHash(data.paymentHash);

        mPaymentParams.setHash(data.paymentHash);

        mPayUHashes.setPaymentRelatedDetailsForMobileSdkHash(data.paymentRelatedDetailsForMobileSdkHash);
        mPayUHashes.setCreateInvoiceHash(data.getMerchantIbiboCodesHash);
        mPayUHashes.setVasForMobileSdkHash(data.vasForMobileSdkHash);


        PayuConfig payuConfig = new PayuConfig();

        if (payUData.environment.equalsIgnoreCase("TEST"))
            payuConfig.setEnvironment(PayuConstants.MOBILE_STAGING_ENV);
        else {
            payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);

        }

        Intent intent = new Intent(getContext(), PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, mPayUHashes);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);

        if (finalCartAttributes.subPayMethod != null) {

            int PAGE_INDEX = 0;
            if (finalCartAttributes.subPayMethod.equals(StaticMap.PAYMENY_PREPAID_CC_DC_KEY)) {
                PAGE_INDEX = 0;
            } else if (finalCartAttributes.subPayMethod.equals(StaticMap.PAYMENY_PREPAID_NB_KEY)) {
                PAGE_INDEX = 1;

            } else if (finalCartAttributes.subPayMethod.equals(StaticMap.PAYMENY_PREPAID_UPI_KEY)) {
                PAGE_INDEX = 2;
            }

            intent.putExtra(PAYMENT_INDEX, PAGE_INDEX);


        }
        if (getActivity() != null && isAdded())
            showProgressDialog();
        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        hideProgressDialog();
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showPaymentError(AppConstants.TRANSACTION_CANCELLED_BY_USER);

            } else {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {

                        PartsEazyApplication.getInstance().setCartCount(0);
                        updateCartCountToolBar(PartsEazyApplication.getInstance().getCartCount());
                        CustomLogger.d("result_data=>" + data.toString());
                        CustomLogger.d("payment=>" + data.getStringExtra("result"));
                        showThankYouFragment(finalCartAttributes.oDIN);


                    } else {
                        showPaymentError(null);
                    }
                }
            }
        }
    }


    private void showPaymentError(final String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popToMall(getActivity());
                if (msg != null && msg.contains(AppConstants.TRANSACTION_CANCELLED_BY_USER)) {

                    addToBackStack(getContext(), OrderFailureFragment.newInstance(true), OrderFailureFragment.class.getSimpleName());
                } else {
                    if (msg == null) {
                        SnackbarFactory.showSnackbar(getContext(), getString(R.string.payu_payment_failed));
                    } else {
                        SnackbarFactory.showSnackbar(getContext(), msg);
                    }
                    addToBackStack(getContext(), OrderFailureFragment.newInstance(false), OrderFailureFragment.class.getSimpleName());
                }

            }
        }, 200);
    }

    public void showThankYouFragment(String odin) {
        hideProgressDialog();
        popToMall(getActivity());
        addToBackStack(getContext(), ThankYouFragment.newInstance(odin), ThankYouFragment.getTagName());
    }


    @Override
    public void onResume() {
        super.onResume();
        PartsEazyEventBus.getInstance().addObserver(this);
        hideKeyBoard(getView());

    }

    @Override
    public void onPause() {
        super.onPause();
        PartsEazyEventBus.getInstance().removeObserver(this);
    }
}

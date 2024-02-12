package com.partseazy.android.ui.fragments.deals.booking_deal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.dealbookings.BookingPrepaidAdapter;
import com.partseazy.android.ui.fragments.account.BookingDetailFragment;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.model.deal.booking.BookingResult;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.payu.PayUData;
import com.partseazy.android.ui.model.payu.PgParams;
import com.partseazy.android.ui.model.prepaid.PrepaidData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.partseazy.android.ui.fragments.checkout.CheckoutFragment;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 2/6/17.
 */

public class ThankYouBookingFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    @BindView(R.id.bookingIdTV)
    protected TextView bookingIdTV;

    @BindView(R.id.firstCheckIV)
    protected ImageView firstCheckIV;

    @BindView(R.id.secondCheckIV)
    protected ImageView secondCheckIV;

    @BindView(R.id.thirdCheckIV)
    protected ImageView thirdCheckIV;

    @BindView(R.id.secondLineView)
    protected View secondLineView;

    @BindView(R.id.firstLineView)
    protected View firstLineView;

    @BindView(R.id.paymentAmtTV)
    protected TextView paymentAmtTV;

    @BindView(R.id.dealNameTV)
    protected TextView dealNameTV;

    @BindView(R.id.productIconIV)
    protected NetworkImageView productIconIV;


    @BindView(R.id.skuPriceTV)
    protected TextView skuPriceTV;

    @BindView(R.id.skuMrpTV)
    protected TextView skuMrpTV;

    @BindView(R.id.skuDiscountTV)
    protected TextView skuDiscountTV;

    @BindView(R.id.dealerNameTV)
    protected TextView dealerNameTV;

    @BindView(R.id.skuRV)
    protected RecyclerView skuRV;


    @BindView(R.id.singleSkuLL)
    protected LinearLayout singleSkuLL;

    @BindView(R.id.timerTV)
    protected TextView timerTV;

    @BindView(R.id.continueBT)
    protected Button continueBT;

    @BindView(R.id.bookingDetailTV)
    protected TextView bookingDetailTV;

    @BindView(R.id.paymentRG)
    protected RadioGroup paymentRG;

    @BindView(R.id.prepaidRB)
    protected RadioButton prepaidRB;

    @BindView(R.id.cashRB)
    protected RadioButton cashRB;

    @BindView(R.id.directRB)
    protected RadioButton directRB;

    @BindView(R.id.prepaidRV)
    protected RecyclerView prepaidRV;


    public static String BOOKING_DATA = "total_amount";
    public static String DEAL_HOLDER = "deal_holder";
    public final static String key = "PREPAID";

    private BookingResult bookingResult;
    private Deal dealDetailHolder;
    private String paymentMode = "";
    private String paymentMethod="card";
    private BookingPrepaidAdapter bookingPrepaidAdapter;
    private boolean showPaymentToTop = true;


    public static ThankYouBookingFragment newInstance(String bookingResultJson, String dealDetailHolder) {
        Bundle bundle = new Bundle();
        ThankYouBookingFragment fragment = new ThankYouBookingFragment();
        bundle.putString(BOOKING_DATA, bookingResultJson);
        bundle.putString(DEAL_HOLDER, dealDetailHolder);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String bookingResultJson = getArguments().getString(BOOKING_DATA);
        bookingResult = new Gson().fromJson(bookingResultJson, BookingResult.class);

        String detailDetailJson = getArguments().getString(DEAL_HOLDER);
        dealDetailHolder = new Gson().fromJson(detailDetailJson, Deal.class);
    }

    public static String getTagName() {
        return ThankYouBookingFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_thank_you_booking;
    }


    @Override
    protected String getFragmentTitle() {
        return getString(R.string.booking_created);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showBackNavigation();
        setDataToView();
        initPrepaidOptionAdapter();

        paymentRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                continueBT.setEnabled(true);

                if (checkedId == cashRB.getId()) {
                    paymentMode = "cash";
                    prepaidRV.setVisibility(View.GONE);
                } else if (checkedId == directRB.getId()) {
                    paymentMode = "direct";
                    prepaidRV.setVisibility(View.GONE);
                } else if (checkedId == prepaidRB.getId()) {
                    CustomLogger.d("Hello Prepaid ");
                    paymentMode=key;
                    prepaidRV.setVisibility(View.VISIBLE);
                }


            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProgress();
        bookingDetailTV.setOnClickListener(this);
        continueBT.setOnClickListener(this);

    }


    private void setProgress() {
        firstCheckIV.setImageResource(R.drawable.check_green_circle);
        secondCheckIV.setImageResource(R.drawable.check_green_circle);
        thirdCheckIV.setImageResource(R.drawable.check_green_circle);
        firstLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));
        secondLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));
    }

    private void setDataToView() {
        bookingIdTV.setText(getString(R.string.booking_id, bookingResult.booking.bkin));
        paymentAmtTV.setText(getString(R.string.payment_due, CommonUtility.convertionPaiseToRupee(bookingResult.booking.payable)));
    }


    private void initPrepaidOptionAdapter() {
        //should be driven by fetauremap
        List<PrepaidData> dataList = new ArrayList<>();

        dataList.addAll(CommonUtility.getPrepaidOptionList());
        dataList.get(0).setChooseOption(1);
        if (bookingPrepaidAdapter == null)
            bookingPrepaidAdapter = new BookingPrepaidAdapter(getContext(), dataList, key,false);
        prepaidRV.setLayoutManager(new LinearLayoutManager(context));
        prepaidRV.setAdapter(bookingPrepaidAdapter);


    }

    private void callUpdateBookingPaymentMode()
    {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        showProgressDialog();
        getNetworkManager().PutRequest(RequestIdentifier.UPDATE_BOOKING.ordinal(),
                WebServiceConstants.UPDATE_BOOKING + bookingResult.booking.bkin,
                WebServicePostParams.updateBookingParams(paymentMode),
                params, ThankYouBookingFragment.this, ThankYouBookingFragment.this, false);

    }



    private void callInitiateBookingPayment(String bkin,String paymentMethod)
    {
       // RequestParams params = new RequestParams();
//        params.headerMap = new HashMap<>();
//        WebServicePostParams.addResultWrapHeader(params.headerMap);
        showProgressBar();
        String initiatePaymentUrl = WebServiceConstants.BOOKING_INITIATE_PAYMENT+"?bkin="+bkin+"&method="+paymentMethod;
        getNetworkManager().GetRequest(RequestIdentifier.BOOKING_INITIATE_PAYMENT.ordinal(),
                initiatePaymentUrl, null, null, this, this, false);


    }




    @Override
    final public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        hideProgressBar();
        showError();
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, final Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.UPDATE_BOOKING.ordinal()) {
            hideProgressBar();
            hideProgressDialog();
//            popToHome(getActivity());
            addToBackStack(getContext(), BookingDetailFragment.newInstance(bookingResult.booking.bkin, false,false), BookingDetailFragment.class.getSimpleName());
        }


        if (request.getIdentifier() == RequestIdentifier.BOOKING_INITIATE_PAYMENT.ordinal()) {


            getGsonHelper().parse(responseObject.toString(), PayUData.class, new OnGsonParseCompleteListener<PayUData>() {
                        @Override
                        public void onParseComplete(PayUData data) {
                            hideProgressDialog();
                           CustomLogger.d("Hello Payment Gateway");
                            initPayUPayment(data,paymentMethod);

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

        return true;
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == bookingDetailTV.getId()) {
            addToBackStack(getContext(), BookingDetailFragment.newInstance(bookingResult.booking.bkin, false,false), BookingDetailFragment.class.getSimpleName());
        }

        if (view.getId() == continueBT.getId()) {
            if ("".equals(paymentMode)) {
                showMessage(getString(R.string.select_payment_mode), MESSAGETYPE.TOAST);
                return;
            }else if(key.equals(paymentMode))
            {
                if(paymentMethod!= null && !paymentMethod.equals("")) {
                    callInitiateBookingPayment(bookingResult.booking.bkin, paymentMethod);
                }else{
                    showMessage(getString(R.string.select_payment_option), MESSAGETYPE.TOAST);
                    return;
                }

            }else {
                callUpdateBookingPaymentMode();
            }

        }
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.DEAL_BOOKING_PAYMENT_METHOD) {
            paymentMethod = (String) eventObject.objects[0];
           // String parentPaymentMethod = (String) eventObject.objects[2];

        }
    }



    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        addToBackStack(getContext(), DealHomeFragment.newInstance(false), DealHomeFragment.class.getSimpleName());
        return false;
    }


    private void initPayUPayment(PayUData payUData, String paymentMethod) {
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


            int PAGE_INDEX = 0;
            if (paymentMethod.equals(StaticMap.PAYMENY_PREPAID_CC_DC_KEY)) {
                PAGE_INDEX = 0;
            } else if (paymentMethod.equals(StaticMap.PAYMENY_PREPAID_NB_KEY)) {
                PAGE_INDEX = 1;

            } else if (paymentMethod.equals(StaticMap.PAYMENY_PREPAID_UPI_KEY)) {
                PAGE_INDEX = 2;
            }
            intent.putExtra(CheckoutFragment.PAYMENT_INDEX, PAGE_INDEX);


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

                        addToBackStack(getContext(), BookingDetailFragment.newInstance(bookingResult.booking.bkin, false,showPaymentToTop), BookingDetailFragment.class.getSimpleName());
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
               // popToHome(getActivity());
                if (msg != null && msg.contains(AppConstants.TRANSACTION_CANCELLED_BY_USER)) {
                    addToBackStack(getContext(), BookingDetailFragment.newInstance(bookingResult.booking.bkin, false,showPaymentToTop), BookingDetailFragment.class.getSimpleName());
                    // addToBackStack(getContext(), OrderFailureFragment.newInstance(true), OrderFailureFragment.class.getSimpleName());
                } else {
                    if (msg == null) {
                        SnackbarFactory.showSnackbar(getContext(), getString(R.string.payu_payment_failed));
                    } else {
                        SnackbarFactory.showSnackbar(getContext(), msg);
                    }
                    addToBackStack(getContext(), BookingDetailFragment.newInstance(bookingResult.booking.bkin, false,showPaymentToTop), BookingDetailFragment.class.getSimpleName());

                }

            }
        }, 200);
    }
}
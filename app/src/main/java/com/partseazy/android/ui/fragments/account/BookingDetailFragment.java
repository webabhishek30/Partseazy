package com.partseazy.android.ui.fragments.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.partseazy.android.ui.adapters.account.BookingSummaryAdapter;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.model.deal.bookingorder.summary.BookingSummaryResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.payu.PayUData;
import com.partseazy.android.ui.model.payu.PgParams;
import com.partseazy.android.utility.ChatUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;
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
 * Created by naveen on 7/6/17.
 */

public class BookingDetailFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.scrollable)
    protected RecyclerView recyclerView;

    @BindView(R.id.chatBT)
    protected Button chatBT;

    public static final String BOOKING_ID = "booking_id";
    public static final String BACK_TO_BOOKINGS = "back_to_bookings";
    public static final String SHOW_PAYMENT_CARD_TO_TOP = "show_payment_to_top";

    private BookingSummaryAdapter bookingSummaryAdapter;
    private String bookingId;
    private BookingSummaryResult bookingSummaryDetail;
    private String paymentMethod = "card";

    private boolean backToMyBookings = true;
    private boolean showPaymentToTop = false;

    private List<String> bookingList;


    public static BookingDetailFragment newInstance(boolean backToMyBookings) {
        Bundle bundle = new Bundle();
        BookingDetailFragment fragment = new BookingDetailFragment();
        bundle.putBoolean(BACK_TO_BOOKINGS, backToMyBookings);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BookingDetailFragment newInstance(String bkin, boolean backToMyBookings, boolean showPaymentToTop) {
        Bundle bundle = new Bundle();
        BookingDetailFragment fragment = new BookingDetailFragment();
        bundle.putString(BOOKING_ID, bkin);
        bundle.putBoolean(BACK_TO_BOOKINGS, backToMyBookings);
        bundle.putBoolean(SHOW_PAYMENT_CARD_TO_TOP, showPaymentToTop);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingId = getArguments().getString(BOOKING_ID);
        backToMyBookings = getArguments().getBoolean(BACK_TO_BOOKINGS, backToMyBookings);
        showPaymentToTop = getArguments().getBoolean(SHOW_PAYMENT_CARD_TO_TOP, showPaymentToTop);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (bookingSummaryDetail != null) {
            setOrderAdapter();
        } else {
            loadBookingData();
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_booking_detail;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.booking_summary);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatBT.setOnClickListener(this);

    }

    private void loadBookingData() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        getNetworkManager().GetRequest(RequestIdentifier.BOOKIND_DETAIL_SUMMARY.ordinal(),
                WebServiceConstants.GET_BOOKING_DETAILS + bookingId, null, params, this, this, false);
    }

    private void callInitiateBookingPayment(String bkin, String paymentMethod) {
        showProgressBar();
        String initiatePaymentUrl = WebServiceConstants.BOOKING_INITIATE_PAYMENT + "?bkin=" + bkin + "&method=" + paymentMethod;
        getNetworkManager().GetRequest(RequestIdentifier.BOOKING_INITIATE_PAYMENT.ordinal(),
                initiatePaymentUrl, null, null, this, this, false);


    }


    private void setOrderAdapter() {
        loadBookingList();
        if (bookingSummaryAdapter == null)
            bookingSummaryAdapter = new BookingSummaryAdapter(context, bookingSummaryDetail, bookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(bookingSummaryAdapter);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.BOOKIND_DETAIL_SUMMARY.ordinal()) {
            if (apiError != null) {
                SnackbarFactory.showSnackbar(getContext(), apiError.message);
            } else {
                SnackbarFactory.showSnackbar(getContext(), getString(R.string.err_somthin_wrong));
            }
        }
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.BOOKIND_DETAIL_SUMMARY.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), BookingSummaryResult.class, new OnGsonParseCompleteListener<BookingSummaryResult>() {
                        @Override
                        public void onParseComplete(BookingSummaryResult data) {
                            hideProgressBar();
                            if (data != null) {
                                bookingSummaryDetail = data;

                                if (getActivity() != null && isAdded()) {
                                    setOrderAdapter();
                                    chatBT.setText(getString(R.string.chat_with, bookingSummaryDetail.supplier));
                                    chatBT.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

        if (request.getIdentifier() == RequestIdentifier.BOOKING_INITIATE_PAYMENT.ordinal()) {


            getGsonHelper().parse(responseObject.toString(), PayUData.class, new OnGsonParseCompleteListener<PayUData>() {
                        @Override
                        public void onParseComplete(PayUData data) {
                            hideProgressDialog();
                            CustomLogger.d("Hello Payment Gateway");
                            initPayUPayment(data, paymentMethod);

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
        if (view.getId() == chatBT.getId()) {
            ChatUtility chat = new ChatUtility(this, bookingSummaryDetail.trade.userId, bookingSummaryDetail.supplier, bookingSummaryDetail.trade.images.get(0).src,
                    bookingSummaryDetail.trade.name, bookingSummaryDetail.trade.trin + "");
            chat.startChatting();
        }
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();

        if (!backToMyBookings) {
            addToBackStack(getContext(), DealHomeFragment.newInstance(false), DealHomeFragment.class.getSimpleName());
        } else {
            popBackStack(getFragmentManager());
        }

        return false;
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.DEAL_BOOKING_PAYMENT_FROM_SUMMARY) {
            paymentMethod = (String) eventObject.objects[0];
        }

        if (eventObject.id == EventConstant.DEAL_BOOKING_PAYMENT_EVENT) {
            callInitiateBookingPayment(bookingSummaryDetail.tradeBooking.bkin, paymentMethod);
        }
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
                        addToBackStack(getContext(), BookingDetailFragment.newInstance(bookingSummaryDetail.tradeBooking.bkin, false, true), BookingDetailFragment.class.getSimpleName());
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
                    removeTopAndAddToBackStack(getContext(), BookingDetailFragment.newInstance(bookingSummaryDetail.tradeBooking.bkin, false, true), BookingDetailFragment.class.getSimpleName());
                } else {
                    if (msg == null) {
                        SnackbarFactory.showSnackbar(getContext(), getString(R.string.payu_payment_failed));
                    } else {
                        SnackbarFactory.showSnackbar(getContext(), msg);
                    }
                    removeTopAndAddToBackStack(getContext(), BookingDetailFragment.newInstance(bookingSummaryDetail.tradeBooking.bkin, false, true), BookingDetailFragment.class.getSimpleName());
                    //  addToBackStack(getContext(), OrderFailureFragment.newInstance(false), OrderFailureFragment.class.getSimpleName());
                }

            }
        }, 200);
    }

    private void loadBookingList() {
        bookingList = new ArrayList<>();
        if (showPaymentToTop) {
            bookingList.add(BookingSummaryAdapter.TYPE_PAYMENT_OPTION);
        }
        bookingList.add(BookingSummaryAdapter.TYPE_PAYMENT_SUMMARY);
        bookingList.add(BookingSummaryAdapter.TYPE_PRODUCT_INFO);
        if (!showPaymentToTop) {
            bookingList.add(BookingSummaryAdapter.TYPE_PAYMENT_OPTION);
        }
        bookingList.add(BookingSummaryAdapter.TYPE_ORDER_SHIPPING);
        bookingList.add(BookingSummaryAdapter.TYPE_ORDER_STATUS);

    }
}
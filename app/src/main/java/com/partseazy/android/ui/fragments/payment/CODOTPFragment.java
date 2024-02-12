package com.partseazy.android.ui.fragments.payment;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.IntentConstants;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.fragments.complete.ThankYouFragment;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.payment.CODPayment;
import com.partseazy.android.ui.model.payment.ConfirmPayment;
import com.partseazy.android.ui.model.payment.EpayCODResult;
import com.partseazy.android.ui.model.payment.EpayVerifyOTPResult;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaurav on 27/01/17.
 */

public class CODOTPFragment extends BaseFragment {


    @BindView(R.id.otpTextTV)
    protected TextView titleTV;
    @BindView(R.id.otpET)
    protected EditText otpET;
    @BindView(R.id.otpSubmitBT)
    protected Button otpSubmitBT;
    @BindView(R.id.otp_resendTV)
    protected TextView otp_resendTV;

    protected CODPayment codPayment;
    private static final String PTIN = "_PTIN";
    public static final String ODIN = "_OTIN";
    public static final String PAYMENT_METHOD = "PAYMENT_METHOD";

    private final static String CONTACT_CUSTOMER_CARE = "contact customer care";

    AppEventsLogger logger;


    private String PAYTIN;
    private String ORDIN;
    private String PAYMENT_TYPE;


    public static CODOTPFragment newInstance(String pTIN, String ODin, String paymentMethod) {
        Bundle bundle = new Bundle();
        CODOTPFragment fragment = new CODOTPFragment();
        bundle.putString(PTIN, pTIN);
        bundle.putString(ODIN, ODin);
        bundle.putString(PAYMENT_METHOD, paymentMethod);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cod_payment;
    }

    @Override
    protected String getFragmentTitle() {
        if (PAYMENT_TYPE.equals("fin"))
            return getContext().getString(R.string.credit_otp_confirmation);
        else
            return getContext().getString(R.string.cod_confirmation);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //sendCODOTPrequest();
        sendOTP();
        showBackNavigation();
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(IntentConstants.OTP));
        PAYTIN = getArguments().getString(PTIN);
        ORDIN = getArguments().getString(ODIN);
        PAYMENT_TYPE = getArguments().getString(PAYMENT_METHOD);
        CustomLogger.d("PAytin ::" + PAYTIN + "orderIn:: " + ORDIN + "PAYMENT_TYPE" + PAYMENT_TYPE);

        //init facebook looger
        logger = AppEventsLogger.newLogger(context);

    }

    private void sendOTP() {
        if (PAYMENT_TYPE.equals("cod")) {
            sendCODOTPrequest();
        }
        else if (PAYMENT_TYPE.equals("fin")) {
            initView();
        }
    }

    private void sendCODOTPrequest() {

        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.OTP_COD_REQUEST_ID.ordinal(),
                WebServiceConstants.SEND_COD_OTP + ORDIN, null, null, this, this, false);
    }

    private void sendEpayOTPrequest() {

        showProgressBar();
        getNetworkManager().PutRequest(RequestIdentifier.OTP_EPAY_REQUEST_ID.ordinal(),
                WebServiceConstants.SEND_EPAY_OTP, null, null, this, this, false);
    }


    @Override
    protected void updateOTPView(String otpCode) {
        super.updateOTPView(otpCode);

        if (otpCode != null && otpCode.length() > 0)
            otpET.setText(otpCode);

    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.OTP_COD_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), CODPayment.class, new OnGsonParseCompleteListener<CODPayment>() {
                        @Override
                        public void onParseComplete(CODPayment data) {
                            try {
                                hideProgressDialog();
                                codPayment = data;
                                initView();
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
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


        if (request.getIdentifier() == RequestIdentifier.OTP_COD_VERIFY_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), ConfirmPayment.class, new OnGsonParseCompleteListener<ConfirmPayment>() {
                        @Override
                        public void onParseComplete(ConfirmPayment data) {
                            try {
                                hideProgressDialog();
                                BaseActivity.showToast("The machine_state is " + data.machineState);
                                showThankYouFragment();
                                logAddPaymentInfoEvent(true);
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            APIError er = new APIError();
                            er.message = exception.getMessage();
                            logAddPaymentInfoEvent(false);



                        }
                    }
            );

        }

        if (request.getIdentifier() == RequestIdentifier.OTP_EPAY_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), EpayCODResult.class, new OnGsonParseCompleteListener<EpayCODResult>() {
                        @Override
                        public void onParseComplete(EpayCODResult data) {
                            hideProgressDialog();
                           // initView();


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

        if (request.getIdentifier() == RequestIdentifier.OTP_EPAY_VERIFY_ID.ordinal()) {
            CustomLogger.d("Resonse ::" + responseObject.toString());
            getGsonHelper().parse(responseObject.toString(), EpayVerifyOTPResult.class, new OnGsonParseCompleteListener<EpayVerifyOTPResult>() {
                        @Override
                        public void onParseComplete(EpayVerifyOTPResult data) {
                            try {
                                hideProgressDialog();
                                if (data.success == 1) {
                                    showThankYouFragment();
                                }
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
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


        return true;
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.OTP_COD_VERIFY_ID.ordinal()) {
            if (apiError != null) {
                SnackbarFactory.showSnackbar(getContext(), apiError.issue);
                logAddPaymentInfoEvent(false);
            } else {
                SnackbarFactory.showSnackbar(getContext(), getString(R.string.wrong_otp));
            }
        }

        if (request.getIdentifier() == RequestIdentifier.OTP_EPAY_VERIFY_ID.ordinal()) {
            if (apiError != null) {
                if (apiError.issue.contains(CONTACT_CUSTOMER_CARE)) {
                    showCCDialog(apiError.issue);
                } else {
                    SnackbarFactory.showSnackbar(getContext(), apiError.issue);
                }
            } else {
                SnackbarFactory.showSnackbar(getContext(), getString(R.string.wrong_otp));
            }
        }

        if(request.getIdentifier() == RequestIdentifier.OTP_EPAY_REQUEST_ID.ordinal())
        {
            if (apiError != null) {
                if (apiError.issue.contains(CONTACT_CUSTOMER_CARE)) {
                    showCCDialog(apiError.issue);
                } else {
                    SnackbarFactory.showSnackbar(getContext(), apiError.issue);
                }
            } else {
                SnackbarFactory.showSnackbar(getContext(), getString(R.string.wrong_otp));
            }
        }


        return true;
    }


    private void initView() {
        if (PAYMENT_TYPE.equals("fin")) {
            titleTV.setText(getString(R.string.epay_otp_text_number));
        } else if (PAYMENT_TYPE.equals("cod")) {
            titleTV.setText(getString(R.string.otp_text_number, codPayment.mobile));
        }


    }

    private void showCCDialog(String message) {
        if (getActivity() != null) {
            hideKeyBoard(getView());
            DialogUtil.showAlertDialog(getActivity(), true, null, message, getString(R.string.call_us)
                    , null, null, new DialogListener() {
                        @Override
                        public void onPositiveButton(DialogInterface dialog) {
                            CommonUtility.dialPhoneNumber(context, StaticMap.CC_PHONE);
                        }
                    });
        }
    }

    public void showThankYouFragment() {
        hideProgressDialog();
        popToHome(getActivity());
        addToBackStack(getContext(), ThankYouFragment.newInstance(ORDIN), ThankYouFragment.getTagName());
    }


    @OnClick(R.id.otpSubmitBT)
    protected void confirmOTP() {

        callVerifyOTP();
    }

    private void callVerifyOTP() {

        if (!TextUtils.isEmpty(otpET.getText().toString())) {
            showProgressDialog(false);

            if (PAYMENT_TYPE.equals("cod")) {
                getNetworkManager().PutRequest(RequestIdentifier.OTP_COD_VERIFY_ID.ordinal(),
                        WebServiceConstants.VERIFY_COD_OTP, WebServicePostParams.verifyCODParams(otpET.getText().toString(), PAYTIN), null, this, this, false);
            } else if (PAYMENT_TYPE.equals("fin")) {
                getNetworkManager().PutRequest(RequestIdentifier.OTP_EPAY_VERIFY_ID.ordinal(),
                        WebServiceConstants.VERIFY_EPAY_OTP, WebServicePostParams.verifyEpayOTPParams(otpET.getText().toString()), null, this, this, false);

            }
        } else {
            BaseActivity.showToast(getString(R.string.please_enter_otp));
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(IntentConstants.OTP));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showOrderFailureDialog();
        return super.onOptionsItemSelected(item);
    }

    public static String getTagName() {
        return CODOTPFragment.class.getSimpleName();
    }


    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        showOrderFailureDialog();
        return false;
    }


    @OnClick(R.id.otp_resendTV)
    public void resendOtp() {
        // sendCODOTPrequest();
        //sendOTP();
        sendResendOTP();
    }

    private void sendResendOTP()
    {
        if (PAYMENT_TYPE.equals("cod")) {
            sendCODOTPrequest();
        }
        else if (PAYMENT_TYPE.equals("fin")) {
           sendEpayOTPrequest();
        }
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logAddPaymentInfoEvent (boolean success) {
        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, success ? 1 : 0);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, params);
    }
}

package com.partseazy.android.ui.fragments.checkout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.fragments.finance.CreditFacilityFragment;
import com.partseazy.android.ui.fragments.finance.FinanceCommonUtility;
import com.partseazy.android.ui.model.cart_checkout.PaymentCreditData;
import com.partseazy.android.ui.model.credit.CreditOptionList;
import com.partseazy.android.ui.model.credit_facility.PendingCreditResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.financialapplication.FinanceStatus;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by can on 27/12/16.
 */

public class CreditFragment extends BaseFragment {

    @BindView(R.id.creditExtraTV)
    protected TextView creditExtraTV;
    @BindView(R.id.paymentsTV)
    protected TextView paymentsTV;

    @BindView(R.id.creditTV)
    TextView creditTV;

    @BindView(R.id.creditPBRL)
    RelativeLayout upfront_paymentRL;
    @BindView(R.id.creditStateTV)
    TextView creditStateTV;

    @BindView(R.id.creditButton)
    TextView creditButton;

    @BindView(R.id.place_orderTV)
    Button placeOrderBT;

    @BindView(R.id.termsConditionLL)
    protected RelativeLayout termsConditionLL;
    @BindView(R.id.termsConditionCB)
    protected CheckBox termsConditionCB;
    @BindView(R.id.termsConditionTV)
    protected TextView termsConditionTV;

    @BindView(R.id.creditAvailableLL)
    protected LinearLayout creditAvailableLL;

    @BindView(R.id.creditValueTV)
    protected TextView creditValueTV;


    private boolean creditCheck;
    private List<CreditOptionList> lists;

    public final static String key = "CREDIT";

    private PaymentCreditData creditData;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_credit;
    }

    @Override
    protected String getFragmentTitle() {
        return CreditFragment.class.getSimpleName();
    }

    public static String getTagName() {
        return CreditFragment.class.getSimpleName();
    }

    public static CreditFragment newInstance(PaymentCreditData credit) {
        Bundle bundle = new Bundle();
        CreditFragment fragment = new CreditFragment();
        bundle.putSerializable(key, (Serializable) credit);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        creditData = (PaymentCreditData) bundle.getSerializable(key);
        init();
    }

    private void init() {

        if (creditData != null) {
            if (creditData.pcent != 0) {
                creditExtraTV.setVisibility(View.VISIBLE);
                paymentsTV.setVisibility(View.VISIBLE);
                creditExtraTV.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.text_dark) + getString(R.string.extra_charge) +
                        "<font color=" + getResources().getColor(R.color.green_checkout) + ">" + creditData.pcent + "% </font>" +
                        "<font color=" + getResources().getColor(R.color.text_dark) + getString(R.string.on_credit_payment)));
                paymentsTV.setText(Html.fromHtml(getString(R.string.you_need_to_pay) + CommonUtility.convertionPaiseToRupeeString(creditData.total) + "</font>" +
                        "<font color=" + getResources().getColor(R.color.green_checkout) + "></font>"));
            } else {
                creditExtraTV.setVisibility(View.GONE);
                paymentsTV.setVisibility(View.GONE);
            }
        }

        upfront_paymentRL.setVisibility(View.VISIBLE);
        creditStateTV.setVisibility(View.GONE);
        creditButton.setVisibility(View.GONE);
        placeOrderBT.setVisibility(View.GONE);
        termsConditionLL.setVisibility(View.VISIBLE);
        showProgressBar();
        FinanceCommonUtility.loadFinanceStatus(this);

        termsConditionCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (termsConditionCB.isChecked()) {
                    placeOrderBT.setVisibility(View.VISIBLE);
                } else {
                    placeOrderBT.setVisibility(View.GONE);
                }
            }
        });

        termsConditionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsConditionDialog();
            }
        });

    }


    protected void callUserCreditDetails() {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_USER_PENDING_CREDIT.ordinal(),
                WebServiceConstants.PENDING_CREDIT, null, params, this, this, false);
    }


    private void showTermsConditionDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dailog_terms_condition, null);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(context.getString(R.string.terms_and_condition));
        webView.loadUrl(AppConstants.EPAY_LATER_TERMS_CONDITION_URL);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        final Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    protected void checkoutRequest() {
        CustomLogger.d("Time to load Cart");
        showProgressDialog();
        getNetworkManager().GetRequest(RequestIdentifier.Checkout_CreditOption_ID.ordinal(),
                WebServiceConstants.GET_PAYMENT_CREDIT, null, null, this, this, false);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.GET_USER_PENDING_CREDIT.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
            hideProgressBar();
        } else {
            upfront_paymentRL.setVisibility(View.GONE);
            showError();

        }
        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.FINANCE_APP_STATUS_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), FinanceStatus.class, new OnGsonParseCompleteListener<FinanceStatus>() {
                        @Override
                        public void onParseComplete(FinanceStatus data) {
                            try {
                                upfront_paymentRL.setVisibility(View.GONE);
                                hideProgressBar();
                                showApplicationStatus(data);
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            upfront_paymentRL.setVisibility(View.GONE);
                            hideProgressBar();
                            CustomLogger.e("Exception ", exception);
                            showError();
                        }
                    }
            );


        }

        if (request.getIdentifier() == RequestIdentifier.GET_USER_PENDING_CREDIT.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), PendingCreditResult.class, new OnGsonParseCompleteListener<PendingCreditResult>() {
                        @Override
                        public void onParseComplete(PendingCreditResult data) {
                            try {
                                if (data.creditResult != null) {

                                    if (data.creditResult.credit > 0) {
                                        int creditRs = CommonUtility.convertionPaiseToRupee(data.creditResult.credit);
                                        DataStore.setUserCredit(context, creditRs + "");
                                        creditValueTV.setText(getString(R.string.rs_str, creditRs + ""));
                                        creditAvailableLL.setVisibility(View.VISIBLE);
                                    } else {
                                        creditAvailableLL.setVisibility(View.GONE);

                                    }

                                }
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressBar();
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );
        }


        return true;
    }


    private void showApplicationStatus(FinanceStatus data) {

        creditStateTV.setVisibility(View.GONE);
        creditButton.setVisibility(View.VISIBLE);
        placeOrderBT.setVisibility(View.GONE);
        termsConditionLL.setVisibility(View.GONE);
        placeOrderBT.setText(R.string.place_order);
        if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.APPROVED.getStatus())) {
            //creditTV.setText(StaticMap.CREDIT_APPROVED_ALLOWED);
            creditButton.setText(context.getString(R.string.credit_apporved));
            creditButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_approval, 0, 0, 0);
            creditButton.setBackgroundColor(context.getResources().getColor(R.color.green_success));
            placeOrderBT.setVisibility(View.GONE);
            placeOrderBT.setText(R.string.apply_for_credit);
            creditButton.setVisibility(View.GONE);
            creditTV.setText("");
            PartsEazyEventBus.getInstance().postEvent(EventConstant.CREDIT_ORDER, key, null, creditData.total);

        } else if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.REJECTED.getStatus())) {
            creditTV.setText(StaticMap.CREDIT_REJECTED);
            creditButton.setEnabled(false);
            creditButton.setText(context.getString(R.string.credit_rejected));
            creditButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.white_cross, 0, 0, 0);
            creditButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        } else if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.UNDER_PROCESS.getStatus())) {
            creditTV.setText(StaticMap.CREDIT_UNDER_PROCESS);
            creditButton.setText(context.getString(R.string.under_processing));
            creditButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_pending, 0, 0, 0);
            creditButton.setEnabled(false);

        } else if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.NOT_APPLIED.getStatus())) {
            creditTV.setText(StaticMap.CREDIT_NOT_APPLIED);
            creditStateTV.setVisibility(View.VISIBLE);
            creditButton.setVisibility(View.GONE);
            creditStateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFragment fragment = CreditFacilityFragment.newInstance();
                    removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, CreditFacilityFragment.getTagName());
                }
            });
        }

    }


    @OnClick({R.id.place_orderTV, R.id.creditButton})
    public void onClick(View view) {

        if (view.getId() == R.id.place_orderTV) {
            PartsEazyEventBus.getInstance().postEvent(EventConstant.PLACE_ORDER, key, null);
        }

        if (view.getId() == R.id.creditButton) {
            PartsEazyEventBus.getInstance().postEvent(EventConstant.CREDIT_ORDER, key, null, creditData.total);

        }

    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.CHECKOUT_CREDIT_ORDER) {

            boolean status = (boolean) eventObject.objects[0];
            if (status) {
                callUserCreditDetails();
                creditTV.setText(StaticMap.CREDIT_APPROVED_ALLOWED);
                creditTV.setVisibility(View.VISIBLE);
                creditButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_approval, 0, 0, 0);
                creditButton.setEnabled(false);
                termsConditionLL.setVisibility(View.VISIBLE);
//                placeOrderBT.setVisibility(View.VISIBLE);
                placeOrderBT.setText(R.string.place_order);

            } else {
                creditTV.setText(StaticMap.CREDIT_APPROVED_DISALLOWED);
                creditTV.setVisibility(View.VISIBLE);
                placeOrderBT.setVisibility(View.GONE);
                termsConditionLL.setVisibility(View.GONE);


            }


        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showOrderFailureDialog();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        showOrderFailureDialog();
        return false;
    }
}

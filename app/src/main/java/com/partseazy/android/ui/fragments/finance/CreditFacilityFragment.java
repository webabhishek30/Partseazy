package com.partseazy.android.ui.fragments.finance;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.financialapplication.FinanceStatus;

import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 2/2/17.
 */

public class CreditFacilityFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.descWebView)
    protected WebView descWebView;
    @BindView(R.id.continueBTN)
    protected Button continueBTN;
    @BindView(R.id.fillAppBTN)
    protected Button fillAppBTN;
    @BindView(R.id.contactUsTV)
    protected TextView contactUsTV;
    @BindView(R.id.approvedTagTV)
    protected TextView approvedTagTV;
    @BindView(R.id.rejectedTagTV)
    protected TextView rejectedTagTV;
    @BindView(R.id.underProcessTagTV)
    protected TextView underProcessTagTV;
    @BindView(R.id.appStatusTextTV)
    protected TextView appStatusTextTV;

    @BindView(R.id.epaylaterWebView)
    protected WebView epaylaterWebView;

    @BindView(R.id.nestedView)
    protected NestedScrollView nestedView;

    @BindView(R.id.b2c2_progressbar)
    protected ProgressBar progressBar;


    public static CreditFacilityFragment newInstance() {

        Bundle bundle = new Bundle();
        CreditFacilityFragment fragment = new CreditFacilityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.credit_facility);
    }

    public static String getTagName() {
        return CreditFacilityFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_credit_facility;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgressBar();
        FinanceCommonUtility.loadFinanceStatus(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        descWebView.loadData(StaticMap.credit_facility_desc, "text/html", "UTF-8");
        continueBTN.setOnClickListener(this);
        contactUsTV.setOnClickListener(this);
        fillAppBTN.setOnClickListener(this);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.FINANCE_APP_STATUS_ID.ordinal()) {
            hideProgressBar();

            getGsonHelper().parse(responseObject.toString(), FinanceStatus.class, new OnGsonParseCompleteListener<FinanceStatus>() {
                        @Override
                        public void onParseComplete(FinanceStatus data) {
                            try {
                                hideProgressDialog();
                                if (data.url != null && !data.url.equals("")) {
                                    epaylaterWebView.setVisibility(View.VISIBLE);
                                    nestedView.setVisibility(View.GONE);
                                    epaylaterWebView.setWebViewClient(new MyWebClient());
                                    epaylaterWebView.loadUrl(data.url);
                                } else {
                                    epaylaterWebView.setVisibility(View.GONE);
                                    nestedView.setVisibility(View.VISIBLE);
                                    showApplicationStatus(data);
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                            showError();
                        }
                    }
            );


        }


        return true;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        return true;
    }

    private void showApplicationStatus(FinanceStatus data) {

        if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.APPROVED.getStatus())) {

            approvedTagTV.setVisibility(View.VISIBLE);
            continueBTN.setVisibility(View.VISIBLE);

        } else if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.REJECTED.getStatus())) {
            rejectedTagTV.setVisibility(View.VISIBLE);

        } else if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.UNDER_PROCESS.getStatus())) {
            underProcessTagTV.setVisibility(View.VISIBLE);
        } else if (data.status.equals(FinanceCommonUtility.FinanceAppStatus.NOT_APPLIED.getStatus())) {
            fillAppBTN.setVisibility(View.VISIBLE);
            appStatusTextTV.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == continueBTN.getId()) {
            popToHome(getActivity());
        }

//        if (view.getId() == rejectedTagTV.getId()) {
//            //TODO need to change the url
//            String webUrl = "https://en.wikipedia.org/wiki/Mathematics";
//            addToBackStack(getContext(), WebViewFragment.newInstance(getString(R.string.contact_us), webUrl), WebViewFragment.getTagName());
//        }

        if (view.getId() == fillAppBTN.getId()) {
            popToHome(getActivity());
            addToBackStack(getContext(), FinancialApplicationFragment.newInstance(), FinancialApplicationFragment.getTagName());
        }
    }


    private class MyWebViewClient extends WebViewClient {

        ProgressDialog progressDialog;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        //Show loader on url load
        public void onLoadResource(WebView view, String url) {
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        }

        public void onPageFinished(WebView view, String url) {
            try {
//                if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                // }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}


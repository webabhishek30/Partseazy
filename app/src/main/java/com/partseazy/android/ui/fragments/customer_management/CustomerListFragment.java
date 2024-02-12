package com.partseazy.android.ui.fragments.customer_management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.customer_management.CustomerAdapter;
import com.partseazy.android.ui.model.customer_management.CustomerResultList;
import com.partseazy.android.ui.model.error.APIError;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by shubhang on 15/02/18.
 */

public class CustomerListFragment extends BaseFragment {

    @BindView(R.id.customersRV)
    protected RecyclerView customersRV;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private CustomerAdapter adapter;

    public static CustomerListFragment newInstance() {
        Bundle bundle = new Bundle();
        CustomerListFragment fragment = new CustomerListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_customer_list;
    }

    public static String getTagName() {
        return CustomerListFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.customers);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadCustomers();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    }

    private void loadCustomers() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_CUSTOMERS.ordinal(),
                WebServiceConstants.GET_CUSTOMERS, null, params, this, this, false);
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressBar();
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.GET_CUSTOMERS.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), CustomerResultList.class, new OnGsonParseCompleteListener<CustomerResultList>() {
                        @Override
                        public void onParseComplete(CustomerResultList data) {
                            if (data.result != null && data.result.size() > 0) {
                                setupAdapter(data);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            CustomLogger.e("Exception ", exception);
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
        if (request.getIdentifier() == RequestIdentifier.GET_CUSTOMERS.ordinal()) {
            showError();
        }
        return true;
    }

    private void setupAdapter(CustomerResultList data) {
        adapter = new CustomerAdapter(this, data.result);
        customersRV.setLayoutManager(new LinearLayoutManager(getContext()));
        customersRV.setAdapter(adapter);
    }
}

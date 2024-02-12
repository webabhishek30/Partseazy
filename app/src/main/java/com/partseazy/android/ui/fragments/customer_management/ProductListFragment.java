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
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.customer_management.ProductAdapter;
import com.partseazy.android.ui.model.customer_management.CustomerProduct;
import com.partseazy.android.ui.model.customer_management.CustomerProductList;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by shubhang on 16/02/18.
 */

public class ProductListFragment extends BaseFragment {

    @BindView(R.id.customersRV)
    protected RecyclerView productsRV;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private int stock = 0;
    private int pItemId = 0;

    private List<CustomerProduct> products;
    private ProductAdapter adapter;

    public static ProductListFragment newInstance() {
        Bundle bundle = new Bundle();
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_customer_list;
    }

    public static String getTagName() {
        return ProductListFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.products);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProducts();
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

    private void loadProducts() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_CUSTOMER_PRODUCTS.ordinal(),
                WebServiceConstants.GET_CUSTOMER_PRODUCTS, null, params, this, this, false);
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.GET_CUSTOMER_PRODUCTS.ordinal()) {
            hideProgressBar();
            hideProgressDialog();
            getGsonHelper().parse(responseObject.toString(), CustomerProductList.class, new OnGsonParseCompleteListener<CustomerProductList>() {
                        @Override
                        public void onParseComplete(CustomerProductList data) {
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

        if (request.getIdentifier() == RequestIdentifier.PUT_SKU.ordinal()) {
            Map params = WebServicePostParams.updateItem(stock);
            getNetworkManager().PutRequest(RequestIdentifier.PUT_ITEM.ordinal(),
                    WebServiceConstants.PUT_ITEM + String.valueOf(pItemId),
                    params, null, this, this, false);
        }

        if (request.getIdentifier() == RequestIdentifier.PUT_ITEM.ordinal() ||
                request.getIdentifier() == RequestIdentifier.SWITCH_SKU.ordinal()) {
            loadProducts();
        }

        return true;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.GET_CUSTOMER_PRODUCTS.ordinal()) {
            showError();
        }

        if (request.getIdentifier() == RequestIdentifier.PUT_SKU.ordinal()
                || request.getIdentifier() == RequestIdentifier.PUT_ITEM.ordinal()
                || request.getIdentifier() == RequestIdentifier.SWITCH_SKU.ordinal()) {
            if (apiError != null) {
                showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
            } else {
                showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
            }
        }
        return true;
    }

    private void setupAdapter(CustomerProductList data) {
        products = data.result;
        adapter = new ProductAdapter(this, products);
        productsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        productsRV.setAdapter(adapter);
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.EDIT_PRODUCT) {
            int price = Integer.parseInt((String) eventObject.objects[0]);
            int cost = Integer.parseInt((String) eventObject.objects[1]);
            stock = Integer.parseInt((String) eventObject.objects[2]);
            int pSkuId = (Integer) eventObject.objects[3];
            pItemId = (Integer) eventObject.objects[4];

            showProgressDialog();
            Map params = WebServicePostParams.updateSku(CommonUtility.convertionRupeeToPaise(price),
                    CommonUtility.convertionRupeeToPaise(cost));
            getNetworkManager().PutRequest(RequestIdentifier.PUT_SKU.ordinal(),
                    WebServiceConstants.PUT_SKU + String.valueOf(pSkuId),
                    params, null, this, this, false);
        }

        if (eventObject.id == EventConstant.SWITCH_PRODUCT) {
            int active = (boolean) eventObject.objects[0] ? 1 : 0;
            int pSkuId = (Integer) eventObject.objects[1];
            showProgressDialog();
            Map params = WebServicePostParams.changeActiveSku(active);
            getNetworkManager().PutRequest(RequestIdentifier.SWITCH_SKU.ordinal(),
                    WebServiceConstants.PUT_SKU + String.valueOf(pSkuId),
                    params, null, this, this, false);
        }
    }
}

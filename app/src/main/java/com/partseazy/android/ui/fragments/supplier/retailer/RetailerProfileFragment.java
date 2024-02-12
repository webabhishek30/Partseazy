package com.partseazy.android.ui.fragments.supplier.retailer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.adapters.suppliers.retailer.RetailerProfileAdapter;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.supplier.shop.Shop;
import com.partseazy.android.utility.KeyPadUtility;

import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by naveen on 8/9/17.
 */

public class RetailerProfileFragment extends BaseFragment {

    @BindView(R.id.recylerView)
    protected RecyclerView recylerView;


    private int shopId;
    private Shop shopDetailHolder;
    private RetailerProfileAdapter retailerProfileAdapter;



    public static RetailerProfileFragment newInstance() {
        Bundle bundle = new Bundle();
        RetailerProfileFragment fragment = new RetailerProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (shopDetailHolder != null) {
            setProfileAdapter();
        } else {
            loadShopDetail();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId=302;
    }



    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_retailer_profile;
    }

    @Override
    protected String getFragmentTitle() {
        return null;
    }

    public static String getTagName() {
        return RetailerProfileFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showBackNavigation();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void loadShopDetail() {
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.GET_SHOP_DETAILS_ID.ordinal(),
                WebServiceConstants.GET_SHOP_DETAIL + shopId, null, null, this, this, false);

    }

    private void setProfileAdapter() {
        if (retailerProfileAdapter == null)
            retailerProfileAdapter = new RetailerProfileAdapter(context,shopDetailHolder);
        recylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recylerView.setAdapter(retailerProfileAdapter);
        recylerView.setNestedScrollingEnabled(false);


    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressBar();
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.GET_SHOP_DETAILS_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), Shop.class, new OnGsonParseCompleteListener<Shop>() {
                        @Override
                        public void onParseComplete(Shop data) {
                            if (data != null) {
                                shopDetailHolder = data;
                                setProfileAdapter();
//                                hideNoResult(productContainerView);
                            } else {
                                // showNoResult(getString(R.string.err_somthin_wrong), productContainerView);
                                showError();
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
        if (request.getIdentifier() == RequestIdentifier.GET_SHOP_DETAILS_ID.ordinal()) {
            showError();
        }
        return true;
    }
}

package com.partseazy.android.ui.fragments.supplier.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.adapters.suppliers.shop.ShopDetailAdapter;
import com.partseazy.android.ui.adapters.suppliers.shop.ShopImageSliderAdapter;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.supplier.shop.Shop;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;

import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopDetailFragment  extends BaseFragment {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.collapsingToolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.scrollable)
    protected RecyclerView recyclerView;

    @BindView(R.id.toolbarRecyclerView)
    protected RecyclerView toolbarRecyclerView;


//    @BindView(R.id.productContainerView)
//    protected RelativeLayout containerView;

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";

    private ShopDetailAdapter shopDetailAdapter;
    private ShopImageSliderAdapter imageSliderAdapter;

    private Shop shopDetailHolder;
    private int shopId;
    private String shopName;


    public static ShopDetailFragment newInstance() {
        Bundle bundle = new Bundle();
        ShopDetailFragment fragment = new ShopDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ShopDetailFragment newInstance(int shopId, String shopName) {
        Bundle bundle = new Bundle();
        bundle.putInt(SHOP_ID, shopId);
        bundle.putString(SHOP_NAME, shopName);
        ShopDetailFragment fragment = new ShopDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shop_details;
    }


    @Override
    protected String getFragmentTitle() {
        return CommonUtility.getFormattedName(shopName);
    }

    public static String getTagName() {
        return ShopDetailFragment.class.getSimpleName();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getInt(SHOP_ID);
        shopName = getArguments().getString(SHOP_NAME);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (shopDetailHolder != null) {
            setImagesAdapter();
            setRetailerAdapter();
        } else {
            loadShopDetail();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showBackNavigation();
        hideKeyBoard(getView());
        toolbarTextAppernce();
        return view;

    }


    private void loadShopDetail() {
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.GET_SHOP_DETAILS_ID.ordinal(),
                WebServiceConstants.GET_SHOP_DETAIL + shopId, null, null, this, this, false);

    }

    private void setRetailerAdapter() {
        if (shopDetailAdapter == null)
            shopDetailAdapter = new ShopDetailAdapter(this, shopDetailHolder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shopDetailAdapter);
        //recyclerView.setNestedScrollingEnabled(false);

    }

    //
//
    public void setImagesAdapter() {

        if (imageSliderAdapter == null)
            imageSliderAdapter = new ShopImageSliderAdapter(context, shopDetailHolder.shopInfo.images, SLIDEIMAGETYPE.RETAIL_DETAIL_PAGE,shopDetailHolder.shopInfo);

        toolbarRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        toolbarRecyclerView.setAdapter(imageSliderAdapter);
        toolbarRecyclerView.setItemViewCacheSize(0);
        toolbarRecyclerView.getRecycledViewPool().setMaxRecycledViews(1, 0);
        toolbarRecyclerView.setNestedScrollingEnabled(false);


    }

    //
    private void toolbarTextAppernce() {
        if (toolbar != null)
            toolbar.setTitle(CommonUtility.getFormattedName(shopName));

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }


    //        setOverflowButtonColor(getActivity(), Color.BLACK);
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
                                setRetailerAdapter();
                                setImagesAdapter();
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

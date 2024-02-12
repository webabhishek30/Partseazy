package com.partseazy.android.ui.fragments.supplier.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.suppliers.shop.ShopAdapter;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.supplier.shop.ShopResultData;
import com.partseazy.android.ui.widget.EndlessRecyclerOnScrollListener;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopListFragment extends ShopsDetailBaseFragment {


    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;

    protected int PAGE_COUNT = 1;
    private static final String RETAILER_RESULT = "retailer_result";

    private LinearLayoutManager linearLayoutManager;
    private ShopAdapter shopAdapter;
    private ShopResultData shopResultData;



    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shop_list;
    }

    @Override
    protected String getFragmentTitle() {
        return ShopListFragment.class.getName();
    }


    public static ShopListFragment newInstance(String retailerResult) {
        Bundle bundle = new Bundle();
        bundle.putString(RETAILER_RESULT, retailerResult);
        ShopListFragment fragment = new ShopListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String retailerResultJson = getArguments().getString(RETAILER_RESULT);
        CustomLogger.d("jsonString ::"+retailerResultJson);
        shopResultData = new Gson().fromJson(retailerResultJson, ShopResultData.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setReatilerAdapter();
        if(shopResultData.shops!=null && shopResultData.shops.size()>0) {
            CustomLogger.d("ReatilerDataJson  11 ::"+shopResultData.shops.size());
            shopAdapter.updateImageSliderAdapter(shopResultData.meta.page, shopResultData);
        }
    }



    private void setReatilerAdapter() {
        if (shopAdapter == null)
            shopAdapter = new ShopAdapter(getContext());;
        linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(shopAdapter);
        setEndLessRecycler();


    }


    private void setEndLessRecycler() {


        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                hideProgressBar();

//                loadMoreProgress.setVisibility(View.VISIBLE);

                CustomLogger.d("The current page is " + current_page
                        + " Total available Items " + shopResultData.meta.noOfResult
                        + " Current Loaded Items " + (shopResultData.meta.page * shopResultData.meta.rows));

                if ((shopResultData.meta.page * shopResultData.meta.rows)
                        < Integer.valueOf(shopResultData.meta.noOfResult)) {

                    PAGE_COUNT = current_page;
                    CustomLogger.d("PAGE COUNT" + PAGE_COUNT);

                    getRetailerShopsList(PAGE_COUNT);
                } else {
//                    loadMoreProgress.setVisibility(View.GONE);

                }
            }
        };
        mRecyclerView.setOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    protected boolean handleBrowseShopsDataResponse(ShopResultData data) {
        hideProgressBar();
        hideProgressDialog();
        shopResultData = data;
        shopAdapter.updateImageSliderAdapter(data.meta.page, data);
        return true;
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        showError();
        return true;
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);
        if (eventObject.id == EventConstant.SELECT_SORT_SHOP_TYPE) {
            String sortCode = (String) eventObject.objects[0];
            finalSearchData.page = 1;
            finalSearchData.sortCode = sortCode;
            clearShopDataForSorting();
            callShopListData();
        }
    }

    private void clearShopDataForSorting() {
        PAGE_COUNT = 1;
        if (shopAdapter != null) {
            shopAdapter.clearAdapter();
        }
    }

}

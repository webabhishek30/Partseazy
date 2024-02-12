package com.partseazy.android.ui.fragments.deals.promote_deal;

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
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.promotions.RetailerPreviewAdapter;
import com.partseazy.android.ui.model.deal.promotion.PromotionPostData;
import com.partseazy.android.ui.model.deal.promotion.preview.PromoteRetailerDetail;
import com.partseazy.android.ui.model.deal.promotion.preview.PromoteRetailerList;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.widget.EndlessRecyclerOnScrollListener;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 28/8/17.
 */

public class PreviewPromotionFragment extends BaseFragment implements View.OnClickListener {

    public static final String PROMOTION_OBJ = "promotion_obj";

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.nextBT)
    protected Button nextBT;


    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private LinearLayoutManager linearLayoutManager;
    private RetailerPreviewAdapter retailerPreviewAdapter;
    private List<PromoteRetailerDetail> retailerList;
    private String selectedClusterString;
    private int PAGE_COUNT = 1;
    public PromotionPostData promotionPostData;
    public String promotionJson;



    public static PreviewPromotionFragment newInstance(String promotionObjJson) {
        Bundle bundle = new Bundle();
        bundle.putString(PROMOTION_OBJ, promotionObjJson);
        PreviewPromotionFragment fragment = new PreviewPromotionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_retailer_list_deals;
    }

    public static String getTagName() {
        return PreviewPromotionFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.promote_your_deal);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retailerList = new ArrayList<>();
        promotionJson = getArguments().getString(PROMOTION_OBJ);
        promotionPostData =  new Gson().fromJson(promotionJson,PromotionPostData.class);
        selectedClusterString = CommonUtility.convertListToCommaString(promotionPostData.clusterList);
        CustomLogger.d("Selected Cluster String ::" + selectedClusterString);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (retailerList.size() > 0)
            setRetailerAdapter();

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
        nextBT.setOnClickListener(this);

        setRetailerAdapter();
        if (retailerList.size() == 0)
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    loadShopsData();
                }
            });


    }

    private void setRetailerAdapter() {
        if (retailerPreviewAdapter == null)
            retailerPreviewAdapter = new RetailerPreviewAdapter(context, retailerList);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(retailerPreviewAdapter);
        setEndLessRecyclerView();

    }

    private void loadShopsData() {
        showProgressDialog(getString(R.string.loading), false);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        Map<String, String> requestParams = WebServicePostParams.getRetailerDistrictParams(PAGE_COUNT, selectedClusterString);
        getNetworkManager().GetRequest(RequestIdentifier.PROMOTION_RETAILER_SHOPS_ID.ordinal(),
                WebServiceConstants.GET_PROMOTION_RETAIL_SHOPS, requestParams, params, this, this, false);
    }

    private void setEndLessRecyclerView() {
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                CustomLogger.d("Page Number ::" + page);
                PAGE_COUNT++;
                loadShopsData();
            }
        };
        recyclerView.setOnScrollListener(endlessRecyclerOnScrollListener);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.PROMOTION_RETAILER_SHOPS_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.PROMOTION_RETAILER_SHOPS_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), PromoteRetailerList.class, new OnGsonParseCompleteListener<PromoteRetailerList>() {
                @Override
                public void onParseComplete(PromoteRetailerList data) {


                    if (data != null && data.retailerList.size() > 0) {
                        retailerList.addAll(data.retailerList);
                        retailerPreviewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    hideProgressBar();
                    CustomLogger.e("Exception ", exception);
                    showError(exception.getMessage(), MESSAGETYPE.SNACK_BAR);
                }

            });

        }


        return true;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == nextBT.getId()) {
            addToBackStack((BaseActivity) getActivity(), PromoteCreditFragment.newInstance(promotionJson), PromoteCreditFragment.getTagName());
        }
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.SHOW_MAP_DAILOG) {
            double latitude = (Double) eventObject.objects[0];
            double longitude = (Double) eventObject.objects[1];
            String shopName = (String) eventObject.objects[2];
            //  DialogUtil.showMapDailog(getActivity(),latitude,longitude,shopName);
            LocationMapFragment locationMapFragment = LocationMapFragment.newInstance(shopName, latitude, longitude);
            addToBackStack((BaseActivity) getActivity(), locationMapFragment, locationMapFragment.getTag());
        }
    }
}

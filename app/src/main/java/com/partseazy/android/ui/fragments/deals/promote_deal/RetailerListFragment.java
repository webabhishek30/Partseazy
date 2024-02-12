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
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.adapters.deals.promotions.PromotionCityAdapter;
import com.partseazy.android.ui.model.deal.promotion.PromotionPostData;
import com.partseazy.android.ui.model.deal.promotion.locations.City;
import com.partseazy.android.ui.model.deal.promotion.locations.District;
import com.partseazy.android.ui.model.deal.promotion.locations.Locality;
import com.partseazy.android.ui.model.deal.promotion.required.PromotionRequiredResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 28/8/17.
 */

public class RetailerListFragment extends BaseFragment implements View.OnClickListener {



    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.nextBT)
    protected Button nextBT;

    public static final  String  TRADE_ID ="trade_id";

    public static final int MAX_LOCALITY = 5;
    private PromotionCityAdapter promotionCityAdapter;
    private List<City> cityList;
    private Map<String, Integer> selectedClusterMap;
    private int mTradeId;

    public static RetailerListFragment newInstance(int tradeId) {
        Bundle bundle = new Bundle();
        bundle.putInt(TRADE_ID,tradeId);
        RetailerListFragment fragment = new RetailerListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_retailer_list_deals;
    }

    public static String getTagName() {
        return RetailerListFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.promote_your_deal);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityList = new ArrayList<>();
        selectedClusterMap = new HashMap<>();
        mTradeId = getArguments().getInt(TRADE_ID);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (cityList.size() > 0) {
            setAreaAdapter();
        } else {
            loadClusterData();
        }

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

    }

    private void setAreaAdapter() {
        if (promotionCityAdapter == null)
            promotionCityAdapter = new PromotionCityAdapter(context, cityList);
        ;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(promotionCityAdapter);

    }


    private void loadClusterData() {
        showProgressDialog();
        getNetworkManager().GetRequest(RequestIdentifier.PROMOTION_RETAILER_CLUSTER_ID.ordinal(),
                WebServiceConstants.PROMOTION_RETAIL_CLUSTER, null, null, this, this, false);
    }



    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_DETAIL_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }

        if (request.getIdentifier() == RequestIdentifier.PROMOTION_RETAILER_CLUSTER_ID.ordinal()) {
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

        if (request.getIdentifier() == RequestIdentifier.PROMOTION_RETAILER_CLUSTER_ID.ordinal()) {
            //Nothing to do
            final String resultJson = responseObject.toString();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    cityList.addAll(getDataListFromJson(resultJson));
                    setAreaAdapter();
                }
            });

        }

        if (request.getIdentifier() == RequestIdentifier.PROMOTION_REQUIRED_ID.ordinal()) {


            getGsonHelper().parse(responseObject.toString(), PromotionRequiredResult.class, new OnGsonParseCompleteListener<PromotionRequiredResult>() {
                @Override
                public void onParseComplete(PromotionRequiredResult promotionRequiredResult) {


                    if (promotionRequiredResult != null && promotionRequiredResult.clusters != null && promotionRequiredResult.clusters.size() > 0) {

                        PreviewPromotionFragment previewPromotionFragment = PreviewPromotionFragment.newInstance(new Gson().toJson(promotionRequiredResult.clusters));
                        addToBackStack((BaseActivity) getActivity(), previewPromotionFragment, previewPromotionFragment.getTag());

                    }

                }

                @Override
                public void onParseFailure(Exception exception) {
                    CustomLogger.d("Exception  dataparsing");
                    APIError er = new APIError();
                    er.message = exception.getMessage();
                    CustomLogger.e("c ", exception);
                    showError(er.message, MESSAGETYPE.SNACK_BAR);
                }


            });


        }

        return true;
    }


    private List<City> getDataListFromJson(String responseJson) {
        List<City> cityList = new ArrayList<>();

        try {

            JSONObject responseObj = new JSONObject(responseJson.trim());

            // First Iteration for getting the cities details
            Iterator<?> cityKeys = responseObj.keys();

            while (cityKeys.hasNext()) {

                String cityKey = (String) cityKeys.next();

                if (responseObj.get(cityKey) instanceof JSONObject) {

                    City city = new City();

                    JSONObject cityObj = (JSONObject) responseObj.get(cityKey);
                    JSONObject districtObj = cityObj.getJSONObject("districts");

                    city.name = cityKey;
                    city.count = (Integer) cityObj.get("count");

                    // Second  Iteration for getting the district  details
                    Iterator<?> districtKeys = districtObj.keys();

                    while (districtKeys.hasNext()) {

                        String districtKey = (String) districtKeys.next();
                        JSONObject localityObj = (JSONObject) districtObj.get(districtKey);
                        District district = new District();
                        district.name = districtKey;
                        district.count = (Integer) localityObj.get("count");


                        JSONArray localityArr = (JSONArray) localityObj.get("localities");

                        int maximumNumberOfLocality = 0;

                        if (localityArr.length() > MAX_LOCALITY)
                            maximumNumberOfLocality = MAX_LOCALITY;
                        else
                            maximumNumberOfLocality = localityArr.length();

                        // Third   Iteration for getting the locality  array  Show only 5 locality
                        for (int i = 0; i < maximumNumberOfLocality; i++) {

                            JSONObject singleLocationObj = localityArr.getJSONObject(i);

                            Iterator<?> locationKeys = singleLocationObj.keys();
                            while (locationKeys.hasNext()) {
                                Locality locality = new Locality();
                                String locationKey = (String) locationKeys.next();
                                locality.name = locationKey;
                                locality.count = (Integer) singleLocationObj.get(locationKey);
                                district.localityList.add(locality);
                            }

                        }

                        city.districtList.add(district);
                    }

                    cityList.add(city);
                }
            }
        } catch (Exception e) {
            showError();
            CustomLogger.e("Exception :-", e);
        }


        return cityList;
    }


    private void updateSelectedMap(District district) {

        if (selectedClusterMap.containsKey(district.name))
            selectedClusterMap.remove(district.name);
        else
            selectedClusterMap.put(district.name, district.count);

        CustomLogger.d("Selected Map Size " + selectedClusterMap.size());
    }


    @Override
    public void onEvent(EventObject eventObject) {


        if (eventObject.id == EventConstant.SELECTED_CLUSTER_ID) {
            District district = (District) eventObject.objects[0];
            updateSelectedMap(district);
        }

    }

    private PromotionPostData getSelectedPromotionParam() {

        PromotionPostData promotionParam = null;
        int totalCredit = 0;

        if (selectedClusterMap.size() > 0) {
            promotionParam = new PromotionPostData();

            for (Map.Entry<String, Integer> district : selectedClusterMap.entrySet()) {
                promotionParam.clusterList.add(district.getKey());
                totalCredit = totalCredit + district.getValue();
            }

            promotionParam.creditRequired = totalCredit / 2;
            promotionParam.tradeId = mTradeId;

        }
        return promotionParam;

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == nextBT.getId()) {

            PromotionPostData promotionParam = getSelectedPromotionParam();

            if (promotionParam != null) {
                PreviewPromotionFragment previewPromotionFragment = PreviewPromotionFragment.newInstance(new Gson().toJson(promotionParam));
                addToBackStack((BaseActivity) getActivity(), previewPromotionFragment, previewPromotionFragment.getTag());
            } else {
                showError(getString(R.string.please_select_area), MESSAGETYPE.SNACK_BAR);
            }


        }
    }
}


package com.partseazy.android.ui.fragments.supplier.shop;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.supplier.geolocation.GeoBox;
import com.partseazy.android.ui.model.supplier.geolocation.GeoPlaceData;
import com.partseazy.android.ui.model.supplier.geolocation.LocalityLocation;
import com.partseazy.android.ui.model.supplier.search.FinalSearchData;
import com.partseazy.android.ui.model.supplier.shop.ShopResultData;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by naveen on 6/9/17.
 */

public abstract class ShopsDetailBaseFragment extends BaseFragment {

    protected static FinalSearchData finalSearchData;

    public FinalSearchData getFinalSearchData() {

        if (finalSearchData == null)
            finalSearchData = new FinalSearchData();

        return finalSearchData;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finalSearchData = getFinalSearchData();
    }

    protected void getRetailerShopsList(int pageCount) {

        showProgressBar();
        finalSearchData.page = pageCount;
        callShopListData();
//        Map requestParamsMap = WebServicePostParams.getPageParams(pageCount);
//        getNetworkManager().PostRequest(RequestIdentifier.GET_BROWSE_SHOPS_ID.ordinal(),
//                WebServiceConstants.BROWSE_SHOPS_DATA, requestParamsMap, null, this, this, false);

    }

    protected void callShopListData() {
        showProgressDialog(false);
        Map params = WebServicePostParams.searchShopsParams(finalSearchData);
        getNetworkManager().PostRequest(RequestIdentifier.APPLY_SEARCH_SHOPS_REQUEST_ID.ordinal(),
                WebServiceConstants.POST_BROWSE_SHOP, params, null, this, this, false);
    }


    protected void setGeoLocation(Map<String, GeoPlaceData> geoPlaceDataMap) {
        List<LocalityLocation> locationList = new ArrayList<>();
        List<GeoBox> geoBoxList = new ArrayList<>();
        if (geoPlaceDataMap != null && geoPlaceDataMap.size() > 0) {
            for (Map.Entry<String, GeoPlaceData> geoMapItem : geoPlaceDataMap.entrySet()) {
                GeoPlaceData placeData = geoMapItem.getValue();

                LocalityLocation localityLocation = new LocalityLocation();
                localityLocation.lat = placeData.geometry.location.lat;
                localityLocation.lon = placeData.geometry.location.lon;
                locationList.add(localityLocation);

                GeoBox geoBox = new GeoBox();
                geoBox.topLeft = new LocalityLocation();
                geoBox.bottomRight = new LocalityLocation();
                geoBox.topLeft.lon = placeData.geometry.bounds.northEast.lon;
                geoBox.topLeft.lat = placeData.geometry.bounds.northEast.lat;
                geoBox.bottomRight.lon = placeData.geometry.bounds.southWest.lon;
                geoBox.bottomRight.lat = placeData.geometry.bounds.southWest.lat;
                geoBoxList.add(geoBox);
            }
        }else{
            finalSearchData.geoLocation = null;
            finalSearchData.geoBox = null;

        }
        if(locationList!=null && locationList.size()>0) {
            finalSearchData.geoLocation = new Gson().toJson(locationList);
            CustomLogger.d("location String::" + finalSearchData.geoLocation);
        }
        if(geoBoxList!=null && geoBoxList.size()>0) {
            finalSearchData.geoBox = new Gson().toJson(geoBoxList);
            CustomLogger.d("location geobox String::" + finalSearchData.geoBox);
        }
    }


    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.APPLY_SEARCH_SHOPS_REQUEST_ID.ordinal() || request.getIdentifier() == RequestIdentifier.GET_BROWSE_SHOPS_ID.ordinal() ) {
            hideProgressBar();


            getGsonHelper().parse(responseObject.toString(), ShopResultData.class, new OnGsonParseCompleteListener<ShopResultData>() {
                        @Override
                        public void onParseComplete(ShopResultData data) {
                            if (data != null) {
                                handleBrowseShopsDataResponse(data);
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            APIError er = new APIError();
                            er.message = exception.getMessage();
                            CustomLogger.e("Exception ", exception);
                            handleBrowseShopsDataError(request, er);
                        }
                    }
            );


        }

        return true;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        if (request.getIdentifier() == RequestIdentifier.GET_BROWSE_SHOPS_ID.ordinal()) {
            hideProgressBar();
            showError();
            handleBrowseShopsDataError(request, apiError);

        }
        if(request.getIdentifier() == RequestIdentifier.APPLY_SEARCH_SHOPS_REQUEST_ID.ordinal())
        {
            // showError();
            handleBrowseShopsDataError(request, apiError);
        }


        return true;
    }



    protected boolean handleBrowseShopsDataResponse(ShopResultData data) {
        return false;
    }

    protected boolean handleBrowseShopsDataError(Request request, APIError error) {
        return false;
    }
}

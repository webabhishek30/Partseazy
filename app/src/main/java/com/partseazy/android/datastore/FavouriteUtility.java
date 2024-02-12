package com.partseazy.android.datastore;

import com.partseazy.android.Logger.CustomLogger;

import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.fav.FavData;
import com.partseazy.android.ui.model.fav.FavResultData;

import java.util.HashMap;
import java.util.Map;

import static com.partseazy.android.network.request.WebServiceConstants.REMOVE_FAV_ITEM;

/**
 * Created by Naveen Kumar on 1/2/17.
 */

public class FavouriteUtility {


    private static Map<Integer, String> favDataMap;


    public static boolean isFavByProductMasterId(int productMasterId) {
        if (favDataMap != null && favDataMap.size() > 0) {
            if (favDataMap.containsKey(productMasterId)) {
                return true;
            }
        }
        return false;
    }


    public static void updateFavMapOnAddRemove(FavResultData favResultData) {

        if (favDataMap == null)
            favDataMap = new HashMap<>();
        else
            favDataMap.clear();

        if (favResultData.result != null) {

            if (favResultData.result.size() == 0) {
                favDataMap.clear();
                return;
            }
            for (FavData favData : favResultData.result) {
                favDataMap.put(favData.id, favData.type);
            }
        }
    }


    public static void callAddProductToFavourite(BaseFragment baseFragment, int productMasterId) {
        baseFragment.showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        baseFragment.getNetworkManager().PutRequest(RequestIdentifier.ADD_FAV_PRODUCT_REQUEST_ID.ordinal(),
                WebServiceConstants.PUT_PRODUCT_TO_FAV,
                WebServicePostParams.addProductToFavouriteParams(productMasterId),
                params, baseFragment, baseFragment, false);

    }

    public static void callRemovefavItemRequest(BaseFragment baseFragment, int productMasterId) {
        baseFragment.showProgressDialog();
        RequestParams paramsHeader = new RequestParams();
        paramsHeader.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(paramsHeader.headerMap);
        Map params = WebServicePostParams.removeFavProduct(productMasterId);
        baseFragment.getNetworkManager().PutRequest(RequestIdentifier.REMOVE_FAV_PRODUCT_REQUEST_ID.ordinal(),
                REMOVE_FAV_ITEM, params, paramsHeader, baseFragment, baseFragment, false);
    }


    public static void callGetfavItemRequest(BaseFragment baseFragment) {
        CustomLogger.d("Time to load Favourite");
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        baseFragment.getNetworkManager().GetRequest(RequestIdentifier.FAV_ID.ordinal(),
                WebServiceConstants.FAV_DETAIL_LIST_ITEM, null, params, baseFragment, baseFragment, false);
    }

    public static void clearMap() {

        if (favDataMap != null)
            favDataMap.clear();
    }


}

package com.partseazy.android.ui.fragments.favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.ui.adapters.favourite.FavProductAdapter;
import com.partseazy.android.ui.callbacks.TabCountCallback;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.fav.FavData;
import com.partseazy.android.ui.model.fav.FavResultData;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by can on 19/12/16.
 */

public class FavProductFragment extends BaseFragment {

    @BindView(R.id.scrollable)
    protected RecyclerView scrollable;
    @BindView(R.id.noResultContinueBT)
    protected Button noResultContinueBT;

    private FavProductAdapter favProductAdapter;
    private ArrayList<FavData> favData;

    TabCountCallback tabCountCallback; //TODO

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_fav;
    }

    @Override
    protected String getFragmentTitle() {
        return "";
    }

    public static FavProductFragment newInstance() {
        Bundle bundle = new Bundle();
        FavProductFragment fragment = new FavProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getTagName() {
        return FavProductFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        scrollable.setLayoutManager(new GridLayoutManager(getContext(), 2));
        showProgressBar();
        FavouriteUtility.callGetfavItemRequest(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initAdapter(ArrayList<FavData> cartItems) {
        if (favProductAdapter == null)
            favProductAdapter = new FavProductAdapter(this, cartItems);
        scrollable.setAdapter(favProductAdapter);
        scrollable.setHasFixedSize(true);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.FAV_ID.ordinal() || request.getIdentifier() == RequestIdentifier.REMOVE_FAV_PRODUCT_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {
                            try {
                                hideProgressDialog();
                                hideProgressBar();
                                favData = (ArrayList<FavData>) data.result;
                                if (favData != null) {

                                    PartsEazyEventBus.getInstance().postEvent(EventConstant.UPDATE_TAB_COUNT_FAV_PRODUCT_ID, favData.size());

                                    if (favData.size() == 0) {

                                        FavouriteUtility.updateFavMapOnAddRemove(data);

                                        if (getActivity() != null) {

                                            RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) noResultContinueBT.getLayoutParams();
                                            buttonLayoutParams.setMargins(buttonLayoutParams.leftMargin, buttonLayoutParams.topMargin, buttonLayoutParams.rightMargin, 200);
                                            noResultContinueBT.setLayoutParams(buttonLayoutParams);

                                            showNoResult(getString(R.string.no_favourites), R.drawable.no_favourite_icon, scrollable);
                                        }
                                        return;
                                    }
                                    initAdapter(favData);
                                    FavouriteUtility.updateFavMapOnAddRemove(data);
                                    favProductAdapter.notifyAdapter(favData);
                                    hideNoResult(scrollable);
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }
                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );
        }

        return true;
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.REMOVE_FAV_ITEM_ID) {
            int product_master_id = (int) eventObject.objects[0];
            FavouriteUtility.callRemovefavItemRequest(this, product_master_id);
        }

        if (eventObject.id == EventConstant.UPDATE_FAV_COUNT_ID) {
            showProgressBar();
            FavouriteUtility.callGetfavItemRequest(this);

        }
    }

}

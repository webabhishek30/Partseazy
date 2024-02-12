package com.partseazy.android.ui.fragments.deals.sell_deal;

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
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.sell.ViewsAdapter;
import com.partseazy.android.ui.model.deal.views.ViewResultList;
import com.partseazy.android.ui.model.error.APIError;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by shubhang on 06/11/17.
 */

public class ViewsDetailFragment extends BaseFragment {

    @BindView(R.id.viewsRV)
    protected RecyclerView recyclerView;

    public static final String TRADE_ID = "trade_id";
    private int tradeID;

    private ViewsAdapter adapter;

    public static ViewsDetailFragment newInstance(int tradeID) {
        Bundle bundle = new Bundle();
        bundle.putInt(TRADE_ID, tradeID);
        ViewsDetailFragment fragment = new ViewsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tradeID = getArguments().getInt(TRADE_ID);
    }


    public static String getTagName() {
        return ViewsDetailFragment.class.getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadViewers();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_people_who_viewed_deal;
    }


    @Override
    protected String getFragmentTitle() {
        return getString(R.string.people_who_viewed);
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

    private void loadViewers() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_TRADE_VIEW_USERS.ordinal(),
                WebServiceConstants.GET_TRADE_VIEWERS + tradeID + "/viewers", null, params, this, this, false);
    }

    private void setupViewsAdapter(ViewResultList data) {
        adapter = new ViewsAdapter(this, data.result);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressBar();
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.GET_TRADE_VIEW_USERS.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), ViewResultList.class, new OnGsonParseCompleteListener<ViewResultList>() {
                        @Override
                        public void onParseComplete(ViewResultList data) {
                            if (data.result != null && data.result.size() > 0) {
                                setupViewsAdapter(data);
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
        if (request.getIdentifier() == RequestIdentifier.GET_TRADE_VIEW_USERS.ordinal()) {
            showError();
        }
        return true;
    }
}

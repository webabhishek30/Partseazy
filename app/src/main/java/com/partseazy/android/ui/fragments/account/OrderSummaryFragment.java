package com.partseazy.android.ui.fragments.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
import com.partseazy.android.ui.adapters.account.OrderSummaryAdapter;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.myorders.ordersummary.OrderSummaryResult;
import com.partseazy.android.utility.dialog.SnackbarFactory;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 29/1/17.
 */

public class OrderSummaryFragment extends BaseFragment {

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.orderDataView)
    protected FrameLayout orderDataView;

    public static final String ORDER_ID = "ORDER_ID";
    private OrderSummaryAdapter orderSummaryAdapter;
    private String orderID;
    private OrderSummaryResult orderSummaryDetail;

    public static OrderSummaryFragment newInstance(String orderId) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(OrderSummaryFragment.ORDER_ID, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderID = getArguments().getString(ORDER_ID);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (orderSummaryDetail!= null) {
            setOrderAdapter();
        } else {
            loadOrderData();
        }
    }

    public static String getTagName() {
        return OrderSummaryFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_summary;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.order_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void setOrderAdapter() {
        if (orderSummaryAdapter == null)
            orderSummaryAdapter = new OrderSummaryAdapter(getContext(),orderSummaryDetail);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(orderSummaryAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private void loadOrderData() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        getNetworkManager().GetRequest(RequestIdentifier.ORDER_DETAIL_SUMMARY_ID.ordinal(),
                WebServiceConstants.GET_ORDER_DETAILS+orderID, null, params, this, this, false);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        if(apiError!=null) {
            SnackbarFactory.showSnackbar(getContext(),apiError.message);
        }else{
            SnackbarFactory.showSnackbar(getContext(), getString(R.string.err_somthin_wrong));
        }
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.ORDER_DETAIL_SUMMARY_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), OrderSummaryResult.class, new OnGsonParseCompleteListener<OrderSummaryResult>() {
                        @Override
                        public void onParseComplete(OrderSummaryResult data) {
                            hideProgressBar();
                            if(data!=null) {
                                orderSummaryDetail = data;
                                setOrderAdapter();
                                hideNoResult(orderDataView);
                            }else{
                                showNoResult(context.getString(R.string.err_somthin_wrong),orderDataView);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                           hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

        return true;
    }
}
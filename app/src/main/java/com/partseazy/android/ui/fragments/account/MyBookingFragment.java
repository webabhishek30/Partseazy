package com.partseazy.android.ui.fragments.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.account.BookingListAdapter;
import com.partseazy.android.ui.model.deal.bookingorder.BookingData;
import com.partseazy.android.ui.model.deal.bookingorder.BookingResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.dialog.SnackbarFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/6/17.
 */

public class MyBookingFragment extends BaseFragment {

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;;
    @BindView(R.id.orderDataView)
    protected FrameLayout orderDataView;

    @BindView(R.id.noResultContinueBT)
    protected Button noResultContinueBT;

    private BookingListAdapter bookingListAdapter;
    private List<BookingData> orderList;


    public static MyBookingFragment newInstance() {
        MyBookingFragment fragment = new MyBookingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (orderList != null && orderList.size() > 0) {
            setOrderAdapter();
        } else {
            loadOrderList();
        }
    }

    public static String getTagName() {
        return MyBookingFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_orders;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.booking_summary);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
       // noResultContinueBT.setVisibility(View.GONE);
        return view;
    }

    private void setOrderAdapter() {
        if (bookingListAdapter == null)
            bookingListAdapter = new BookingListAdapter(getContext(), orderList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(bookingListAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private void loadOrderList() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.RECENT_BOOKING_LIST_ID.ordinal(),
                WebServiceConstants.GET_BOOKING_RECENT, null, params, this, this, false);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        SnackbarFactory.showSnackbar(getContext(), getString(R.string.err_somthin_wrong));
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.RECENT_BOOKING_LIST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), BookingResult.class, new OnGsonParseCompleteListener<BookingResult>() {
                        @Override
                        public void onParseComplete(BookingResult data) {
                            try {
                                hideProgressBar();
                                CustomLogger.d("data size ::" + data.bookingDataList.size());
                                if (data.bookingDataList != null && data.bookingDataList.size() > 0) {
                                    orderList = new ArrayList<BookingData>();
                                    orderList.addAll(data.bookingDataList);
                                    setOrderAdapter();
                                    hideNoResult(orderDataView);
                                } else {
                                    showNoResult(getString(R.string.no_booking_found), R.drawable.no_order_icon, orderDataView);
                                }
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getNetworkManager().cancelAll();
    }
}

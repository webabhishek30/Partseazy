package com.partseazy.android.ui.fragments.deals.sell_deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.adapters.deals.sell.DealOrderDetailAdapter;
import com.partseazy.android.ui.adapters.product.ProductBannerAdapter;
import com.partseazy.android.ui.model.deal.bookingorder.summary.BookingSummaryResult;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.ChatUtility;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by naveen on 16/5/17.
 */

public class DealOrderDetailFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.scrollable)
    protected RecyclerView recyclerView;

    @BindView(R.id.chatBT)
    protected Button chatBT;

    @BindView(R.id.callBT)
    protected Button callBT;

    @BindView(R.id.contactNameTV)
    protected TextView contactNameTV;

    @BindView(R.id.dealBTLL)
    protected LinearLayout dealBTLL;


    private DealOrderDetailAdapter dealOrderDetailAdapter;
    public static final String BOOKING_ID = "booking_id";
    public static String DEAL_HOLDER = "deal_holder";
    private static final String LAUNCH_SCREEN_NAME = "launchScreenName";

    private String bookingId;
    private String launchScreenName;
    private Deal dealDetailHolder;
    private BookingSummaryResult bookingSummaryDetail;

    public static DealOrderDetailFragment newInstance() {
        Bundle bundle = new Bundle();
        DealOrderDetailFragment fragment = new DealOrderDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DealOrderDetailFragment newInstance(String bkin, String dealdetailHolder, String launchScreen) {
        Bundle bundle = new Bundle();
        DealOrderDetailFragment fragment = new DealOrderDetailFragment();
        bundle.putString(BOOKING_ID, bkin);
        bundle.putString(DEAL_HOLDER, dealdetailHolder);
        bundle.putString(LAUNCH_SCREEN_NAME, launchScreen);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingId = getArguments().getString(BOOKING_ID);
        launchScreenName = getArguments().getString(LAUNCH_SCREEN_NAME, null);
        String detailDetailJson = getArguments().getString(DEAL_HOLDER);
        dealDetailHolder = new Gson().fromJson(detailDetailJson, Deal.class);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (bookingSummaryDetail != null) {
            setOrderAdapter();
        } else {
            loadBookingData();
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_deal_order_detail;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.booking_summary);
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
        chatBT.setOnClickListener(this);
        callBT.setOnClickListener(this);
    }

    private void setOrderAdapter() {
        if (dealOrderDetailAdapter == null)
            dealOrderDetailAdapter = new DealOrderDetailAdapter(context, bookingSummaryDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(dealOrderDetailAdapter);
    }

    private void loadBookingData() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        getNetworkManager().GetRequest(RequestIdentifier.BOOKIND_DETAIL_SUMMARY.ordinal(),
                WebServiceConstants.GET_BOOKING_DETAILS + bookingId, null, params, this, this, false);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.BOOKIND_DETAIL_SUMMARY.ordinal()) {
            if (apiError != null) {
                SnackbarFactory.showSnackbar(getContext(), apiError.message);
            } else {
                SnackbarFactory.showSnackbar(getContext(), getString(R.string.err_somthin_wrong));
            }
        }
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.BOOKIND_DETAIL_SUMMARY.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), BookingSummaryResult.class, new OnGsonParseCompleteListener<BookingSummaryResult>() {
                        @Override
                        public void onParseComplete(BookingSummaryResult data) {
                            hideProgressBar();
                            if (data != null) {
                                bookingSummaryDetail = data;
                                setOrderAdapter();
                                if (getActivity() != null && isAdded()) {
                                    contactNameTV.setText(getString(R.string.contact, bookingSummaryDetail.user.name));
                                    dealBTLL.setVisibility(View.VISIBLE);
                                }
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
    public void onClick(View view) {

        if (view.getId() == chatBT.getId()) {
            ChatUtility chat = new ChatUtility(this, bookingSummaryDetail.tradeBooking.userId,
                    bookingSummaryDetail.user.name, bookingSummaryDetail.trade.images.get(0).src,
                    bookingSummaryDetail.trade.name, bookingSummaryDetail.trade.trin + "");
            chat.startChatting();
        }

        if (view.getId() == callBT.getId()) {
            CommonUtility.dialPhoneNumber(getActivity(), bookingSummaryDetail.user.mobile);
        }
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();


        if (launchScreenName != null && launchScreenName.equals(ProductBannerAdapter.DEAL_BUY_LAUNCH_FOR_YOUTUBE)) {
            popToHome(getActivity());
            addToBackStack((BaseActivity) getActivity(), SellDealDetailFragment.newInstance(dealDetailHolder.trade.id, dealDetailHolder.trade.name), SellDealDetailFragment.getTagName());
            return false;
        }
        return true;

    }
}

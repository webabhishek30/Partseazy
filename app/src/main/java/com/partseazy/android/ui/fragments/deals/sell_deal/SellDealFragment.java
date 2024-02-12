package com.partseazy.android.ui.fragments.deals.sell_deal;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.sell.ReportCardAdapter;
import com.partseazy.android.ui.adapters.deals.sell.SellDealAdapter;
import com.partseazy.android.ui.fragments.deals.create_deal.NewDealBasicInfoFragment;
import com.partseazy.android.ui.model.deal.selldeallist.DealItem;
import com.partseazy.android.ui.model.deal.selldeallist.DealListResult;
import com.partseazy.android.ui.model.deal.selldeallist.ReportCardItem;
import com.partseazy.android.ui.model.deal.selldeallist.ReportCardList;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.widget.EndlessRecyclerOnScrollListener;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 26/4/17.
 */

public class SellDealFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.card_view)
    protected CardView cardView;

    @BindView(R.id.contentLL)
    protected LinearLayout contentLL;

    @BindView(R.id.scrollable)
    protected RecyclerView recyclerView;

    @BindView(R.id.createDealBT)
    protected Button createDealBT;

    @BindView(R.id.noDealLL)
    protected LinearLayout noDealLL;

    @BindView(R.id.toolbarLL)
    protected LinearLayout toolbarLL;

    @BindView(R.id.toolbarSell)
    protected Toolbar toolbar;

    @BindView(R.id.totalBookingValueTV)
    protected TextView totalBookingValueTV;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private LinearLayoutManager linearLayoutManager;

    private SellDealAdapter sellDealAdapter;
    private List<DealItem> selldealList;

    private ReportCardAdapter reportCardAdapter;
    private List<ReportCardItem> reportCardList;

    protected int PAGE_COUNT = 1;

    public static final String LAUNCH_REPORT_CARD = "launch_report_card";
    protected boolean isReportCard = false;

    private int totalBookingValue = 0;

    public static SellDealFragment newInstance() {
        Bundle bundle = new Bundle();
        SellDealFragment fragment = new SellDealFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SellDealFragment newInstance(boolean isReportCard) {
        Bundle bundle = new Bundle();
        SellDealFragment fragment = new SellDealFragment();
        bundle.putBoolean(LAUNCH_REPORT_CARD, isReportCard);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getTagName() {
        return SellDealFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selldealList = new ArrayList<>();
        reportCardList = new ArrayList<>();
        isReportCard = getArguments().getBoolean(LAUNCH_REPORT_CARD, isReportCard);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sell_deals;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (selldealList.size() > 0 || reportCardList.size() > 0) {
            setSellAdapter();
        }
    }

    @Override
    protected String getFragmentTitle() {
        if (isReportCard)
            return "Report Card";
        else
            return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        createDealBT.setVisibility(View.VISIBLE);
        createDealBT.setOnClickListener(this);

        if (isReportCard) {
            toolbarLL.setVisibility(View.VISIBLE);
            if (toolbar != null) {
                toolbar.setNavigationIcon(R.drawable.back_arrow);
                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                toolbar.setTitleTextColor(Color.WHITE);
                if (getFragmentTitle() != null)
                    toolbar.setTitle(getFragmentTitle());
                context.setSupportActionBar(toolbar);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        KeyPadUtility.hideSoftKeypad(getActivity());
                        ((BaseActivity) getActivity()).onPopBackStack(false);
                    }
                });
            }
        }
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSellAdapter();
        if (selldealList.size() == 0 || reportCardList.size() == 0)
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    loadSellDeal();
                }
            });
    }


    private void loadSellDeal() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        if (isReportCard) {
            Map<String, String> sellParams = WebServicePostParams.reportCardParams(DataStore.getUserID(context));
            getNetworkManager().GetRequest(RequestIdentifier.GET_REPORT_CARD.ordinal(),
                    WebServiceConstants.GET_REPORT_CARD, sellParams, params, this, this, false);
        } else {
            Map<String, String> sellParams = WebServicePostParams.sellDealParams(PAGE_COUNT);
            getNetworkManager().GetRequest(RequestIdentifier.GET_SELL_DEAL.ordinal(),
                    WebServiceConstants.GET_SELL_TRADE, sellParams, params, this, this, false);
        }
    }

    private void setSellAdapter() {
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (isReportCard) {
            if (reportCardAdapter == null)
                reportCardAdapter = new ReportCardAdapter(context, reportCardList);
            recyclerView.setAdapter(reportCardAdapter);
        } else {
            if (sellDealAdapter == null)
                sellDealAdapter = new SellDealAdapter(context, selldealList);
            recyclerView.setAdapter(sellDealAdapter);
            setEndLessRecyclerView();
        }
    }

    private void setEndLessRecyclerView() {
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                PAGE_COUNT = page;
                loadSellDeal();
            }
        };
        recyclerView.setOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void hideContentLayout() {
        contentLL.setVisibility(View.GONE);
    }

    private void showContentLayout() {
        contentLL.setVisibility(View.VISIBLE);
        noDealLL.setVisibility(View.GONE);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());
        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }
        if (request.getIdentifier() == RequestIdentifier.GET_REPORT_CARD.ordinal()) {
            if (apiError != null)
                showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.GET_SELL_DEAL.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), DealListResult.class, new OnGsonParseCompleteListener<DealListResult>() {
                @Override
                public void onParseComplete(DealListResult data) {
                    if (data != null) {

                        if (data.dealItemList.size() > 0) {
                            selldealList.addAll(data.dealItemList);
                            sellDealAdapter.notifyDataSetChanged();
                            showContentLayout();
                            noDealLL.setVisibility(View.GONE);
                        } else {
                            if (selldealList.size() == 0) {
                                hideContentLayout();
                                noDealLL.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                }

                @Override
                public void onParseFailure(Exception exception) {
                    CustomLogger.e("Exception ", exception);
                    showError(exception.getMessage(), MESSAGETYPE.SNACK_BAR);
                }


            });
        }

        if (request.getIdentifier() == RequestIdentifier.GET_REPORT_CARD.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), ReportCardList.class, new OnGsonParseCompleteListener<ReportCardList>() {
                @Override
                public void onParseComplete(ReportCardList data) {
                    if (data != null) {
                        try {
                            if (data.dealItemList.size() > 0) {
                                reportCardList.clear();
                                reportCardList.addAll(data.dealItemList);
                                reportCardAdapter.notifyDataSetChanged();
                                showContentLayout();
                                noDealLL.setVisibility(View.GONE);

                                cardView.setVisibility(View.VISIBLE);
                                totalBookingValue = 0;
                                for (int i = 0; i < data.dealItemList.size(); i++) {
                                    totalBookingValue += Integer.parseInt(data.dealItemList.get(i).totalBookingAmnt);
                                }
                                totalBookingValueTV.setText(getString(R.string.booking_amount_1_s, String.valueOf(
                                        CommonUtility.convertionPaiseToRupee(totalBookingValue))));
                                totalBookingValueTV.setVisibility(View.VISIBLE);
                            } else {
                                if (reportCardList.size() == 0) {
                                    hideContentLayout();
                                    noDealLL.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            CustomLogger.e("Exception ", e);
                        }
                    }

                }

                @Override
                public void onParseFailure(Exception exception) {
                    CustomLogger.e("Exception ", exception);
                    showError(exception.getMessage(), MESSAGETYPE.SNACK_BAR);
                }


            });
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        if (createDealBT.getId() == v.getId()) {
            addToBackStack((BaseActivity) context, NewDealBasicInfoFragment.newInstance(), NewDealBasicInfoFragment.getTagName());

//            int totalPayment =4000;
//            addToBackStack((BaseActivity)context, ThankYouBookingFragment.newInstance(totalPayment),ThankYouBookingFragment.getTagName());

        }
    }
}

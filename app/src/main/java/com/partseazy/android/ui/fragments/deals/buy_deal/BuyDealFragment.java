package com.partseazy.android.ui.fragments.deals.buy_deal;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.buy.BuyDealAdapter;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.model.deal.filters.DealFilterListResult;
import com.partseazy.android.ui.model.deal.filters.FilterMaster;
import com.partseazy.android.ui.model.deal.filters.FilterValue;
import com.partseazy.android.ui.model.deal.selldeallist.DealItem;
import com.partseazy.android.ui.model.deal.selldeallist.DealListResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.widget.EndlessRecyclerOnScrollListener;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;


/**
 * Created by naveen on 25/4/17.
 */

public class BuyDealFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, DealFilterFragment.OnFilterAppliedListener {

    @BindView(R.id.scrollable)
    protected RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noDealRL)
    protected RelativeLayout noDealRL;

    @BindView(R.id.contentLL)
    protected LinearLayout contentLL;

    @BindView(R.id.noDealCategoryTV)
    protected TextView noDealCategoryTV;

    @BindView(R.id.noDealTV)
    protected TextView noDealTV;

    @BindView(R.id.supplierFilterTV)
    protected TextView supplierFilterTV;


    public static final String SUPPLIER_FILTER_PARAM_DEEP_LINK = "supplier_filter_param";
    public static final String CATEGORY_FILTER_PARAM_DEEP_LINK = "category_filter_param";

    private BuyDealAdapter buyDealAdapter;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private LinearLayoutManager linearLayoutManager;

    private List<DealItem> buyDealList;
    public List<FilterMaster> filterMasterList;
    private Set<String> selectedCategorySet;
    private Map<String, Set<FilterValue>> selectedFilterMap;


    private int isPublicDeal = 1;
    protected int PAGE_COUNT = 1;
    private boolean isLoading = false;
    private boolean showSupplierPromotion = false;
    DealFilterFragment.OnFilterAppliedListener listener;
    private int supplierFilterId;

    private int categoryFilterId;

    public static BuyDealFragment newInstance() {
        Bundle bundle = new Bundle();
        BuyDealFragment fragment = new BuyDealFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BuyDealFragment newInstance(String filterId, String deepLinkType) {
        Bundle bundle = new Bundle();
        if (SUPPLIER_FILTER_PARAM_DEEP_LINK.equals(deepLinkType)) {
            bundle.putString(SUPPLIER_FILTER_PARAM_DEEP_LINK, filterId);
        } else if (CATEGORY_FILTER_PARAM_DEEP_LINK.equals(deepLinkType)) {
            bundle.putString(CATEGORY_FILTER_PARAM_DEEP_LINK, filterId);
        }
        BuyDealFragment fragment = new BuyDealFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BuyDealFragment newInstance(DealFilterFragment.OnFilterAppliedListener listener) {
        Bundle bundle = new Bundle();
        BuyDealFragment fragment = new BuyDealFragment();
        fragment.setArguments(bundle);
        fragment.setOnFilterChangeNotifier(listener);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_buy_deals;
    }

    @Override
    protected String getFragmentTitle() {
        return null;
    }

    public static String getTagName() {
        return BuyDealFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buyDealList = new ArrayList<>();
        buyDealList.add(new DealItem());
        filterMasterList = new ArrayList<>();
        selectedFilterMap = new HashMap<>();
        selectedCategorySet = new HashSet<>();

        String supplierFilterStr = getArguments().getString(SUPPLIER_FILTER_PARAM_DEEP_LINK);
        if (supplierFilterStr != null && !supplierFilterStr.equals("")) {
            supplierFilterId = Integer.parseInt(supplierFilterStr);
        } else if (DataStore.getSupplierId(context) != null && !DataStore.getSupplierId(context).equals("")) {
            supplierFilterId = Integer.parseInt(DataStore.getSupplierId(context));
        }

        String categoryFilterStr = getArguments().getString(CATEGORY_FILTER_PARAM_DEEP_LINK);
        if (categoryFilterStr != null && !categoryFilterStr.equals("")) {
            categoryFilterId = Integer.parseInt(categoryFilterStr);
        } else if (DataStore.getCategoryId(context) != null && !DataStore.getCategoryId(context).equals("")) {
            categoryFilterId = Integer.parseInt(DataStore.getCategoryId(context));
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (filterMasterList.size() > 0) {
            setBuyAdapter(true);
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (filterMasterList.size() == 0) {
            loadDealFiltersList();
        }

    }


    private void loadBuyDeal() {
        isLoading = true;
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        Map<String, String> buyRequestParams = WebServicePostParams.buyDealParams(PAGE_COUNT, isPublicDeal);
        Map<String, String> filterParams = getRequestParams(selectedFilterMap);
        if (filterParams != null && filterParams.size() > 0) {
            buyRequestParams.putAll(filterParams);
        }


        getNetworkManager().GetRequest(RequestIdentifier.GET_BUY_DEAL.ordinal(),
                WebServiceConstants.GET_BUY_TRADE, buyRequestParams, params, this, this, false);
    }


    private void loadDealFiltersList() {
        showProgressDialog(getString(R.string.loading), false);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_DEAL_FILTERS_LIST.ordinal(),
                WebServiceConstants.DEAL_FILTER_LIST, null, params, this, this, false);
    }


    private void callTradeMetricsApi(int tradeId) {
        int identifier = -tradeId;
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        Map<String, Object> requestParams = WebServicePostParams.dealViewOpenMetricsParams(tradeId, true);
        getNetworkManager().PutRequest(identifier, WebServiceConstants.UPDATE_DEAL_METRICS, requestParams, params, this, this, false);
    }


    private void sortFilterList() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Do something for lollipop and above versions
            if (filterMasterList != null && filterMasterList.size()>0) {
                for (int i = 0; i < filterMasterList.size(); i++) {
                    if (filterMasterList.get(i).code.equals(DealConstants.CATEGORY_FILTER_PARAM_KEY) &&
                            filterMasterList.get(i).values != null) {
                        Collections.sort(filterMasterList.get(i).values, new Comparator<FilterValue>() {
                            @SuppressLint("NewApi")
                            @Override
                            public int compare(FilterValue o1, FilterValue o2) {
                                return -1 * Boolean.compare(o1.isSelected, o2.isSelected);
                            }
                        });
                    }
                }
            }
        }
    }

    private void setBuyAdapter(boolean isLoading) {
        sortFilterList();
        if (buyDealAdapter == null) {
            buyDealAdapter = new BuyDealAdapter(context, buyDealList, filterMasterList);
        }
        buyDealAdapter.isLoading = isLoading;
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(buyDealAdapter);

        setEndLessRecyclerView();

    }


    private void setEndLessRecyclerView() {
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                CustomLogger.d("Page Number ::" + page);
                if (!isLoading) {
                    // PAGE_COUNT = page;
                    PAGE_COUNT++;
                    loadBuyDeal();
                }
            }
        };
        if (recyclerView != null)
            recyclerView.setOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void hideContentLayout() {

        if (getActivity() != null && isAdded()) {
            buyDealList.clear();
            buyDealList.add(new DealItem());
            buyDealAdapter.isLoading = false;
            buyDealAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());
        if (request.getIdentifier() == RequestIdentifier.GET_BUY_DEAL.ordinal()) {
            try {
                if (apiError != null)
                    showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
                else
                    showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);

                swipeRefreshLayout.setRefreshing(false);
            }catch (Exception e){
                CustomLogger.e("Exception ", e);
            }
        } else if (request.getIdentifier() == RequestIdentifier.GET_DEAL_FILTERS_LIST.ordinal()) {
            try {
                if (apiError != null)
                    showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
                else
                    showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
            }catch (Exception e){
                CustomLogger.e("Exception ", e);
            }
        } else {
            showError();
        }


        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.GET_BUY_DEAL.ordinal()) {

            sortFilterList();
            swipeRefreshLayout.setRefreshing(false);
            JSONArray jsonArray = null;
            try {
                JSONObject jsonObj = new JSONObject(responseObject.toString());
                if (jsonObj != null) {
                    jsonArray = jsonObj.getJSONArray("result");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonArray != null && jsonArray.length() > 0) {

                getGsonHelper().parse(responseObject.toString(), DealListResult.class, new OnGsonParseCompleteListener<DealListResult>() {
                    @Override
                    public void onParseComplete(DealListResult data) {
                        try {
                            if (data != null && data.dealItemList != null && data.dealItemList.size() > 0) {
                                buyDealList.addAll(data.dealItemList);
                                if (buyDealAdapter == null) {
                                    buyDealAdapter = new BuyDealAdapter(context, buyDealList, filterMasterList);
                                    recyclerView.setAdapter(buyDealAdapter);
                                } else {
                                    buyDealAdapter.isLoading = false;
                                    buyDealAdapter.notifyDataSetChanged();
                                }
//                            showContentLayout();
                                isLoading = false;

                                if (showSupplierPromotion) {
                                    supplierFilterTV.setText(context.getString(R.string.numbers_deals_found_for, buyDealList.size(), buyDealList.get(1).supplier));
                                    supplierFilterTV.setVisibility(View.VISIBLE);

                                } else {
                                    supplierFilterTV.setVisibility(View.GONE);
                                }

                            }
                        }catch (Exception e){
                            CustomLogger.e("Exception ", e);
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
            } else if (buyDealList.size() == 1)
                if (getActivity() != null && isAdded()) {
                    hideContentLayout();
                }
        }

        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_FILTERS_LIST.ordinal()) {


            getGsonHelper().parse(responseObject.toString(), DealFilterListResult.class, new OnGsonParseCompleteListener<DealFilterListResult>() {
                        @Override
                        public void onParseComplete(DealFilterListResult data) {
                            try {
                                if (data != null && data.result.size() > 0) {
                                    filterMasterList = new ArrayList<>();
                                    filterMasterList.add(new FilterMaster(DealConstants.FILTER_SWITCH_CODE, "Switch Deal", true));

                                    for (FilterMaster filterMaster : data.result) {

                                        if (supplierFilterId > 0 && filterMaster.code.equals(DealConstants.SUPPLIER_FILTER_PARAM_KEY)) {
                                            for (int i = 0; i < filterMaster.values.size(); i++) {
                                                if (filterMaster.values.get(i).id == (supplierFilterId)) {
                                                    DataStore.setSupplierId(context, null);
                                                    filterMaster.values.get(i).isSelected = true;
                                                    Set<FilterValue> filterValueSet = new HashSet<FilterValue>();
                                                    filterValueSet.add(filterMaster.values.get(i));
                                                    selectedFilterMap.put(filterMaster.code, filterValueSet);
                                                }
                                            }
                                        }

                                        if (categoryFilterId > 0 && filterMaster.code.equals(DealConstants.CATEGORY_FILTER_PARAM_KEY)) {
                                            for (int i = 0; i < filterMaster.values.size(); i++) {
                                                if (filterMaster.values.get(i).id == (categoryFilterId)) {
                                                    DataStore.setCategoryId(context, null);
                                                    filterMaster.values.get(i).isSelected = true;
                                                    Set<FilterValue> filterValueSet = new HashSet<FilterValue>();
                                                    filterValueSet.add(filterMaster.values.get(i));
                                                    selectedFilterMap.put(filterMaster.code, filterValueSet);
                                                }
                                            }
                                        }
                                        filterMasterList.add(filterMaster);
                                    }

                                    setBuyAdapter(true);
                                    loadBuyDeal();
                                }
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            APIError er = new APIError();
                            er.message = exception.getMessage();
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


        if (eventObject.id == EventConstant.VIEW_DEAL_METRICS) {
            Integer tradeId = (Integer) eventObject.objects[0];
            callTradeMetricsApi(tradeId);
        }

        if (eventObject.id == EventConstant.OPEN_DEAL_FILTERS) {

            if (filterMasterList != null) {
                openDealFilter();
            } else {
                showError();
            }
        }

        if (eventObject.id == EventConstant.SELECT_DEAL_CATID) {
            FilterValue item = (FilterValue) eventObject.objects[0];
            String filterMasterKey = DealConstants.CATEGORY_FILTER_PARAM_KEY;
            if (selectedFilterMap.containsKey(filterMasterKey)) {
                Set<FilterValue> valueSet = selectedFilterMap.get(filterMasterKey);
                boolean add = true;
                for (FilterValue selectedValueItem : valueSet) {
                    if (selectedValueItem.name.equals(item.name)) {
                        valueSet.remove(selectedValueItem);
                        if (valueSet.size() == 0) {
                            selectedFilterMap.remove(filterMasterKey);
                        }
                        add = false;
                        break;
                    }
                }
                if (add) {
                    valueSet.add(item);
                    selectedFilterMap.put(filterMasterKey, valueSet);
                }
            } else {
                Set<FilterValue> valueSet = new HashSet<>();
                valueSet.add(item);
                selectedFilterMap.put(filterMasterKey, valueSet);
            }
            onApplyFilter(isPublicDeal, selectedFilterMap);
        }

    }


    private void clearAllData() {
        PAGE_COUNT = 1;
        if (buyDealAdapter != null) {
            buyDealAdapter.clearAdapter();
            buyDealAdapter.isLoading = true;
        }

        if (buyDealList != null) {
            buyDealList.clear();
            buyDealList.add(new DealItem());
        }

        setEndLessRecyclerView();
    }


    private void resetFilterList() {

        for (int i = 0; i < filterMasterList.size(); i++) {
            if (filterMasterList.get(i).values != null && filterMasterList.get(i).values.size() > 0) {
                for (int j = 0; j < filterMasterList.get(i).values.size(); j++) {
                    filterMasterList.get(i).values.get(j).isSelected = false;
                }
            }
        }

    }


    public void updateAndPopulateFilterList() {

        // Resetting the filter List
        resetFilterList();
        // Updating the list and populating params
        if (selectedFilterMap != null && selectedFilterMap.size() > 0 &&  filterMasterList!=null && filterMasterList.size()>0) {

            for (int i = 0; i < filterMasterList.size(); i++) {

                String filterKeyName = filterMasterList.get(i).code;

                if (selectedFilterMap.containsKey(filterKeyName)) {

                    Set<FilterValue> valueSet = new HashSet<>();
                    valueSet = selectedFilterMap.get(filterKeyName);

                    for (FilterValue filterValue : valueSet) {

                        for (int j = 0; j < filterMasterList.get(i).values.size(); j++) {

                            if (filterMasterList.get(i).values.get(j).name.equals(filterValue.name)) {
                                filterMasterList.get(i).values.get(j).isSelected = true;
                            }

                        }

                    }

                }
            }

        }


    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        clearAllData();
        loadBuyDeal();
    }


    public void setOnFilterChangeNotifier(DealFilterFragment.OnFilterAppliedListener onFilterAppliedListener) {
        this.listener = onFilterAppliedListener;
    }

    private void openDealFilter() {
        DealFilterFragment dealFilterFragment = DealFilterFragment.newInstance(new Gson().toJson(filterMasterList), new Gson().toJson(selectedFilterMap), isPublicDeal, this);
        addToBackStack((BaseActivity) getActivity(), dealFilterFragment, dealFilterFragment.getTag());

    }


    @Override
    public void onApplyFilter(Integer isPublicDeal, Map<String, Set<FilterValue>> selectedMap) {
        this.isPublicDeal = isPublicDeal;
        selectedFilterMap = selectedMap;
        updateAndPopulateFilterList();
        clearAllData();
        loadBuyDeal();

    }

    @Override
    public void onClearFilterApplied() {
        isPublicDeal = 1;
        resetFilterList();
        if (selectedFilterMap != null && selectedFilterMap.size() > 0) {
            selectedFilterMap.clear();
        }
        clearAllData();
        loadBuyDeal();
    }


    protected Map<String, String> getRequestParams(Map<String, Set<FilterValue>> selectedFilterMap) {

        Map<String, String> paramMap = new HashMap<>();
        if (selectedFilterMap != null) {
            for (Map.Entry<String, Set<FilterValue>> entry : selectedFilterMap.entrySet()) {
                String key = entry.getKey();
                if (key.equals(DealConstants.CATEGORY_FILTER_PARAM_KEY)) {
                    selectedCategorySet.clear();
                }

                Set<FilterValue> filterValueList = entry.getValue();
                List<String> selectedList = new ArrayList<>();
                if (filterValueList != null && filterValueList.size() > 0) {
                    for (FilterValue filterVal : filterValueList) {
                        selectedList.add(filterVal.id + "");
                        if (key.equals(DealConstants.CATEGORY_FILTER_PARAM_KEY)) {
                            selectedCategorySet.add(filterVal.name);
                        }

                    }
                }
                paramMap.put(key, CommonUtility.convertListToCommaString(selectedList));
                CustomLogger.d("key::" + key + "value :: " + paramMap.get(key));
            }
        }

        showSupplierPromotion = false;
        if (paramMap.size() == 1 && paramMap.containsKey(DealConstants.SUPPLIER_FILTER_PARAM_KEY)) {
            String supplierId = paramMap.get(DealConstants.SUPPLIER_FILTER_PARAM_KEY);
            String supplierFilterIdStr = supplierFilterId + "";
            if (supplierFilterIdStr.equals(supplierId)) {
                showSupplierPromotion = true;
            }
        }

        CustomLogger.d("paramMAp Size ::" + paramMap.size());
        return paramMap;
    }
}
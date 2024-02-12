package com.partseazy.android.ui.fragments.catalogue;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.ui.adapters.home.HorizontalViewAllProductAdapter;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebParamsConstants;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.catalogue.CatalogueProductAdapter;
import com.partseazy.android.ui.adapters.catalogue.CatalogueSpinnerAdapter;
import com.partseazy.android.ui.model.catalogue.Bucket;
import com.partseazy.android.ui.model.catalogue.CatalogueResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.fav.FavResultData;
import com.partseazy.android.ui.model.home.category.Param;
import com.partseazy.android.ui.model.home.category.ProductData;
import com.partseazy.android.ui.model.home.usershop.L1CategoryData;
import com.partseazy.android.ui.model.home.usershop.L2CategoryData;
import com.partseazy.android.ui.model.home.usershop.L3CategoryData;
import com.partseazy.android.ui.widget.EndlessRecyclerOnScrollListener;
import com.partseazy.android.ui.widget.NavigationDrawer;
import com.partseazy.android.utility.KeyPadUtility;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by naveen on 9/1/17.
 */

public class CatalogueViewAllFragment extends CatalogueSearchBaseFragment implements View.OnClickListener,
        CatalogueFilterFragment.OnFilterAppliedListener, AdapterView.OnItemSelectedListener,HorizontalViewAllProductAdapter.FavouriteListener, HorizontalViewAllProductAdapter.OnAddToCartReorder,
        View.OnTouchListener  {

    @BindView(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.drawerRecylerView)
    protected RecyclerView drawerRecylerView;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.spinnerNav)
    protected Spinner spinnerNav;
    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.sortLL)
    protected LinearLayout sortLL;
    @BindView(R.id.filterLL)
    protected LinearLayout filterLL;

    @BindView(R.id.filterLYT)
    protected LinearLayout filterLYT;

    @BindView(R.id.catalogeContainerView)
    protected View catalogueContainer;

    @BindView(R.id.filterTopWhiteView)
    protected View filterTopWhiteView;
    @BindView(R.id.loadmore_progress)
    protected ProgressBar loadMoreProgress;


    @BindView(R.id.cartValueTV)
    protected TextView cartValueTV;


    @BindView(R.id.cartAmountTV)
    protected TextView cartAmountTV;

    @BindView(R.id.fabCartIcon)
    protected FloatingActionButton homefabCartIcon;

    private boolean isBySearch;
    private boolean isByFilter = false;

    private int adapterIndex;

    protected int PAGE_COUNT = 1;


    private CatalogueProductAdapter catalogueProductAdapter;
    private GridLayoutManager layoutManager;
    //    private List<Product> productList;
    private CatalogueResult catalogueResult;
    protected Map<String, Set<Bucket>> selectedFacetBucketMap;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private Map<String, Object> filterParams;

    HorizontalViewAllProductAdapter horizontalProductAdapter;
    protected String selectedBucketMapJson;
    private String facetDataJson;

    //sorting keyValue
    private String sortCode;
    private int categoryId;
    private String searchQuery;
    private List<ProductData> productDataList;
    private List<ProductData> productDataListTemp;
    private boolean userSelected = false;
    public List<Param> requestParams = null;
    public static final String SORT_DAILOG = "sort_dailog";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_REQUEST_PARAMS = "params_map";
    private CatalogueSpinnerAdapter catalogueSpinnerAdapter;
    public static final String IS_BY_SEARCH = "is_by_search";
    private int positionToNotify;

    public static final String SEARCH_QUERY = "SEARCH_QUERY";
    public static final String PRODUCT_LIST = "PRODUCT_LIST";


    public static CatalogueViewAllFragment newInstance(int categoryId) {
        Bundle bundle = new Bundle();
        bundle.putInt(CATEGORY_ID, categoryId);
        CatalogueViewAllFragment fragment = new CatalogueViewAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CatalogueViewAllFragment newInstance(int categoryId, String params) {
        Bundle bundle = new Bundle();
        CustomLogger.d("The params are " + params);
        bundle.putInt(CATEGORY_ID, categoryId);
        bundle.putString(CATEGORY_REQUEST_PARAMS, params);
        CatalogueViewAllFragment fragment = new CatalogueViewAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CatalogueViewAllFragment newInstance(int categoryId, List<ProductData> productDataList) {
        Bundle bundle = new Bundle();
        bundle.putInt(CATEGORY_ID, categoryId);
        bundle.putSerializable(PRODUCT_LIST, (Serializable) productDataList);
        CatalogueViewAllFragment fragment = new CatalogueViewAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public static CatalogueViewAllFragment newInstance(String query, boolean isBySearch) {
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY, query);
        bundle.putBoolean(IS_BY_SEARCH, isBySearch);
        CatalogueViewAllFragment fragment = new CatalogueViewAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CatalogueViewAllFragment newInstance(String query, String params, boolean isBySearch) {
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY, query);
        CustomLogger.d("The params are " + params);
        bundle.putString(CATEGORY_REQUEST_PARAMS, params);
        bundle.putBoolean(IS_BY_SEARCH, isBySearch);
        CatalogueViewAllFragment fragment = new CatalogueViewAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        productDataList = new ArrayList<>();
        productDataListTemp = new ArrayList<>();
        categoryId = getArguments().getInt(CATEGORY_ID);
        isBySearch = getArguments().getBoolean(IS_BY_SEARCH, false);
        searchQuery = getArguments().getString(SEARCH_QUERY);
        productDataListTemp = (List<ProductData>) getArguments().getSerializable(PRODUCT_LIST);
        productDataList.addAll(productDataListTemp);
        String requestParamsJson = getArguments().getString(CATEGORY_REQUEST_PARAMS);
        Type listType = new TypeToken<List<Param>>() {
        }.getType();
        if (requestParamsJson != null && !requestParamsJson.equals("")) {
            requestParams = new Gson().fromJson(requestParamsJson, listType);
        } else {
            requestParams = new ArrayList<>();
        }
        selectedFacetBucketMap = new HashMap<>();


    }


    public static String getTagName() {
        return CatalogueViewAllFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_catalogue;
    }

    @Override
    protected String getFragmentTitle() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCatalogueAdapter(false);
        updateCartIconOnHome();
      //  filterLYT.setVisibility(View.GONE);
        if (catalogueResult == null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            });
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (!isBySearch) {
            context.getSupportActionBar().setDisplayShowTitleEnabled(false);
            initCustomSpinner();
        } else {
            spinnerNav.setVisibility(View.GONE);
            toolbar.setTitle(searchQuery);
        }

        spinnerNav.setOnTouchListener(this);
        spinnerNav.setOnItemSelectedListener(this);

        setUpNavigationDrawer();
        hideKeyBoard(view);
        sortLL.setOnClickListener(this);
        filterLL.setOnClickListener(this);
        homefabCartIcon.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyPadUtility.hideSoftKeypad(getActivity());
        filterLL.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

    }

    @Override
    public void onPause() {
        super.onPause();
        KeyPadUtility.hideSoftKeypad(getActivity());
        filterLL.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterLL.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

    }


    protected void setUpNavigationDrawer() {
        Type listType = new TypeToken<List<L1CategoryData>>() {
        }.getType();
        List<L1CategoryData> l1CategoryDataList = new Gson().fromJson(DataStore.getL1CategoryList(getContext()), listType);
        NavigationDrawer navigationDrawer = new NavigationDrawer(getContext(), l1CategoryDataList, drawerLayout, drawerRecylerView, toolbar);
        navigationDrawer.setNavigationDrawer();
    }

    private void initCustomSpinner() {

        final List<L3CategoryData> categoryList = getSpinnerList(categoryId);
        if (categoryList.size() > 1) {
            spinnerNav.setVisibility(View.VISIBLE);
        } else {
            if (categoryList != null && categoryList.size() == 1) {
                toolbar.setTitle(categoryList.get(0).name);
                spinnerNav.setVisibility(View.GONE);
            } else {
                showError();
            }
        }
        catalogueSpinnerAdapter = new CatalogueSpinnerAdapter(getContext(), categoryList);
        spinnerNav.setAdapter(catalogueSpinnerAdapter);


    }


    private void setCatalogueAdapter(boolean isFromFilter) {

        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        if (productDataList.size() == 0) {
            if (!isFromFilter)
                setEndLessRecycler();
            else
                setEndLessRecyclerForFilter();
        }
        if (catalogueProductAdapter == null) {
            catalogueProductAdapter = new CatalogueProductAdapter(getContext(), categoryId);

        }
        mRecyclerView.setAdapter(catalogueProductAdapter);


    }

    private void loadData() {
        if (productDataList.size() == 0)
            showProgressBar();

        if (!isByFilter) {
            if (!isBySearch)
                loadCatalogueData();
            else
                loadSearchData();
        }
    }

    private void loadCatalogueData() {
//        showProgressDialog(getString(R.string.loading), false);
  /**
   *
   * Added code for view all click
   *
   * */
        if (productDataList.size() > 0 ) {

            horizontalProductAdapter = new HorizontalViewAllProductAdapter(getContext(), (List<ProductData>) productDataList, this, this,0,false);
            mRecyclerView.setAdapter(horizontalProductAdapter);

            filterLYT.setVisibility(View.GONE);
        }
        else {
            Map requestParamsMap = WebServicePostParams.getPageParams(PAGE_COUNT);
            Map<String, Object> filterParams = getRequestParams(selectedFacetBucketMap, requestParams);
            requestParamsMap.putAll(filterParams);
            getNetworkManager().PostRequest(RequestIdentifier.PRODUCT_BROWSE.ordinal(),
                    WebServiceConstants.POST_BROWSE + categoryId, requestParamsMap, null, this, this, true);
        }
    }


    private void loadSearchData() {
        Map requestParamsMap = WebServicePostParams.getSearchParams(searchQuery, PAGE_COUNT);
        Map<String, Object> filterParams = getRequestParams(selectedFacetBucketMap, requestParams);
        requestParamsMap.putAll(filterParams);
        getNetworkManager().PostRequest(RequestIdentifier.SEARCH_ID.ordinal(),
                WebServiceConstants.POST_SEARCH, requestParamsMap, null, this, this, true);

    }


    private List<L3CategoryData> getSpinnerList(int categoryId) {

        List<L3CategoryData> l3CategoryDataList = new ArrayList<>();
        L3CategoryData selectedL3Data = null;
        String categoryData = DataStore.getL2CategoryList(getContext());

        if (categoryData == null)
            return l3CategoryDataList;

        Type listType = new TypeToken<List<L2CategoryData>>() {
        }.getType();

        List<L2CategoryData> l2CategoryDataList = new Gson().fromJson(categoryData, listType);

        for (L2CategoryData l2CatData : l2CategoryDataList) {

            for (L3CategoryData l3CategoryData : l2CatData.l3CategoryDataList) {

                if (categoryId == l3CategoryData.id) {
                    selectedL3Data = l3CategoryData;
                    l3CategoryDataList.addAll(l2CatData.l3CategoryDataList);
                    break;
                }
            }

            CustomLogger.d("categoryDAta " + l2CatData.name);
        }

        if (l3CategoryDataList != null && l3CategoryDataList.size() > 0) {
            if (selectedL3Data != null) {
                if (l3CategoryDataList.contains(selectedL3Data)) {
                    int index = l3CategoryDataList.indexOf(selectedL3Data);
                    l3CategoryDataList.remove(index);
                    l3CategoryDataList.add(0, selectedL3Data);
                }
            }
        }
        return l3CategoryDataList;

    }

    private void setEndLessRecycler() {


        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                hideProgressBar();

                loadMoreProgress.setVisibility(View.VISIBLE);

                CustomLogger.d("The current page is " + current_page
                        + " Total available Items " + catalogueResult.meta.noOfResult
                        + " Current Loaded Items " + (catalogueResult.meta.page * catalogueResult.meta.rows));

                if ((catalogueResult.meta.page * catalogueResult.meta.rows)
                        < Integer.valueOf(catalogueResult.meta.noOfResult)) {

                    PAGE_COUNT = current_page;
                    CustomLogger.d("PAGE COUNT" + PAGE_COUNT);

                    if (!isBySearch)
                        loadCatalogueData();
                    else
                        loadSearchData();
                } else {
                    loadMoreProgress.setVisibility(View.GONE);

                }
            }
        };
        mRecyclerView.setOnScrollListener(endlessRecyclerOnScrollListener);
    }

    private void setEndLessRecyclerForFilter() {


        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                hideProgressBar();

                loadMoreProgress.setVisibility(View.VISIBLE);

                CustomLogger.d("The current page is " + current_page
                        + " Total available Items " + catalogueResult.meta.noOfResult
                        + " Current Loaded Items " + (catalogueResult.meta.page * catalogueResult.meta.rows));

                if ((catalogueResult.meta.page * catalogueResult.meta.rows)
                        < Integer.valueOf(catalogueResult.meta.noOfResult)) {

                    PAGE_COUNT = current_page;
                    CustomLogger.d("PAGE COUNT" + PAGE_COUNT);
                    if (filterParams != null) {
                        applyFilterCall(categoryId, filterParams);
                    }
                } else {
                    loadMoreProgress.setVisibility(View.GONE);

                }
            }
        };
        mRecyclerView.setOnScrollListener(endlessRecyclerOnScrollListener);
    }


    private void applyFilterCall(int categoryId, Map<String, Object> filterParams) {
        if (isBySearch) {
            applySearchFilter();
        } else {
            if (getActivity() != null && isAdded()) {
//                showProgressDialog(getString(R.string.loading), false);
                showProgressBar();
            }
            Map requestParams = WebServicePostParams.getPageParams(PAGE_COUNT);
            requestParams.putAll(filterParams);
            if (sortCode != null) {
                requestParams.put(WebParamsConstants.SORT_FILTER, sortCode);
            }
            getNetworkManager().PostRequest(RequestIdentifier.FILTER_APPLY_REQUEST_ID.ordinal(),
                    WebServiceConstants.POST_BROWSE + categoryId, requestParams, null, this, this, true);
        }
    }

    private void applySearchFilter() {
        if (getActivity() != null && isAdded()) {
//            showProgressDialog(getString(R.string.loading), false);
            showProgressBar();
        }
        Map requestParamsMap = WebServicePostParams.getSearchParams(searchQuery, PAGE_COUNT);
        Map<String, Object> filterParams = getRequestParams(selectedFacetBucketMap, requestParams);
        requestParamsMap.putAll(filterParams);
        if (sortCode != null) {
            requestParamsMap.put(WebParamsConstants.SORT_FILTER, sortCode);
        }
        getNetworkManager().PostRequest(RequestIdentifier.SEARCH_FILTER_APPLY_ID.ordinal(),
                WebServiceConstants.POST_SEARCH, requestParamsMap, null, this, this, true);
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, final JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.PRODUCT_BROWSE.ordinal() ||
                request.getIdentifier() == RequestIdentifier.SEARCH_ID.ordinal()) {
            hideProgressBar();

            getGsonHelper().parse(responseObject.toString(), CatalogueResult.class, new OnGsonParseCompleteListener<CatalogueResult>() {
                        @Override
                        public void onParseComplete(CatalogueResult data) {
                            loadMoreProgress.setVisibility(View.GONE);

                            hideProgressDialog();
                            catalogueResult = data;
                            facetDataJson = getSelectedFacetsList(selectedFacetBucketMap, data.facets);
                            if (data.products != null && data.products.size() > 0) {
                                if (isBySearch && data.meta.id != null && data.meta.id.length() > 0)
                                    toolbar.setTitle(data.meta.id);
                                catalogueProductAdapter.updateProductList(catalogueResult.meta.page, data.products);
                                filterLYT.setVisibility(View.VISIBLE);
                                hideNoResult(catalogueContainer);
                            } else {
                                if (catalogueResult.meta.page > 1)
                                    return;
                                if (getActivity() != null && isAdded()) {
                                    if (isBySearch) {
                                        showNoResult(getString(R.string.no_result_found_for, searchQuery), catalogueContainer, NORESULTBUTTON.SEARCH, categoryId, searchQuery, isBySearch);
                                    } else {
                                        showNoResult(getString(R.string.no_result_found), catalogueContainer);
                                        //  showNoResult(getString(R.string.no_result_found), catalogueContainer,NORESULTBUTTON.CATALOGUE,l2Id);
                                    }

                                    filterLYT.setVisibility(View.GONE);
                                }
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            hideProgressBar();
                            showError();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );

        }
         else if (request.getIdentifier() == RequestIdentifier.ADD_FAV_PRODUCT_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {
                            hideProgressDialog();
                            CustomLogger.d("Added to Favourite Item ");
                            if (data.result != null) {
                                FavouriteUtility.updateFavMapOnAddRemove(data);
                                if (horizontalProductAdapter != null)
                                    horizontalProductAdapter.updateHorizontalAdapter(positionToNotify);

                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        } else if (request.getIdentifier() == RequestIdentifier.REMOVE_FAV_PRODUCT_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {
                            hideProgressDialog();

                            CustomLogger.d("Remmve  Favourite Item ");
                            if (data.result != null) {
                                FavouriteUtility.updateFavMapOnAddRemove(data);
                                if (horizontalProductAdapter != null)
                                    horizontalProductAdapter.updateHorizontalAdapter(positionToNotify);

                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();

                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        if (request.getIdentifier() == RequestIdentifier.FILTER_APPLY_REQUEST_ID.ordinal() ||
                request.getIdentifier() == RequestIdentifier.SEARCH_FILTER_APPLY_ID.ordinal()) {
            hideProgressDialog();
            hideProgressBar();

            getGsonHelper().parse(responseObject.toString(), CatalogueResult.class, new OnGsonParseCompleteListener<CatalogueResult>() {
                        @Override
                        public void onParseComplete(final CatalogueResult data) {
                            hideNoResult();

                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {


                                    catalogueResult = data;

                                    facetDataJson = getSelectedFacetsList(selectedFacetBucketMap, data.facets);

                                    if (data.products != null && data.products.size() > 0) {
                                        filterLYT.setVisibility(View.VISIBLE);
                                        catalogueProductAdapter.updateProductList(data.meta.page, data.products);
                                    } else {
                                        if (getActivity() != null && isAdded()) {
                                            filterLYT.setVisibility(View.GONE);
                                            if (isBySearch) {
                                                showNoResult(getString(R.string.no_result_found_filter), catalogueContainer, NORESULTBUTTON.CATALOGUE_FILTER, 0, searchQuery, isBySearch);
                                            } else {
                                                showNoResult(getString(R.string.no_result_found_filter), catalogueContainer, NORESULTBUTTON.CATALOGUE_FILTER, categoryId, "", isBySearch);
                                            }
                                        }
                                    }
                                }
                            });


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
        showError();
        if (request.getIdentifier() == RequestIdentifier.PRODUCT_BROWSE.ordinal()) {
            filterLYT.setVisibility(View.GONE);
            showError();
        }
        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        hideFavMenu();
        hideDealFilterMenu();
        hideCartMenu();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_cart:
                launchCart();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {
        hideKeyBoard(view);
        if (view.getId() == sortLL.getId()) {
            //  filterLL.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_70_opacity));
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    CatalogueSortDailog catalogueSortDailog = CatalogueSortDailog.newInstance(isBySearch, sortCode);
                    catalogueSortDailog.show(getActivity().getFragmentManager(), "dialog");

                }
            });
        }
        if (view.getId() == homefabCartIcon.getId()) {
            launchCart();
        }


        if (view.getId() == filterLL.getId()) {
            selectedBucketMapJson = new Gson().toJson(selectedFacetBucketMap);
            CatalogueFilterFragment fragment;
            if (isBySearch) {
                fragment = CatalogueFilterFragment.newInstance(facetDataJson, selectedBucketMapJson, searchQuery, this);
            } else {
                fragment = CatalogueFilterFragment.newInstance(facetDataJson, selectedBucketMapJson, categoryId, this);
            }

            BaseFragment.addToBackStack((BaseActivity) getActivity(), fragment, fragment.getTag());
        }

    }


    @Override
    public void onEvent(EventObject eventObject) {

//        if (eventObject.id == EventConstant.APPLY_FILTER) {
//            filterParams = (Map<String, Object>) eventObject.objects[0];
//            selectedFacetBucketMap = (Map<String, Set<Bucket>>) eventObject.objects[1];
//            // clearDataForFilter();
//            applyFilterCall(l2Id, filterParams);
//
//        }
//
//
//        if (eventObject.id == EventConstant.CLEAR_ALL_FILTER) {
//            selectedFacetBucketMap = (Map<String, Set<Bucket>>) eventObject.objects[0];
//            CustomLogger.d("Selected MAp " + selectedFacetBucketMap.size());
//
//        }

        if (eventObject.id == EventConstant.SELECT_SORT_FILTER_ID) {
            sortCode = (String) eventObject.objects[0];
            filterParams = getRequestParams(selectedFacetBucketMap, requestParams);
            //  clearDataForFilter();
            filterLL.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            clearDataForFilter();
            applyFilterCall(categoryId, filterParams);
        }

        if (eventObject.id == EventConstant.CATALOGUE_ADD_TO_FAVOURITE) {
            int productMasterId = (int) eventObject.objects[0];
            positionToNotify = (int) eventObject.objects[1];
            FavouriteUtility.callAddProductToFavourite(this, productMasterId);
        }

        if (eventObject.id == EventConstant.CATALOGUE_REMOVE_FAVOURITE) {
            int productMasterId = (int) eventObject.objects[0];
            positionToNotify = (int) eventObject.objects[1];
            FavouriteUtility.callRemovefavItemRequest(this, productMasterId);
        }


    }

    private void clearAllData() {
        PAGE_COUNT = 1;
        if (productDataList.size() == 0) {
            setEndLessRecycler();
        }
        if (catalogueProductAdapter != null) {
            catalogueProductAdapter.clearAdapter();
        }


        if (catalogueResult != null)
            catalogueResult = null;
        selectedFacetBucketMap.clear();
        if (requestParams != null)
            requestParams.clear();
    }

    private void clearDataForFilter() {
        PAGE_COUNT = 1;
        isByFilter = true;
        setEndLessRecyclerForFilter();


        if (catalogueProductAdapter != null) {
            catalogueProductAdapter.clearAdapter();
        }
    }

    @Override
    public void onFilterApplied(Map<String, Object> filterMap, final Map<String, Set<Bucket>> selectedFacetBucketMap) {
        filterParams = filterMap;
        clearDataForFilter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CatalogueViewAllFragment.this.selectedFacetBucketMap = selectedFacetBucketMap;
                applyFilterCall(categoryId, filterParams);

            }
        }, 100);
    }

    @Override
    public void onClearDataApplied(final Map<String, Set<Bucket>> selectedFacetBucketMap) {

        CustomLogger.d("Selected MAp " + selectedFacetBucketMap.size());
        clearAllData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isBySearch) {
                    showProgressBar();
                    loadSearchData();
                } else {
                    showProgressBar();
                    loadCatalogueData();
                }

            }
        }, 100);
    }

    @Override
    protected void retryFailedRequest(int identifier, Request<?> oldRequest, VolleyError error) {
        if (oldRequest.getIdentifier() == RequestIdentifier.PRODUCT_BROWSE.ordinal()) {
            if (getActivity() != null && isAdded()) {
                showError(getString(R.string.wait_trying_to_reconnect), MESSAGETYPE.SNACK_BAR);
                showProgressBar();
            }
            loadCatalogueData();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelected = true;
        productDataList.clear();
        spinnerNav.setOnItemSelectedListener(this);
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

        L3CategoryData selectedCategory = (L3CategoryData) parent.getItemAtPosition(position);
        if (userSelected) {
            userSelected = false;
            sortCode = null;
            categoryId = selectedCategory.id;
            clearAllData();
            if (getActivity() != null && isAdded()) {
                showProgressBar();
                KeyPadUtility.hideSoftKeypad(getActivity());
            }
            productDataList.clear();
            setCatalogueAdapter(false);
            loadCatalogueData();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    catalogueSpinnerAdapter.updateSelectedItem(position);

                }
            });

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void addFav(int id, int position) {

        positionToNotify = position;
        this.adapterIndex = adapterIndex;
        FavouriteUtility.callAddProductToFavourite(this, id);
    }

    @Override
    public void removeFav(int id, int position) {

        positionToNotify = position;
        this.adapterIndex = adapterIndex;
        FavouriteUtility.callRemovefavItemRequest(this, id);
    }

    @Override
    public void OnAddToCartReorder(int selectedProductSKUIds, int selectedProductQuantity) {

    }

    private void updateCartIconOnHome() {

        if (cartValueTV == null)
            return;

        int cartCount = PartsEazyApplication.getInstance().getCartCount();
        int cartAmount = PartsEazyApplication.getInstance().getCartAmount();

        /* Hide cart count icon if cart count is 0 */
        if (cartCount > 0) {
            cartValueTV.setVisibility(View.VISIBLE);
            if (cartCount == 1 )
                cartValueTV.setText(String.valueOf(cartCount) + " " + getString(R.string.item_in_cart));
            else cartValueTV.setText(String.valueOf(cartCount) + " " + getString(R.string.items_in_cart));
        } else {
            cartValueTV.setVisibility(View.GONE);
        }

        /*Show cart amount*/
        if (cartAmount > 0)
        {
            cartAmountTV.setVisibility(View.VISIBLE);
            cartAmountTV.setText(getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(cartAmount)));
        }
        else
            cartAmountTV.setVisibility(View.GONE);



    }
}



package com.partseazy.android.ui.fragments.catalogue;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.ui.adapters.catalogue.CatalogueSpinnerAdapter;
import com.partseazy.android.ui.adapters.home.HorizontalProductAdapter;
import com.partseazy.android.ui.model.catalogue.Bucket;
import com.partseazy.android.ui.model.catalogue.CatalogueResult;
import com.partseazy.android.ui.model.error.APIError;
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

public class SingleCatalogueFragment extends BaseFragment implements View.OnClickListener, HorizontalProductAdapter.FavouriteListener,HorizontalProductAdapter.OnAddToCartReorder,
        AdapterView.OnItemSelectedListener,
        View.OnTouchListener {

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

    @BindView(R.id.catalogeContainerView)
    protected View catalogueContainer;

    private boolean isBySearch;

    protected int PAGE_COUNT = 1;

    private HorizontalProductAdapter catalogueProductAdapter;
    private GridLayoutManager layoutManager;
    //    private List<Product> productList;
    private CatalogueResult catalogueResult;
    protected Map<String, Set<Bucket>> selectedFacetBucketMap;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private Map<String, Object> filterParams;


    protected String selectedBucketMapJson;
    private String facetDataJson;

    //sorting keyValue
    private String sortCode;
    private int categoryId;
    private List<ProductData> searchQuery;

    private boolean userSelected = false;
    public List<Param> requestParams = null;
    public static final String SORT_DAILOG = "sort_dailog";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_REQUEST_PARAMS = "params_map";
    private CatalogueSpinnerAdapter catalogueSpinnerAdapter;
    public static final String IS_BY_SEARCH = "is_by_search";
    private int positionToNotify;

    public static final String SEARCH_QUERY = "SEARCH_QUERY";


    public static SingleCatalogueFragment newInstance(List<ProductData> query, boolean isBySearch) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEARCH_QUERY, (Serializable) query);
        bundle.putBoolean(IS_BY_SEARCH, isBySearch);
        SingleCatalogueFragment fragment = new SingleCatalogueFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        searchQuery = new ArrayList<>();
        isBySearch = getArguments().getBoolean(IS_BY_SEARCH, false);
        searchQuery = (List<ProductData>) getArguments().getSerializable(SEARCH_QUERY);

        selectedFacetBucketMap = new HashMap<>();


    }


    public static String getTagName() {
        return SingleCatalogueFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_single_catalogue;
    }

    @Override
    protected String getFragmentTitle() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCatalogueAdapter(false);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (!isBySearch) {
            context.getSupportActionBar().setDisplayShowTitleEnabled(false);
            initCustomSpinner();
        } else {
            spinnerNav.setVisibility(View.GONE);
            toolbar.setTitle("");
        }

        spinnerNav.setOnTouchListener(this);
        spinnerNav.setOnItemSelectedListener(this);

        setUpNavigationDrawer();
        hideKeyBoard(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyPadUtility.hideSoftKeypad(getActivity());

    }

    @Override
    public void onPause() {
        super.onPause();
        KeyPadUtility.hideSoftKeypad(getActivity());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
               // showError();
            }
        }
        catalogueSpinnerAdapter = new CatalogueSpinnerAdapter(getContext(), categoryList);
        spinnerNav.setAdapter(catalogueSpinnerAdapter);
    }


    private void setCatalogueAdapter(boolean isFromFilter) {

        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        if (catalogueProductAdapter == null) {
            catalogueProductAdapter = new HorizontalProductAdapter(getContext(), (List<ProductData>) searchQuery, this, this,0,false, "");

        }
        mRecyclerView.setAdapter(catalogueProductAdapter);


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



    @Override
    public boolean handleResponse(Request<JSONObject> request, final JSONObject responseObject, Response<JSONObject> response) {

        return true;
    }



    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        showError();

        return true;
    }


    @Override
    protected void retryFailedRequest(int identifier, Request<?> oldRequest, VolleyError error) {

    }
        @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        hideFavMenu();
        hideDealFilterMenu();

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


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelected = true;
        spinnerNav.setOnItemSelectedListener(this);
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void addFav(int id, int position, int adapterIndex) {

    }

    @Override
    public void removeFav(int id, int position, int adapterIndex) {

    }

    @Override
    public void OnAddToCartReorder(int selectedProductSKUIds) {

    }
}

package com.partseazy.android.ui.fragments.deals;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.partseazy.android.ui.adapters.base.BaseViewPagerAdapter;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealFragment;
import com.partseazy.android.ui.fragments.deals.buy_deal.DealFilterFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealFragment;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.model.credit_facility.PendingCreditResult;
import com.partseazy.android.ui.model.deal.filters.FilterValue;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.home.usershop.UserShopResult;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by naveen on 26/4/17.
 */

public class DealHomeFragment extends BaseFragment implements View.OnClickListener, DealFilterFragment.OnFilterAppliedListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    @BindView(R.id.fab)
    protected FloatingActionButton fabMall;

    TextView filterValueTV;


    public static final String LAUNCH_SELL = "launch_sell";
    public static final String SUPPLIER_FILTER_PARAM_DEEP_LINK = "supplier_filter_param";
    public static final String CATEGORY_FILTER_PARAM_DEEP_LINK = "category_filter_param";

    private BaseViewPagerAdapter pagerAdapter;
    private boolean launchSell = false;
    private String supplierFilterId;
    private String categoryFilterId;
    private DrawerLayout mDrawerLayout;


    public static DealHomeFragment newInstance() {
        Bundle bundle = new Bundle();
        DealHomeFragment fragment = new DealHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DealHomeFragment newInstance(boolean launchSell) {
        Bundle bundle = new Bundle();
        DealHomeFragment fragment = new DealHomeFragment();
        bundle.putBoolean(LAUNCH_SELL, launchSell);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DealHomeFragment newInstance(String filterId, String filterType) {
        Bundle bundle = new Bundle();
        DealHomeFragment fragment = new DealHomeFragment();
        if (SUPPLIER_FILTER_PARAM_DEEP_LINK.equals(filterType)) {
            bundle.putString(SUPPLIER_FILTER_PARAM_DEEP_LINK, filterId);
        } else if (CATEGORY_FILTER_PARAM_DEEP_LINK.equals(filterType)) {
            bundle.putString(CATEGORY_FILTER_PARAM_DEEP_LINK, filterId);
        }
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        launchSell = getArguments().getBoolean(LAUNCH_SELL, launchSell);
        supplierFilterId = getArguments().getString(SUPPLIER_FILTER_PARAM_DEEP_LINK, supplierFilterId);
        categoryFilterId = getArguments().getString(CATEGORY_FILTER_PARAM_DEEP_LINK, categoryFilterId);

        CustomLogger.d("step 6 " + supplierFilterId);
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                initOtherRequiredCalls();

            }
        });


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_deal_viewpager;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.deals);
    }


    public static String getTagName() {
        return DealHomeFragment.class.getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String l2CategoryName = DataStore.getL2CategoryList(context);

        if (l2CategoryName != null && !l2CategoryName.equals("")) {
            setUpNavigationDrawer();
            setupViewPager(viewPager);
        } else {
            loadUserL123Category();
        }

    }

    @Override
    protected boolean isNavigationDrawerNeeded() {
        return true;
    }

    @Override
    protected DrawerLayout getNavigationDrawerLayout() {
        mDrawerLayout = (DrawerLayout) getView().findViewById(R.id.drawerLayout);
        return mDrawerLayout;
    }

    @Override
    protected RecyclerView getNavigationDrawerRecyclerView() {
        return (RecyclerView) getView().findViewById(R.id.drawerRecylerView);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
//        setUpNavigationDrawer();
        setupViewPager(viewPager);
        fabMall.setOnClickListener(this);

    }

    private void loadUserL123Category() {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.USER_L123_CATEGORY_REQUEST_ID.ordinal(),
                WebServiceConstants.GET_USER_L123_LIST, null, params, this, this, true);
    }

    private void initOtherRequiredCalls() {
        callUserCreditDetails();
    }

    protected void callUserCreditDetails() {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_USER_PENDING_CREDIT.ordinal(),
                WebServiceConstants.PENDING_CREDIT, null, params, this, this, false);
    }


    private void setupViewPager(ViewPager viewPager) {
        if (getActivity() != null && isAdded()) {
            pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(), context);
            if (supplierFilterId != null && !supplierFilterId.equals("")) {
                pagerAdapter.addFragment(BuyDealFragment.newInstance(supplierFilterId, SUPPLIER_FILTER_PARAM_DEEP_LINK), getString(R.string.title_buy));
            } else if (categoryFilterId != null && !categoryFilterId.equals("")) {
                pagerAdapter.addFragment(BuyDealFragment.newInstance(categoryFilterId, CATEGORY_FILTER_PARAM_DEEP_LINK), "Buy");
            } else {
                pagerAdapter.addFragment(BuyDealFragment.newInstance(), getString(R.string.title_buy));
            }
            pagerAdapter.addFragment(SellDealFragment.newInstance(), getString(R.string.title_sell));
            viewPager.setAdapter(pagerAdapter);
            if (launchSell) {
                viewPager.setCurrentItem(1);
            } else {
                viewPager.setCurrentItem(0);
            }

        /*..after you set the adapter you have to check if view is laid out, i did a custom method for it
        * */
            if (ViewCompat.isLaidOut(tabLayout)) {
                setViewPagerListener();
            } else {
                tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        setViewPagerListener();
//                        tabLayout.notify();
                        tabLayout.removeOnLayoutChangeListener(this);
                    }
                });
            }
        }
    }


    private void setViewPagerListener() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                handleFabVisibility(tab.getPosition());
            }
        });
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.USER_L123_CATEGORY_REQUEST_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();


        }

        if (request.getIdentifier() == RequestIdentifier.GET_USER_PENDING_CREDIT.ordinal()) {
            DataStore.setUserCredit(context, "");
//            if (apiError != null)
//                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
//            else
//                showError();
//            hideProgressBar();
        }

        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.USER_L123_CATEGORY_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), UserShopResult.class, new OnGsonParseCompleteListener<UserShopResult>() {
                        @Override
                        public void onParseComplete(UserShopResult data) {
                            if (data.result != null && data.result.size() > 0) {
                                CommonUtility.setUserL1L2L3CategoryList(context, data);
                                setUpNavigationDrawer();
                            }
                            setupViewPager(viewPager);
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressBar();
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

        if (request.getIdentifier() == RequestIdentifier.GET_USER_PENDING_CREDIT.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), PendingCreditResult.class, new OnGsonParseCompleteListener<PendingCreditResult>() {
                        @Override
                        public void onParseComplete(PendingCreditResult data) {
                            if (data.creditResult.credit != null && getActivity() != null && getNavigationDrawer() != null) {
                                int creditRs = CommonUtility.convertionPaiseToRupee(data.creditResult.credit);
                                DataStore.setUserCredit(context, creditRs + "");
                                getNavigationDrawer().updateCreditFacility();
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressBar();
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
        if (eventObject.id == EventConstant.UPDATE_TAB_COUNT_CART_ID) {
            int cartCount = (int) eventObject.objects[0];
            pagerAdapter.updateCartFragmentTitle(cartCount);

        }

        if (eventObject.id == EventConstant.UPDATE_TAB_COUNT_FAV_PRODUCT_ID) {
            int favCount = (int) eventObject.objects[0];
            CustomLogger.d("favouriteCount" + favCount);
            pagerAdapter.updateFavProdFragmentTitle(favCount);

        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        hideSearchMenu();
        hideFavMenu();
        hideCartMenu();
        hideFavTextMenu();
        showDealFilterMenu();
        hideMyOrderMenu();
        if (getActivity() != null && isAdded()) {
            if (homeMenu != null) {
                homeMenu.setTitle(getString(R.string.mall));
            }

            if (filterMenu != null) {
                filterMenu.setActionView(R.layout.deal_filter_custom_icon);

                View filterView = MenuItemCompat.getActionView(filterMenu);
                filterValueTV = (TextView) filterView.findViewById(R.id.filterValueTV);

                filterView.findViewById(R.id.filterFL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.OPEN_DEAL_FILTERS);
                    }
                });

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_filter:
                // getNavigationDrawerLayout().openDrawer(GravityCompat.END);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.OPEN_DEAL_FILTERS);
                return true;

            case R.id.action_home:
                addToBackStack((BaseActivity) getActivity(), HomeFragment.newInstance(), HomeFragment.getTagName());
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onBackPressed() {
        CommonUtility.exitApp(getActivity());
        return false;
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == fabMall.getId()) {
            addToBackStack((BaseActivity) getActivity(), HomeFragment.newInstance(), HomeFragment.getTagName());

        }
    }

    private void handleFabVisibility(int position) {
        switch (position) {
            case 0:
                fabMall.show();
                showDealFilterMenu();
                break;
            case 1:
                fabMall.show();
                hideDealFilterMenu();
                break;

            default:
                fabMall.show();
                showDealFilterMenu();

                break;
        }
    }


    @Override
    public void onApplyFilter(Integer isPublicDeal, Map<String, Set<FilterValue>> selectedFilterMap) {
        Integer filterNum = selectedFilterMap.size();
        if (filterNum > 0) {
            filterValueTV.setVisibility(View.VISIBLE);
            filterValueTV.setText(filterNum + "");
            getContext().invalidateOptionsMenu();
        }
    }

    @Override
    public void onClearFilterApplied() {
        filterValueTV.setVisibility(View.GONE);
    }
}

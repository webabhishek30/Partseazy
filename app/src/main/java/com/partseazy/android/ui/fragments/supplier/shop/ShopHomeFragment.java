package com.partseazy.android.ui.fragments.supplier.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewPagerAdapter;
import com.partseazy.android.ui.adapters.suppliers.OnSortClickListener;
import com.partseazy.android.ui.adapters.suppliers.search.OnPlaceClickListener;
import com.partseazy.android.ui.adapters.suppliers.shop.LocationTagsAdapter;
import com.partseazy.android.ui.fragments.supplier.map.ShopListMapFragment;
import com.partseazy.android.ui.model.catalogue.SortAttibute;
import com.partseazy.android.ui.model.supplier.placeAutocomplete.Prediction;
import com.partseazy.android.ui.model.supplier.search.TagModel;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopHomeFragment extends BaseFragment implements View.OnClickListener, OnSortClickListener, OnPlaceClickListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.toolbarRecyclerView)
    protected RecyclerView toolbarRecyclerView;

    @BindView(R.id.searchIV)
    protected ImageView searchIV;

    @BindView(R.id.tagRL)
    protected RelativeLayout tagRL;

    @BindView(R.id.sortTV)
    protected TextView sortTV;

    public static final String SELECT_TAB_POSITION = "select_tab_position";
    public static final String RETAILER_RESULT = "retailer_result";
    private BaseViewPagerAdapter pagerAdapter;
    private int selectedTabPosition = 0;
    private String shopDataJson;
    private String sortCode = "size_asc";
    private LocationTagsAdapter locationTagsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedTabPosition = getArguments().getInt(SELECT_TAB_POSITION, selectedTabPosition);
        shopDataJson = getArguments().getString(RETAILER_RESULT);
        CustomLogger.d("Retailer Json ::" + shopDataJson);

    }


    public static ShopHomeFragment newInstance() {
        Bundle bundle = new Bundle();
        ShopHomeFragment fragment = new ShopHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ShopHomeFragment newInstance(String retailerResult, int selectTabPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(SELECT_TAB_POSITION, selectTabPosition);
        bundle.putString(RETAILER_RESULT, retailerResult);
        ShopHomeFragment fragment = new ShopHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shop_home;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.results);
    }


    public static String getTagName() {
        return ShopHomeFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        setLocationTagsView();
        setupViewPager(viewPager);
        searchIV.setOnClickListener(this);
        sortTV.setOnClickListener(this);

    }


    private void setupViewPager(ViewPager viewPager) {
        if (getActivity() != null && isAdded()) {
            pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(), context);
            pagerAdapter.addFragment(ShopListFragment.newInstance(shopDataJson), getString(R.string.list));
            pagerAdapter.addFragment(ShopListMapFragment.newInstance(shopDataJson), getString(R.string.map));
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(selectedTabPosition, false);

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
            }
        });
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);
    }


    public void setLocationTagsView() {
        List<TagModel> locationTagList = new ArrayList<>();
        Type listType = new TypeToken<List<TagModel>>() {
        }.getType();
        locationTagList = new Gson().fromJson(ShopsDetailBaseFragment.finalSearchData.locationName, listType);
        if (locationTagList != null && locationTagList.size() > 0) {
            tagRL.setVisibility(View.VISIBLE);
            if (locationTagsAdapter == null)
                locationTagsAdapter = new LocationTagsAdapter(context, locationTagList);
            toolbarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            toolbarRecyclerView.setAdapter(locationTagsAdapter);
        } else {
            tagRL.setVisibility(View.GONE);
            toolbar.setTitle(getString(R.string.results));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        hideFavMenu();
        hideCartMenu();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                KeyPadUtility.hideSoftKeypad(getActivity());
                ((BaseActivity) getActivity()).onPopBackStack(true);
                return true;

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.searchIV:
                ((BaseActivity) getActivity()).onPopBackStack(true);
                break;

            case R.id.sortTV:

                ShopsSortDailog retailersSortDailog = ShopsSortDailog.newInstance(ShopHomeFragment.this, sortCode);
                retailersSortDailog.show(getActivity().getFragmentManager(), "dialog");

                break;
        }

    }

    @Override
    public void onSelectSortClick(SortAttibute sortAttibute) {
        sortCode = sortAttibute.sortCode;
        PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_SORT_SHOP_TYPE, sortCode);

    }

    @Override
    public void onSelectPlaceClick(Prediction prediction) {

    }

}

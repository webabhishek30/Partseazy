package com.partseazy.android.ui.fragments.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewPagerAdapter;
import com.partseazy.android.ui.fragments.favourites.FavProductFragment;
import com.partseazy.partseazy_eventbus.EventObject;

import butterknife.BindView;

/**
 * Created by can on 19/12/16.
 */

public class CartHomeFragment extends BaseFragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private BaseViewPagerAdapter pagerAdapter;
    private boolean launchFav = false;
    public static final String LAUNCH_FAV = "launch_fav";


    public static CartHomeFragment newInstance(boolean launchFav) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CartHomeFragment.LAUNCH_FAV, launchFav);
        CartHomeFragment fragment = new CartHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchFav = getArguments().getBoolean(LAUNCH_FAV, launchFav);

    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cart_viewpager;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.cart_name);
    }

    public static String getTagName() {
        return CartHomeFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        setupViewPager(viewPager);

    }


//    protected void callItemCountRequest() {
//        CustomLogger.d("Time to load Cart");
//        showProgressDialog();
//        getNetworkManager().GetRequest(RequestIdentifier.ITEM_COUNT_ID.ordinal(),
//                WebServiceConstants.ITEM_COUNT, null, null, this, this, false);
//    }

//    @Override
//    public boolean handleErrorResponse(Request request, VolleyError error) {
//        hideProgressDialog();
//        showError();
//        return true;
//    }


    private void setupViewPager(ViewPager viewPager) {
        if (getActivity() != null && isAdded()) {
            pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(), context);
            pagerAdapter.addFragment(CartFragment.newInstance(), getString(R.string.cart_value_pager, PartsEazyApplication.getInstance().getCartCount()));
            pagerAdapter.addFragment(FavProductFragment.newInstance(), getString(R.string.fav_value_pager, PartsEazyApplication.getInstance().getFavCount()));
            viewPager.setAdapter(pagerAdapter);
            if (launchFav) {
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
            }
        });
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
}

package com.partseazy.android.ui.fragments.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.base.BaseViewPagerAdapter;

import butterknife.BindView;

/**
 * Created by naveen on 6/6/17.
 */

public class OrderHomeFragment extends BaseFragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private BaseViewPagerAdapter pagerAdapter;
    private boolean launchBooking = false;
    public static final String LAUNCH_BOOKING = "launchBooking";


    public static OrderHomeFragment newInstance(boolean launchBooking) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(OrderHomeFragment.LAUNCH_BOOKING, launchBooking);
        OrderHomeFragment fragment = new OrderHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchBooking = getArguments().getBoolean(LAUNCH_BOOKING, launchBooking);

    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_viewpager;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.my_order_bookings);
    }

    public static String getTagName() {
        return OrderHomeFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        setupViewPager(viewPager);

    }



    private void setupViewPager(ViewPager viewPager) {
        if (getActivity() != null && isAdded()) {
            pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(), context);
            pagerAdapter.addFragment(MyOrderFragment.newInstance(), getString(R.string.orders));
            pagerAdapter.addFragment(MyBookingFragment.newInstance(), getString(R.string.bookings));
            viewPager.setAdapter(pagerAdapter);
            if (launchBooking) {
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
}


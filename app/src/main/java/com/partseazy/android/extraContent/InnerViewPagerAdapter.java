package com.partseazy.android.extraContent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import xyz.santeri.wvp.WrappingFragmentPagerAdapter;
import xyz.santeri.wvp.WrappingFragmentStatePagerAdapter;

public class InnerViewPagerAdapter extends WrappingFragmentPagerAdapter {
    private int tab;

    public InnerViewPagerAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tab = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                ViewPager1Fragment matches = new ViewPager1Fragment();
                return matches;
            case 1:
                ViewPager1Fragment sortlisted = new ViewPager1Fragment();
                return sortlisted;

            default:
                return new ViewPager1Fragment();
        }
    }

    @Override
    public int getCount() {
        return tab;
    }
}

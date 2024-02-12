package com.partseazy.android.ui.adapters.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partseazy.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by can on 19/12/16.
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();
    private Context mContext;

    public BaseViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    public void addFragment(Fragment fragment, String name) {
        fragmentList.add(fragment);
        fragmentTitles.add(name);
    }

    public void updateCartFragmentTitle(int itemcount) {
        fragmentTitles.set(0, mContext.getString(R.string.cart_value_pager, itemcount));
        notifyDataSetChanged();

    }

    public void updateFavProdFragmentTitle(int itemcount) {
        fragmentTitles.set(1, mContext.getString(R.string.fav_value_pager, itemcount));
        notifyDataSetChanged();

    }

}

package com.partseazy.android.ui.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.ui.adapters.navigation.NavigationDrawerAdapter;
import com.partseazy.android.ui.model.home.usershop.L1CategoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naveen Kumar on 31/1/17.
 */

public class NavigationDrawer {


    protected Context context;
    protected DrawerLayout drawerLayout;
    protected RecyclerView recyclerView;
    protected Toolbar toolbar;
    protected NavigationDrawerAdapter navigationDrawerAdapter;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected List<L1CategoryData> l1CategoryDataList;
    protected List<DrawerData> drawerItemList;

    public NavigationDrawer(Context context, List<L1CategoryData> l1CategoryDataList,
                            DrawerLayout drawerLayout,
                            RecyclerView drawerRecyclerView,
                            Toolbar toolbar) {

        this.context = context;
        this.l1CategoryDataList = l1CategoryDataList;
        this.drawerLayout = drawerLayout;
        this.recyclerView = drawerRecyclerView;
        this.toolbar = toolbar;

        // init();

    }

    public void setNavigationDrawer() {
        loadDataList();
        setupDrawerToggle();
        setDrawerAdapter();
    }

    private void loadDataList() {
        drawerItemList = new ArrayList<>();

        drawerItemList.add(new DrawerData(NavigationDrawerAdapter.TYPE_HEADER));
        drawerItemList.add(new DrawerData(NavigationDrawerAdapter.TYPE_HOME));

        if (l1CategoryDataList != null) {
            for (L1CategoryData catData : l1CategoryDataList) {
                drawerItemList.add(new DrawerData(NavigationDrawerAdapter.TYPE_CATEGORY, catData));
            }
        }
        drawerItemList.add(new DrawerData(NavigationDrawerAdapter.TYPE_ICON_TYPE));
        drawerItemList.add(new DrawerData(NavigationDrawerAdapter.TYPE_APP_INFO));
    }


    private void setDrawerAdapter() {
        if (navigationDrawerAdapter == null)
            navigationDrawerAdapter = new NavigationDrawerAdapter(context, drawerItemList, drawerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(navigationDrawerAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        drawerLayout.closeDrawers();

    }

    void setupDrawerToggle() {
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle = new ActionBarDrawerToggle((BaseActivity) context, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
    }

    public class DrawerData {
        public int id;
        public String name;
        public String type;
        public L1CategoryData l1CategoryData;

        public DrawerData(String type) {
            this.type = type;
        }

        public DrawerData(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public DrawerData(String type, L1CategoryData l1CategoryData) {
            this.type = type;
            this.l1CategoryData = l1CategoryData;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void updateNavigationDrawerData(List<L1CategoryData> list) {
        l1CategoryDataList = list;
        navigationDrawerAdapter.notifyDataSetChanged();
    }

    public void updateCreditFacility()
    {
        navigationDrawerAdapter.notifyItemChanged(0);
    }

    public void updateAdapter(){
        navigationDrawerAdapter = null;
        navigationDrawerAdapter = new NavigationDrawerAdapter(context, drawerItemList, drawerLayout);
        recyclerView.setAdapter(navigationDrawerAdapter);
    }
}

package com.partseazy.android.ui.fragments.home;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.facebook.FacebookSdk;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.analytics.MoengageUtility;
import com.partseazy.android.pushnotification.PushNotificationTracker;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.extraContent.QRCodeFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.base.BaseViewPagerAdapter;
import com.partseazy.android.ui.adapters.home.AutoSuggestionListAdapter;
import com.partseazy.android.ui.fragments.account.MyOrderFragment;
import com.partseazy.android.ui.fragments.account.OrderHomeFragment;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.listener.RecyclerItemClickListener;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.fav.FavResultData;
import com.partseazy.android.ui.model.home.autosuggestion.AutoSuggesstionKeyword;
import com.partseazy.android.ui.model.home.autosuggestion.AutoSuggesstionKeywordData;
import com.partseazy.android.ui.model.home.usershop.Child;
import com.partseazy.android.ui.model.home.usershop.Child_;
import com.partseazy.android.ui.model.home.usershop.L1CategoryData;
import com.partseazy.android.ui.model.home.usershop.L2CategoryData;
import com.partseazy.android.ui.model.home.usershop.L3CategoryData;
import com.partseazy.android.ui.model.home.usershop.UserShopResult;
import com.partseazy.android.ui.model.user.UserDetails;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.yariksoffice.lingver.Lingver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.viewPager)
    protected ViewPager viewPager;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.fabCartIcon)
    protected FloatingActionButton homefabCartIcon;
    @BindView(R.id.favIcon)
    protected ImageView favIcon;
    @BindView(R.id.menuOptionsIcon)
    protected ImageView menuOptionsIcon;
    @BindView(R.id.searchET)
    protected EditText searchET;
    @BindView(R.id.goTV)
    protected TextView goTV;
    @BindView(R.id.crossIV)
    protected ImageView crossIV;
    @BindView(R.id.searchIconLL)
    protected LinearLayout searchIconLL;
    @BindView(R.id.menuIconLL)
    protected LinearLayout menuIconLL;
    @BindView(R.id.cartValueTV)
    protected TextView cartValueTV;
    @BindView(R.id.cartAmountTV)
    protected TextView cartAmountTV;
    @BindView(R.id.recyclerViewAutoSuggest)
    protected RecyclerView recyclerViewAutoSuggest;
    @BindView(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;


//    @BindView(R.id.bottomNavigationView)
//    BottomNavigationView bottomNavigationView;

    @BindView(R.id.homeLL)
    protected LinearLayout homeLL;

    @BindView(R.id.dealLL)
    protected LinearLayout dealLL;

    @BindView(R.id.chatLL)
    protected LinearLayout chatLL;

    @BindView(R.id.callLL)
    protected LinearLayout callLL;


    @BindView(R.id.btn_mode_switch)
    protected ImageView drawerView;
    @BindView(R.id.btn_home)
    protected ImageView btn_home;
    protected ImageLoader imageLoader;


    private BaseViewPagerAdapter pagerAdapter;
    private List<L2CategoryData> tabList;

    private FloatingActionButton contactFB;

    public static final String SELECT_TAB_POSITION = "select_tab_position";
    public int selectedTabPosition = 0;
   private List<AutoSuggesstionKeywordData> autoResultList ;

    public static HomeFragment newInstance() {
        Bundle bundle = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static HomeFragment newInstance(int selectTabPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(SELECT_TAB_POSITION, selectTabPosition);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setRetainInstance(true);
        selectedTabPosition = getArguments().getInt(SELECT_TAB_POSITION, selectedTabPosition);
        imageLoader = ImageManager.getInstance(getContext()).getImageLoader();
        autoResultList = new ArrayList<>();
        getNecessaryPermissions();

        //Facebook Event Tracking Enable
        initFBEvent();

        //Moengage Tracking
        if (PushNotificationTracker.isMoEngageTrackingActive())
            sendUserBasicAttributeToMoengage();

        launchLoginTaskForApplogic();

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                fetchCartCountHandler();
                initOtherRequiredCalls();

            }
        });


    }


    private void initOtherRequiredCalls() {
        FavouriteUtility.callGetfavItemRequest(this);
        String userId = DataStore.getUserID(getContext());
        {
            if (userId != null)
                callUserDetails(userId);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (tabList != null && tabList.size() > 0) {
            drawerView.setVisibility(View.VISIBLE);
            setupViewPager(viewPager, tabList);
        } else {
            drawerView.setVisibility(View.INVISIBLE);
            loadUserL123Category();
        }
    }

    private void updateNavigationDrawer() {
        drawerView.setVisibility(View.VISIBLE);
        setUpNavigationDrawer();
        toolbar.setNavigationIcon(null);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected String getFragmentTitle() {
        return HomeFragment.class.getSimpleName();
    }


    public static String getTagName() {
        return HomeFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateCartIconOnHome();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        toolbar.setNavigationIcon(null);
        hideKeyBoard(view);
        homefabCartIcon.setOnClickListener(this);
        //  homefabCartIcon.setImageResource(R.drawable.icon_cart_black);
        favIcon.setOnClickListener(this);
        drawerView.setOnClickListener(this);
        menuOptionsIcon.setOnClickListener(this);
        crossIV.setOnClickListener(this);
        goTV.setOnClickListener(this);

        dealLL.setOnClickListener(this);
        chatLL.setOnClickListener(this);
        callLL.setOnClickListener(this);
        btn_home.setOnClickListener(this);


        recyclerViewAutoSuggest.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerViewAutoSuggest ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if (autoResultList.get(position).getName() != null) {
                            recyclerViewAutoSuggest.setVisibility(View.GONE);
                            searchET.setText("");
                            BaseFragment proFragment = ProductDetailFragment.newInstance(Integer.valueOf(autoResultList.get(position).getProductId()), autoResultList.get(position).getName());
                            BaseFragment.addToBackStack(getContext(), proFragment, ProductDetailFragment.getTagName());
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );


        //setupBottomNavigationView();

        searchET.addTextChangedListener(new MyTextWatcher(searchET));
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String searchQuery = searchET.getText().toString().trim();
                    if (searchQuery != null && !searchQuery.equals("")) {
                        searchIconLL.setVisibility(View.VISIBLE);
//                        menuIconLL.setVisibility(View.GONE);
                        searchET.setText("");
                        addToBackStack((BaseActivity) getActivity(), CatalogueFragment.newInstance(searchQuery, true, false), CatalogueFragment.getTagName());
                        //    performSearch();
                    } else {
                        searchIconLL.setVisibility(View.GONE);
                        //                 menuIconLL.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });

        contactFB = (FloatingActionButton) view.findViewById(R.id.contactUsFAB);
        contactFB.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyBoard(getView());

    }


    @Override
    protected boolean isNavigationDrawerNeeded() {
        return true;
    }

    @Override
    protected DrawerLayout getNavigationDrawerLayout() {
        return (DrawerLayout) getView().findViewById(R.id.drawerLayout);
    }

    @Override
    protected RecyclerView getNavigationDrawerRecyclerView() {
        return (RecyclerView) getView().findViewById(R.id.drawerRecylerView);
    }


    private void setupViewPager(ViewPager viewPager, List<L2CategoryData> tabList) {
        if (getActivity() != null && isAdded()) {
            pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(), context);
            for (int i = 0; i < tabList.size(); i++) {
                pagerAdapter.addFragment(HomeCategoryFragment.newInstance(tabList.get(i).id, tabList.get(i).firstChildId), tabList.get(i).name);
            }
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(selectedTabPosition, false);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons(tabList);
            //TODO uncomment the below code to hide tablayout for one L2 category
            if (tabList.size() > 1) {
                tabLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    private void setupTabIcons(List<L2CategoryData> tablist) {
        for (int i = 0; i < tablist.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_home, null);
            TextView tabNameTV = (TextView) view.findViewById(R.id.tabNameTV);
            NetworkImageView tabIcon = (NetworkImageView) view.findViewById(R.id.tabIconIV);

//            tabIcon.setImageUrl(CommonUtility.getFormattedImageUrl(tablist.get(i).icon), imageLoader);
//            tabNameTV.setText(tablist.get(i).name);


            String formatedURL = CommonUtility.getFormattedImageUrl(tablist.get(i).icon, tabIcon, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CustomLogger.d("Image url :: " + formatedURL);
            CommonUtility.setImageSRC(imageLoader, tabIcon, formatedURL);
            tabNameTV.setText(tablist.get(i).name);


            tabLayout.getTabAt(i).setCustomView(view);
        }
    }

    private void parseTabList(List<Child> childList) {
        tabList = new ArrayList<>();
        if (childList.size() > 0) {
            for (Child child : childList) {
                if (child.icon != null) {
                    tabList.add(new L2CategoryData(child.id, child.name, child.icon.src, child.children.get(0).id));
                } else {
                    tabList.add(new L2CategoryData(child.id, child.name, "", child.children.get(0).id));
                }
            }
        }
    }


    private void loadUserL123Category() {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.HOME_SHOP_USER_TAB_REQUEST_ID.ordinal(),
                WebServiceConstants.GET_USER_L123_LIST, null, params, this, this, true);
    }


    protected void callUserDetails(String userId) {

        getNetworkManager().GetRequest(RequestIdentifier.GET_USER_DETAILS.ordinal(),
                WebServiceConstants.USER_DETAILS + userId, null, null, this, this, false);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        if (request.getIdentifier() == RequestIdentifier.HOME_SHOP_USER_TAB_REQUEST_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
            hideProgressBar();

        }

        if (request.getIdentifier() == RequestIdentifier.FAV_ID.ordinal()) {
            showError();
            hideProgressBar();
        }
        if (request.getIdentifier() == RequestIdentifier.AUTO_SUGGESTION_SEARCH_KEYWORD_ID.ordinal()) {
            showError();
        }

        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.HOME_SHOP_USER_TAB_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), UserShopResult.class, new OnGsonParseCompleteListener<UserShopResult>() {
                        @Override
                        public void onParseComplete(UserShopResult data) {
                            try {
                                if (data.result != null && data.result.size() > 0) {
                                    parseTabList(data.result.get(0).children);
                                    setUserL1L2L3CategoryList(data);

                                }
                                setupViewPager(viewPager, tabList);
                                hideProgressBar();
                            } catch (Exception exception) {
                                CustomLogger.e("Exception ", exception);
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


        if (request.getIdentifier() == RequestIdentifier.FAV_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {

                            if (data.result != null) {
                                FavouriteUtility.updateFavMapOnAddRemove(data);
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }


        if (request.getIdentifier() == RequestIdentifier.GET_USER_DETAILS.ordinal()) {
            hideProgressBar();


            getGsonHelper().parse(responseObject.toString(), UserDetails.class, new OnGsonParseCompleteListener<UserDetails>() {
                        @Override
                        public void onParseComplete(UserDetails data) {

                            if (data.name != null && data.email != null) {
                                DataStore.setUserName(getContext(), data.name);
                                DataStore.setAppUserEmail(getContext(), data.email);
                            }
                            if (data.l1Category != null) {
                                DataStore.setUserCategoryId(getContext(), data.l1Category);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        if (request.getIdentifier() == RequestIdentifier.AUTO_SUGGESTION_SEARCH_KEYWORD_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), AutoSuggesstionKeyword.class, new OnGsonParseCompleteListener<AutoSuggesstionKeyword>() {
                        @Override
                        public void onParseComplete(AutoSuggesstionKeyword data) {

                            if (data.result != null && data.result.size() > 0 ) {
                                recyclerViewAutoSuggest.setVisibility(View.VISIBLE);
                                autoResultList = data.result;
                                recyclerViewAutoSuggest.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false ));
                                AutoSuggestionListAdapter adapter = new AutoSuggestionListAdapter(getContext(), data.result);
                                recyclerViewAutoSuggest.setAdapter(adapter);
                            } else {
                                recyclerViewAutoSuggest.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        return true;
    }


    private void setUserL1L2L3CategoryList(UserShopResult userShopResult) {
        List<L1CategoryData> l1CategoryDataList = new ArrayList<>();
        List<L2CategoryData> l2CategoryDataList = new ArrayList<>();
        List<L3CategoryData> l3CategoryDataList = new ArrayList<>();

        for (int i = 0; i < userShopResult.result.size(); i++) {

            L1CategoryData l1CategoryData = new L1CategoryData();
            l1CategoryData.name = userShopResult.result.get(i).name;
            l1CategoryData.id = userShopResult.result.get(i).id;
            l1CategoryData.icon = userShopResult.result.get(i).icon.src;
            l1CategoryData.imageUrl = userShopResult.result.get(i).image.src;
//            l1CategoryData.icon = userShopResult.result.get(i).icon;
            l1CategoryData.l2CategoryDataList = new ArrayList<>();

            for (Child catChild : userShopResult.result.get(i).children) {

                L2CategoryData l2CategoryData = new L2CategoryData();
                l2CategoryData.id = catChild.id;
                l2CategoryData.name = catChild.name;
                if (catChild.icon != null) {
                    l2CategoryData.icon = catChild.icon.src;
                }
                if (catChild.image != null) {
                    l2CategoryData.imageUrl = catChild.image.src;
                }
                l2CategoryData.l3CategoryDataList = new ArrayList<>();

                for (Child_ l3CatChild : catChild.children) {
                    L3CategoryData l3CategoryData = new L3CategoryData();
                    l3CategoryData.id = l3CatChild.id;
                    l3CategoryData.name = l3CatChild.name;
                    if (l3CatChild.icon != null) {
                        l3CategoryData.icon = catChild.icon.src;
                    }
                    if (l3CatChild.image != null) {
                        l3CategoryData.imageUrl = l3CatChild.image.src;
                    }
                    l2CategoryData.l3CategoryDataList.add(l3CategoryData);
                    l3CategoryDataList.add(l3CategoryData);
                }
                l2CategoryDataList.add(l2CategoryData);
                l1CategoryData.l2CategoryDataList.add(l2CategoryData);
            }

            l1CategoryDataList.add(l1CategoryData);
        }

        String L1categoryListString = new Gson().toJson(l1CategoryDataList);
        DataStore.setL1CategoryList(getContext(), L1categoryListString);

        String L2categoryListString = new Gson().toJson(l2CategoryDataList);
        DataStore.setL2CategoryList(getContext(), L2categoryListString);

        String L3categoryListString = new Gson().toJson(l3CategoryDataList);
        DataStore.setL3CategoryList(getContext(), L3categoryListString);

        updateNavigationDrawer();

        CustomLogger.d("L1categoryListString ::" + L1categoryListString);
        CustomLogger.d("L2categoryListString ::" + L2categoryListString);
        CustomLogger.d("L3categoryListString ::" + L3categoryListString);
    }

//    private void setupBottomNavigationView() {
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_deals:
//                        BaseFragment.addToBackStack((BaseActivity) context, DealHomeFragment.newInstance(), DealHomeFragment.getTagName());
//                        break;
//
//                    case R.id.menu_chat:
//                        BaseFragment.addToBackStack((BaseActivity) context, SellDealDetailFragment.newInstance(), SellDealDetailFragment.getTagName());
//                        break;
//                }
//                return true;
//            }
//        });
//    }

    private class MyTextWatcher implements TextWatcher {
        private EditText view;
        private Timer timer = new Timer();
        private final long DELAY = 700; // milliseconds


        public MyTextWatcher(EditText view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //Calculate autosuggestion top margin
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) recyclerViewAutoSuggest.getLayoutParams();
            int marginTop = appBarLayout.getHeight()- tabLayout.getHeight();
            params.topMargin = marginTop;
        //    params.setMargins(0, marginTop, 0, 0);
            recyclerViewAutoSuggest.setLayoutParams(params);
            recyclerViewAutoSuggest.setVisibility(View.GONE);
            timer.cancel();
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            String searchQuery = searchET.getText().toString().trim();
                            if (searchQuery != null && !searchQuery.equals("")) {
                                loadAutoSuggestionData(searchQuery);
                            }

                        }
                    },
                    DELAY
            );
        }

    @Override
    public void afterTextChanged(Editable editable) {
        switch (view.getId()) {
            case R.id.searchET:
                String searchQuery = searchET.getText().toString().trim();
                if (searchQuery != null && !searchQuery.equals("")) {
                    searchIconLL.setVisibility(View.VISIBLE);
                    //                  menuIconLL.setVisibility(View.GONE);
                    // performSearch();
                } else {
                    searchIconLL.setVisibility(View.GONE);
                    //                menuIconLL.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

}


    @Override
    public void onClick(View view) {

        if (view.getId() == btn_home.getId()) {
            addToBackStack((BaseActivity) getActivity(), HomeFragment.newInstance(), HomeFragment.getTagName());
        }
        if (view.getId() == menuOptionsIcon.getId()) {

            BaseFragment qrCodeFragment = QRCodeFragment.newInstance();
            BaseFragment.addToBackStack((BaseActivity) getActivity(), qrCodeFragment, QRCodeFragment.getTagName());

            // showPopupMenu(view);
        }

        if (view.getId() == drawerView.getId()) {

            if (getNavigationDrawerLayout() != null)
                getNavigationDrawerLayout().openDrawer(GravityCompat.START);

        }


        if (view.getId() == favIcon.getId()) {
            launchFav();
//            BaseFragment cartFragment = CartHomeFragment.newInstance();
//            BaseFragment.addToBackStack((BaseActivity) getActivity(), cartFragment, CartHomeFragment.getTagName());
        }

        if (view.getId() == homefabCartIcon.getId()) {
//            BaseFragment cartFragment = CartHomeFragment.newInstance();
//            BaseFragment.addToBackStack((BaseActivity) getActivity(), cartFragment, CartHomeFragment.getTagName());
            launchCart();
        }


        if (view.getId() == crossIV.getId()) {
            searchET.setText("");
            searchIconLL.setVisibility(View.GONE);
            menuIconLL.setVisibility(View.VISIBLE);
        }

        if (view.getId() == goTV.getId()) {
            CustomLogger.d("go button ");
            String searchQuery = searchET.getText().toString().trim();
            //moengage searched product event
            MoengageUtility.trackEvent(searchQuery,context);

            if (searchQuery != null) {
                searchET.setText("");
                addToBackStack((BaseActivity) getActivity(), CatalogueFragment.newInstance(searchQuery, true, false), CatalogueFragment.getTagName());
            }
        }

        if (view.getId() == contactFB.getId()) {
            //  CommonUtility.dialPhoneNumber(getActivity(), StaticMap.CC_PHONE);
            addToBackStack((BaseActivity) getActivity(), DealHomeFragment.newInstance(), DealHomeFragment.getTagName());
        }

        if (view.getId() == dealLL.getId()) {
            addToBackStack((BaseActivity) getActivity(), DealHomeFragment.newInstance(), DealHomeFragment.getTagName());

//            AppExitDailog appExitDailog = new AppExitDailog();
//            appExitDailog.showUpgradeDialog(getActivity());
        }


        if (view.getId() == chatLL.getId()) {
            Intent intent = new Intent(context, ConversationActivity.class);
            if (ApplozicClient.getInstance(context).isContextBasedChat()) {
                intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
            }
            startActivity(intent);
        }


        if (view.getId() == callLL.getId()) {
            CommonUtility.dialPhoneNumber(getActivity(), StaticMap.CC_PHONE);
        }


    }


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        hideHomeMenu();
        PopupMenu popup = new PopupMenu(getContext(), view);
        Menu menuPopup = popup.getMenu();
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.show();
        menuPopup.findItem(R.id.action_home).setVisible(false);
        menuPopup.findItem(R.id.action_search).setVisible(false);
        menuPopup.findItem(R.id.action_favourite).setVisible(false);
        menuPopup.findItem(R.id.action_cart).setVisible(false);
        menuPopup.findItem(R.id.action_fav).setVisible(false);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_order) {
                    addToBackStack(getContext(), OrderHomeFragment.newInstance(true), MyOrderFragment.getTagName());
                    return true;
                }
                if (id == R.id.action_customercare) {
                    CommonUtility.dialPhoneNumber(getActivity(), StaticMap.CC_PHONE);
                    return true;
                }

                return true;
            }
        });


    }

    @Override
    protected void retryFailedRequest(int identifier, Request<?> oldRequest, VolleyError error) {
        if (oldRequest.getIdentifier() == RequestIdentifier.HOME_SHOP_USER_TAB_REQUEST_ID.ordinal()) {
            if (getActivity() != null && isAdded())
                showError(getString(R.string.wait_trying_to_reconnect), MESSAGETYPE.SNACK_BAR);
            loadUserL123Category();
        }


    }

    @Override
    protected void notifyCartCountUpdated() {
        updateCartIconOnHome();
    }

    public void updateCart()
    {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                fetchCartCountHandler();
            }
        });

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


    private void getNecessaryPermissions() {
        PermissionUtil.requestPermission(getContext());
    }

//    @Override
//    public boolean onBackPressed() {
//        CommonUtility.exitApp(getActivity());
//        return false;
//    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.GET_USER_DETAILS) {
            String userId = (String) eventObject.objects[0];
            callUserDetails(userId);
        }
        if (eventObject.id == EventConstant.UPDATE_TAB_COUNT_CART_ID) {
            updateCart();
        }
    }

    private void performSearch() {
        searchET.clearFocus();
        hideKeyBoard(getView());
    }

    private void loadAutoSuggestionData(String keyword) {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        String searchUrl = WebServiceConstants.GET_INDEX_AUTOSUGGESTION + "?q=" + keyword;
        getNetworkManager().GetRequest(RequestIdentifier.AUTO_SUGGESTION_SEARCH_KEYWORD_ID.ordinal(), searchUrl, null, params, this, this, false);
    }

    private void initFBEvent() {
        //To start Google Analytics
        FacebookSdk.setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.setAutoLogAppEventsEnabled(true);
    }

    private void sendUserBasicAttributeToMoengage() {

        String deviceId = CommonUtility.getDeviceId(getContext());
        String email = "";
        String mobile = "";
        String userName = "";

        String phone = CommonUtility.getDevicePhoneNUmber(getContext());

        if (DataStore.getUserName(getContext()) != null)
            userName = DataStore.getUserName(getContext());

        if (DataStore.getUserName(getContext()) != null)
            userName = DataStore.getUserName(getContext());

        mobile = (DataStore.getUserPhoneNumber(getContext()) != null) ? DataStore.getUserPhoneNumber(getContext()) : phone;
        email = (DataStore.getUserEmail(getContext()) != null) ? DataStore.getUserEmail(getContext()) : CommonUtility.getEmailFromMobile(getContext());

        MoEHelper.getInstance(getContext()).setUniqueId(deviceId);

        if (!TextUtils.isEmpty(email))
            MoEHelper.getInstance(getContext()).setEmail(email);

        if (!TextUtils.isEmpty(mobile))
            MoEHelper.getInstance(getContext()).setNumber( mobile);

        if (!TextUtils.isEmpty(userName))
            MoEHelper.getInstance(getContext()).setFullName(userName);

        CustomLogger.d("Sent User data to Monengage for " +
                "push notifications  User name " + userName +
                "Mobile " + mobile + " Email " + email);
    }


}

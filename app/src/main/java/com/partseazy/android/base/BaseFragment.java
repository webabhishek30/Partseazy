package com.partseazy.android.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.api.account.user.UserService;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.Tracker;
import com.partseazy.android.constants.IntentConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.GsonHelper;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.network.manager.NetworkManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.pushnotification.PushNotificationTracker;
import com.partseazy.android.ui.fragments.account.OrderHomeFragment;
import com.partseazy.android.ui.fragments.cart.CartHomeFragment;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.fragments.failure.OrderFailureFragment;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.model.cart.ItemCount;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.home.usershop.L1CategoryData;
import com.partseazy.android.ui.model.session.Session;
import com.partseazy.android.ui.model.user.UserDetails;
import com.partseazy.android.ui.widget.NavigationDrawer;
import com.partseazy.android.ui.widget.SearchCommon;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.android.utility.error.ErrorHandlerUtility;
import com.partseazy.android.utility.fonts.FontUtiity;
import com.partseazy.android.utility.fonts.ToolBarHelper;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.partseazy.android.network.error.ErrorHandler;
import com.yariksoffice.lingver.Lingver;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * Created by Pumpkin Guy on 04/12/16.
 */

public abstract class BaseFragment extends Fragment implements
        Response.Listener<JSONObject>, Response.ErrorListener,
        Toolbar.OnMenuItemClickListener,
        OnBackPressed {


    protected Dialog progressDialog;
    protected BaseActivity context;
    protected Toolbar toolbar;
    private Map<Integer, Map.Entry<Request<?>, VolleyError>> failedRequestsMap;
    private Map<Integer, String> successfulRequests;
    private static NetworkManager networkManager;
    private ImageManager imageManager;
    protected ProgressBar progressBar;
    protected MenuItem cartMenu, searchMenu, favMenu, homeMenu, sendMenu, shareMenu, filterMenu, orderMenu, favTextMenu;
    protected RecyclerView drawerRecylerView;
    private NavigationDrawer navigationDrawer;
    private FrameLayout noResultLayout;
    private Button noResultContinueBT;
    private TextView noResultTextTV;
    private ImageView noResultIV;
    //    protected static String actualBoardingST;
//    protected static String forcedBoardingST;
    protected static int storeID;
    private Snackbar networkSnackbar;
    private boolean isSnackBarshown;
    private boolean loadingSession = false;

    public enum MESSAGETYPE {
        TOAST, ALERT, SNACK_BAR

    }

    public enum NORESULTBUTTON {
        HOME, CATALOGUE_FILTER, SEARCH, CATALOGUE

    }

    public enum UserType {
        SUPPLIER("supplier"),
        RETAILER("retailer"),
        AGENT("employee");

        private final String userRole;

        private UserType(String userRole) {
            this.userRole = userRole;
        }

        public String getUserRole() {
            return userRole;
        }
    }


    @Nullable
    @Override
    public View getView() {
        return view;
    }

    private View view;

    public Toolbar getToolbar() {
        return toolbar;
    }


    public BaseActivity getContext() {
        return context;
    }

    protected abstract int getLayoutResId();

    protected abstract String getFragmentTitle();

    protected void setUpNavigationDrawer() {

        if (isNavigationDrawerNeeded() && getNavigationDrawerLayout() != null && getNavigationDrawerRecyclerView() != null) {
            navigationDrawer = new NavigationDrawer(getContext(), getNavigationDrawerDataList(), getNavigationDrawerLayout(), getNavigationDrawerRecyclerView(), toolbar);
            navigationDrawer.setNavigationDrawer();
        }
    }

    protected NavigationDrawer getNavigationDrawer() {
        return navigationDrawer;
    }

    protected List<L1CategoryData> getNavigationDrawerDataList() {

        Type listType = new TypeToken<List<L1CategoryData>>() {
        }.getType();

        return new Gson().fromJson(DataStore.getL1CategoryList(getContext()), listType);

    }

    protected boolean isNavigationDrawerNeeded() {
        return false;
    }

    @Nullable
    protected DrawerLayout getNavigationDrawerLayout() {
        return null;
    }

    @Nullable
    protected RecyclerView getNavigationDrawerRecyclerView() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkManager = NetworkManager.newInstance(getActivity(),
                BuildConfig.URL);
        context = (BaseActivity) getActivity();
        successfulRequests = new HashMap<Integer, String>();

        imageManager = ImageManager.getInstance(getActivity());
        initAllLibs();

        String userId = DataStore.getUserID(getContext());
        if (userId != null)
            callUserDetails(userId);


    }


    private void initAllLibs() {
        //Event Bus
        PartsEazyEventBus.getInstance().addObserver(this);
    }

    protected void callUserDetails(String userId) {

        getNetworkManager().GetRequest(RequestIdentifier.GET_USER_DETAILS.ordinal(),
                WebServiceConstants.USER_DETAILS + userId, null, null, this, this, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        setUpNavigationDrawer();
        setUpNoResultView();
        progressBar = (ProgressBar) view.findViewById(R.id.b2c2_progressbar);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        Log.d("Fingerprint", WebServiceConstants.ANDROID_DEVICE_ID);

        FontUtiity.changeFonts(view);
        ToolBarHelper.changeActionBarFonts(context.getSupportActionBar(), FontUtiity.getDefaultTypeface());
        KeyPadUtility.hideSoftKeypad(getActivity());

/*
        ((BaseActivity) getActivity()).mHelper.onFragmentStart(getActivity(), getClass().getSimpleName());
*/


        return view;

    }


    private void initToolbar() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            if (getFragmentTitle() != null)
                toolbar.setTitle(getFragmentTitle());
            context.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KeyPadUtility.hideSoftKeypad(getActivity());
                    ((BaseActivity) getActivity()).onPopBackStack(false);
                }
            });
        }
    }

    private void setUpNoResultView() {
        noResultLayout = (FrameLayout) view.findViewById(R.id.no_result_found);
        noResultTextTV = (TextView) view.findViewById(R.id.noResultTextTV);
        noResultIV = (ImageView) view.findViewById(R.id.noResultIV);
        noResultContinueBT = (Button) view.findViewById(R.id.noResultContinueBT);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onPause() {
        super.onPause();
        unRegisterEventBus();

    }

    protected void registerEventBus() {
        PartsEazyEventBus.getInstance().addObserver(this);
    }

    protected void unRegisterEventBus() {
        PartsEazyEventBus.getInstance().removeObserver(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        registerEventBus();
        KeyPadUtility.hideSoftKeypad(getActivity());
        if (!getTrackerScreenName())
            Tracker.trackScreen(getClass().getSimpleName());
        if (navigationDrawer != null)
            navigationDrawer.updateAdapter();
    }

    protected boolean getTrackerScreenName() {
        //Should be overrided by subclasess
        return false;
    }

    public NetworkManager getNetworkManager() {
        networkManager = NetworkManager.getNetworkManagerInstance();
        return networkManager;
    }

    public ImageLoader getImageLoader() {
        return imageManager.getImageLoader();
    }

    protected GsonHelper getGsonHelper() {
        return GsonHelper.getInstance();
    }

    protected void setTitle(String title) {
        if (toolbar != null) {
            ((BaseActivity) getActivity()).getSupportActionBar().setTitle(title);
        }
    }

    protected void showBackNavigation() {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.back_arrow);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
    }

    protected void showCrossNavigation() {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (navigationDrawer != null)
            navigationDrawer.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main_menu, menu);

        int cartCount = PartsEazyApplication.getInstance().getCartCount();

        /* Declaration for cart update icon */
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(cartItem, R.layout.cart_update_icon);
        View cartView = MenuItemCompat.getActionView(cartItem);
        TextView tvCart = (TextView) cartView.findViewById(R.id.cartValueTV);

        /* Declaration for fav update icon */
        MenuItem notificationItem = menu.findItem(R.id.action_favourite);
//        MenuItemCompat.setActionView(notificationItem, R.layout.notification_update_count);
//        View notificationView = MenuItemCompat.getActionView(notificationItem);
//        TextView tvNotification = (TextView) notificationView.findViewById(R.id.tv_notification_update);


        /* Hide cart count icon if cart count is 0 */
        if (cartCount > 0) {
            tvCart.setVisibility(View.VISIBLE);
            tvCart.setText(String.valueOf(cartCount));
        } else {
            tvCart.setVisibility(View.GONE);
        }


        /* cart view  click Listener*/
        cartView.findViewById(R.id.cartFL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCart();
            }
        });


        cartMenu = menu.findItem(R.id.action_cart);
        searchMenu = menu.findItem(R.id.action_search);
        favMenu = menu.findItem(R.id.action_favourite);
        favTextMenu = menu.findItem(R.id.action_fav);
        homeMenu = menu.findItem(R.id.action_home);
        sendMenu = menu.findItem(R.id.action_send);
        shareMenu = menu.findItem(R.id.action_share);
        filterMenu = menu.findItem(R.id.action_filter);
        orderMenu = menu.findItem(R.id.action_order);


        hideSendMenu();


        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                KeyPadUtility.hideSoftKeypad(getActivity());
                ((BaseActivity) getActivity()).onPopBackStack(true);
                return true;

            case R.id.action_home:
                popToHome(getActivity());
                return true;

            case R.id.action_search:
                launchSearch();
                return true;

            case R.id.action_cart:
                launchCart();
                return true;
            case R.id.action_favourite:
                launchFav();
                return true;

            case R.id.action_fav:
                launchFav();
                return true;

            case R.id.action_order:
                addToBackStack(getContext(), OrderHomeFragment.newInstance(false), OrderHomeFragment.getTagName());
                return true;


            case R.id.action_customercare:
                CommonUtility.dialPhoneNumber(getActivity(), StaticMap.CC_PHONE);
                return true;


            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }


    public void updateCartCountToolBar(int count) {

        //  showError("The cart count is " + count, MESSAGETYPE.TOAST);
        PartsEazyApplication.getInstance().setCartCount(count);
        notifyCartCountUpdated();
        getContext().invalidateOptionsMenu();

    }    public void updateCartAmountToolBar(int count) {

        //  showError("The cart count is " + count, MESSAGETYPE.TOAST);
        PartsEazyApplication.getInstance().setCartAmount(count);
        notifyCartCountUpdated();
        getContext().invalidateOptionsMenu();

    }

    public void updateFavCountToolBar(int count) {

        PartsEazyApplication.getInstance().setFavCount(count);
        getContext().invalidateOptionsMenu();
        notifyFavCountUpdated();
    }

    protected void hideDealFilterMenu() {
        if (filterMenu != null) {
            filterMenu.setVisible(false);
        }
    }

    protected void showDealFilterMenu() {
        if (filterMenu != null) {
            filterMenu.setVisible(true);
        }
    }

    protected void hideSearchMenu() {
        if (searchMenu != null) {
            searchMenu.setVisible(false);
        }
    }

    protected void hideShareMenu() {
        if (shareMenu != null) {
            shareMenu.setVisible(false);
        }
    }

    protected void hideMyOrderMenu() {
        if (orderMenu != null) {
            orderMenu.setVisible(false);
        }
    }

    protected void hideCartMenu() {
        if (cartMenu != null) {
            cartMenu.setVisible(false);
        }
    }

    protected void hideFavMenu() {
        if (favMenu != null) {
            favMenu.setVisible(false);
        }
    }

    protected void hideFavTextMenu() {
        if (favTextMenu != null) {
            favTextMenu.setVisible(false);
        }
    }

    protected void hideHomeMenu() {
        if (homeMenu != null) {
            homeMenu.setVisible(false);
        }
    }

    protected void hideSendMenu() {
        if (sendMenu != null) {
            sendMenu.setVisible(false);
        }
    }

    protected void visibleSendMenu() {
        if (sendMenu != null) {
            sendMenu.setVisible(true);
        }
    }



    protected void showShareMenu() {
        if (shareMenu != null) {
            shareMenu.setVisible(true);
        }
    }


    protected void loadSession() {
        CustomLogger.d("Time to load session");

        Map params = WebServicePostParams.sessionParams();

        getNetworkManager().PostRequest(RequestIdentifier.SESSION_REQUEST_ID.ordinal(),
                WebServiceConstants.SESSION_URL, params, null, this, this, false);

    }


    protected void renewSession(Request request, VolleyError error) {
        addFailedRequest(request, error);
        if (!loadingSession)
            loadSession();
    }

    protected void notifyCartCountUpdated() {
        //Empty function to be called by subclasses
    }

    protected void notifyFavCountUpdated() {
        //Empty function to be called by subclasses
    }

    protected void fetchCartCountHandler() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getCartCount();
            }
        });

    }

    private void getCartCount() {
        CustomLogger.d("Going to call cart count apis ");
        getNetworkManager().GetRequest(RequestIdentifier.CART_WISHLIST_COUNT.ordinal(),
                WebServiceConstants.ITEM_COUNT, null, null, this, this, false);
    }

    /**
     * Volley Error Response
     *
     * @param request
     * @param error
     */

    @Override
    final public void onErrorResponse(Request request, VolleyError error) {

        if (error instanceof NoConnectionError) {
            hideProgressBar();
            hideProgressDialog();
            if (!isSnackBarshown) showError();

        }

        if (error.networkResponse != null) {

            CustomLogger.d("Inside onErrorResponse " + error.getLocalizedMessage() +
                    "The error code is " + error.networkResponse.statusCode + "The Request ID is " + request.getIdentifier());


            //TimeOut handling

            if (error.getClass().equals(TimeoutError.class)) {

                hideProgressDialog();
                hideProgressBar();
                CustomLogger.d("Time Out");
            }


            //Checking for Session expired
            if (error.networkResponse.statusCode == ErrorHandler.SESSION_ERROR_CODE) {

                CustomLogger.e("Authentication Session is failure due to " + error.getLocalizedMessage());
                CustomLogger.e("Going to renew the session");


                if (failedRequestsMap == null || failedRequestsMap.get(request.getIdentifier()) == null) {
                    CustomLogger.d("Not able to renew the last sesssion, clearning everything and going to regenerate the session");
                    DataStore.clearInfo(getContext(), this);
                    renewSession(request, error);
                    return;
                }


                CustomLogger.d("Not able to renew the last sesssion, let's leave it to handle on subclass level ");


//                if (DataStore.getSessionID(getContext()) != null) {
//                    DataStore.clearInfo(getContext(), this);
//                    loadSession();
//                }


            }


        }

        addFailedRequest(request, error);


        APIError apiError = null;

        if (error != null) {
            apiError = ErrorHandlerUtility.getAPIErrorData(error);

        }
        if (!handleErrorResponse(request, error, apiError)) {

            CustomLogger.e(String.format(
                    "Request: (%s), Error not handled: (%s)", request.getUrl(),
                    error.getMessage()));
        }

    }


//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        PartsEazyEventBus.getInstance().addObserver(this);
//
//    }

    /**
     * Volley Response
     *
     * @param request
     * @param responseObject
     * @param response
     */
    @Override
    public final void onResponse(final Request<JSONObject> request, final JSONObject responseObject, final Response<JSONObject> response) {

        CustomLogger.d("Inside OnResponse " + responseObject.toString());

        //Session handling, would also take care for Retry
        if (request.getIdentifier() == RequestIdentifier.SESSION_REQUEST_ID.ordinal()) {
            parseSessionID(responseObject);
        }


        if (request.getIdentifier() == RequestIdentifier.CART_WISHLIST_COUNT.ordinal()) {


            getGsonHelper().parseOnUIThread(responseObject.toString(), ItemCount.class, new OnGsonParseCompleteListener<ItemCount>() {
                @Override
                public void onParseComplete(ItemCount itemCount) {
                    if (itemCount.cartCount != null)
                        updateCartCountToolBar(itemCount.cartCount);
                    if (itemCount.favCount != null)
                        updateFavCountToolBar(itemCount.favCount);
                    if (itemCount.cartAmount != null)
                        updateCartAmountToolBar(itemCount.cartAmount);

                }

                @Override
                public void onParseFailure(Exception exception) {

                }
            });

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

                            DataStore.setUserPremium(context, false);
                            DataStore.setConsumerFinancing(context, false);
                            if (data.tags != null) {
                                for (String tag : data.tags) {
                                    if ("premium".equalsIgnoreCase(tag)) {
                                        DataStore.setUserPremium(context, true);
                                    }
                                    if ("consumer_finance".equalsIgnoreCase(tag)) {
                                        DataStore.setConsumerFinancing(context, true);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        if (!handleResponse(request, responseObject, response)) {
            CustomLogger.e(String.format(
                    "Request: (%s), Response not handled: (%s)",
                    request.getUrl(), response.toString()));
        }


    }


    /**
     * @param request        volley request
     * @param responseObject api json object response
     * @param response       volley response
     * @return return true if response handled otherwise return false.
     */
    public boolean handleResponse(Request<JSONObject> request,
                                  JSONObject responseObject, Response<JSONObject> response) {
        return false;
    }

    /**
     * @param request  volley request
     * @param error    volley error
     * @param apiError
     * @return return true if error handled otherwise return false.
     */
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        return false;
    }


    protected boolean shouldIgnoreRepeatedResponse(Request<?> request,
                                                   Response<?> response) {
        return true;
    }


    public void onNetworkConnectionChanged(boolean connected) {

        if (connected) {
            CustomLogger.d("Resumed the network, Its time to remove the network error");
            retryFailedRequests();
        } else {
            CustomLogger.d("No Network, It's time to show error screen");
            showNetworkErrorSnackBar();


        }

    }


    protected void showNetworkErrorSnackBar() {

        isSnackBarshown = true;
   /*     if (getActivity() != null && !getActivity().isFinishing() && isAdded())
            networkSnackbar = SnackbarFactory.showSnackbar(getActivity(), getString(R.string.no_network_avbl), 0, getActivity().getString(R.string.close),
                    new SnackbarFactory.SnackBarButtonListener() {
                        @Override
                        public void onButtonClick() {
                            isSnackBarshown = false;

                        }
                    });*/
        final Dialog dialog=new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_no_network_found);
        dialog.findViewById(R.id.btn_retry_no_internet_connection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    protected final void retryFailedRequests() {
        if (this.failedRequestsMap != null && this.failedRequestsMap.size() > 0) {
            CustomLogger.d("The Failed request map size is " + this.failedRequestsMap.size());
            Set<Map.Entry<Integer, Map.Entry<Request<?>, VolleyError>>> entries = this.failedRequestsMap
                    .entrySet();
            for (Map.Entry<Integer, Map.Entry<Request<?>, VolleyError>> entry : entries) {
                Map.Entry<Request<?>, VolleyError> value = entry.getValue();
                CustomLogger.d("Going to retry the request post resuming network");
                retryFailedRequest(entry.getKey(), value.getKey(),
                        value.getValue());
            }
            this.failedRequestsMap.clear();
        }
    }

    /**
     * Should be implemented for retrying failed requests. It is invoked when
     * app connects to network or explicit retryFailedRequests() is invoked
     *
     * @param identifier Request Identifier
     * @param oldRequest Failed Request.
     * @param error      Failed error
     */
    protected void retryFailedRequest(int identifier, Request<?> oldRequest,
                                      VolleyError error) {
        CustomLogger.d("Inside retryFailedrequest");

    }

    private void addFailedRequest(Request<?> request, VolleyError error) {
        Map.Entry<Request<?>, VolleyError> failedEntry = new AbstractMap.SimpleImmutableEntry<Request<?>, VolleyError>(
                request, error);
        CustomLogger.d("Adding failed request to the map");
        if (this.failedRequestsMap == null) {
            this.failedRequestsMap = new HashMap<Integer, Map.Entry<Request<?>, VolleyError>>();
        }
        this.failedRequestsMap.put(request.getIdentifier(), failedEntry);
    }

    private void removeAnyFailedRequests(int identifier) {
        if (this.failedRequestsMap != null) {
            this.failedRequestsMap.remove(identifier);
        }
    }


    protected void parseSessionID(JSONObject responseObject) {
        getGsonHelper().parse(responseObject.toString(), Session.class, new OnGsonParseCompleteListener<Session>() {
                    @Override
                    public void onParseComplete(Session data) {

                        if (data != null) {
                            saveSessionData(data);

                        } else {
                            CustomLogger.d("There is some issue in session object parsing and session ID is ");
                        }

                    }

                    @Override
                    public void onParseFailure(Exception exception) {

                        CustomLogger.e("Exception ", exception);
                    }
                }

        );

    }

    private void saveSessionData(Session sessionData) {
        String sessionId = sessionData.sessionId;
        CustomLogger.d("Received the session " + sessionId);
        DataStore.setSessionID(getContext(), sessionId);
        if (sessionData.userRole != null && sessionData.userRole.size() > 0) {
            DataStore.setAgentType(getContext(), false);
            DataStore.setRetailerType(getContext(), false);
            DataStore.setSupplierType(getContext(), false);
            for (String userRole : sessionData.userRole) {
                if (userRole.equals(UserType.SUPPLIER.getUserRole())) {
                    DataStore.setSupplierType(getContext(), true);
                }
                if (userRole.equals(UserType.RETAILER.getUserRole())) {
                    DataStore.setRetailerType(getContext(), true);
                }
                if (userRole.equals(UserType.AGENT.getUserRole())) {
                    DataStore.setAgentType(getContext(), true);
                }
            }
        }

        if (sessionData.userId != null && sessionData.userId != 0) {
            DataStore.setUserId(getContext(), "" + sessionData.userId);

        }
        loadingSession = true;
        PartsEazyEventBus.getInstance().postEvent(EventConstant.SESSION_SAVED);

        // It might be that session got failed in any other api request and then it passed
        // So now control should go to the failed apis again
        retryFailedRequests();
    }

    public static BaseFragment getTopVisibleFragment(FragmentManager manager,
                                                     int containerId) {
        return manager != null ? (BaseFragment) manager.findFragmentById(containerId) : null;
    }

    // return
    public static BaseFragment getTopFragment(FragmentManager manager) {
        return getTopVisibleFragment(manager, R.id.container);
    }

    public static Fragment getTopFragmentFromList(FragmentManager manager) {
        List<android.support.v4.app.Fragment> fragments = manager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            return fragments.get(fragments.size() - 1);
        }
        return null;
    }

    protected static void doBeforeLaunchAnyFragment(BaseActivity activity) {
        KeyPadUtility.hideSoftKeypad(activity);
    }


    //To add fragment to the BackStack
    public static void addToBackStack(BaseActivity activity,
                                      BaseFragment fragment, String TAG) {

        if (activity != null) {
            doBeforeLaunchAnyFragment(activity);
            //Later on Custom animation can be given here
            replaceFragment(activity.getSupportFragmentManager(), R.id.container,
                    fragment, true, false, TAG);
        }
    }

    /**
     * overload for {@code addtoBackStack} with animate flag.
     * Use this to load the new fragment with default faide in animation
     */

    public static void addToBackStack(BaseActivity activity,
                                      BaseFragment fragment, String TAG, boolean animate) {
        doBeforeLaunchAnyFragment(activity);
        //Later on Custom animation can be given here
        replaceFragment(activity.getSupportFragmentManager(), R.id.container,
                fragment, true, animate, TAG);

    }


    //To
    public static void removeTopAndAddToBackStack(BaseActivity activity,
                                                  BaseFragment fragment, String TAG) {
        popBackStack(activity.getSupportFragmentManager());
        addToBackStack(activity, fragment, TAG);

    }


    public static void replaceFragment(final FragmentManager fragmentManager,
                                       final int targetId,
                                       final BaseFragment fragment, final boolean isAddToBackStack, final boolean animate,
                                       final String TAG) {

        new Handler().post(new Runnable() {
            public void run() {

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                //apply transition animation
                if (animate) {
//                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                try {
                    transaction.replace(targetId, fragment);
                    if (isAddToBackStack) {
                        transaction.addToBackStack(TAG);
                    }
                    transaction.commit();
                } catch (Exception e) {
                    CustomLogger.e("Got exception ", e);
                }

            }
        });

    }

    public static void popBackStack(final FragmentManager manager) {
        new Handler().post(new Runnable() {
            public void run() {
                try {
                    if (manager != null) {
                        CustomLogger.d("Pop up the last fragment");
                        manager.popBackStack();
                    }
                } catch (Exception e) {
                    CustomLogger.e(e.toString());
                }
            }
        });

    }

    public static void popToHome(FragmentActivity activity) {
        if (activity == null)
            return;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        CustomLogger.d("The Total fragments are " + fragmentManager.getBackStackEntryCount());
        // TLog.d("The Home fragments is " + fragmentManager.getBackStackEntryAt(1));

        if (fragmentManager.getBackStackEntryCount() > 1) {
            popBackStackTo(fragmentManager,
                    fragmentManager.getBackStackEntryAt(1));
            return;
        }

        if (fragmentManager.getBackStackEntryCount() == 1 && !(BaseFragment.getTopFragment(fragmentManager) instanceof DealHomeFragment)) {

            DealHomeFragment fragment = DealHomeFragment.newInstance();

            removeTopAndAddToBackStack((BaseActivity) activity, fragment, fragment.getTagName());
        }


    }

    public static void popToMall(FragmentActivity activity) {
        popToHome(activity);
        addToBackStack((BaseActivity) activity, HomeFragment.newInstance(), HomeFragment.getTagName());

    }


    public static void popBackStackTo(final FragmentManager manager,
                                      FragmentManager.BackStackEntry entry) {

        new Handler().post(new Runnable() {
            public void run() {
                try {
                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (Exception e) {
                    CustomLogger.e(e.toString());
                }
            }
        });

    }

    public static void popBackStackTo(FragmentManager manager,
                                      FragmentManager.BackStackEntry entry, int flag) {
        manager.popBackStack(entry.getName(), flag);
    }


    protected void showProgressBar() {

        CustomLogger.d("Progress Bar is shown " + progressBar);

        if (progressBar != null) {
            CustomLogger.d("Progress Bar is shown");
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    protected void hideProgressBar() {
        if (progressBar != null) {
            CustomLogger.d("Progress Bar is hidden");
            progressBar.setVisibility(View.GONE);
        }

    }

    protected void showNoResult(String noResultText) {
        if (noResultLayout != null) {
            noResultLayout.setVisibility(View.VISIBLE);
            if (noResultTextTV != null) {
                noResultTextTV.setText(noResultText);
            }
        }
    }

    protected void hideNoResult() {
        if (noResultLayout != null && noResultLayout.getVisibility() == View.VISIBLE) {
            noResultLayout.setVisibility(View.GONE);

        }
    }


    protected void launchLoginTaskForApplogic() {

        if (DataStore.getUserID(getActivity()) == null
                || DataStore.getUserName(getActivity()) == null
                || DataStore.isApplozicLoggedIn(getActivity())
                || DataStore.isApplozicLoginRequesting(getActivity())) {

            if (DataStore.getUserName(getActivity()) != null)
                updateDetailsAtApplozic(DataStore.getUserName(getActivity()));
            return;
        }

        DataStore.setApplozicLoginRequesting(context, true);

        final User user = new User();
        user.setUserId(DataStore.getUserID(getActivity())); //userId it can be any unique user identifier
        user.setDisplayName(DataStore.getUserName(getActivity())); //displayName is the name of the user which will be shown in chat messages
        user.setEmail(DataStore.getUserEmail(getActivity())); //optional
        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());  //User.AuthenticationType.APPLOZIC.getValue() for password verification from Applozic server and User.AuthenticationType.CLIENT.getValue() for access Token verification from your server set access token as password

        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {

            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                DataStore.setApplozicLoggedIn(context, true);
                ApplozicClient apzClient = ApplozicClient.getInstance(context);
                apzClient.enableShowUnreadCountBadge();
                apzClient.setNotificationStacking(true);
                PushNotificationTask pushNotificationTask = null;
                PushNotificationTask.TaskListener listener = new PushNotificationTask.TaskListener() {
                    @Override
                    public void onSuccess(RegistrationResponse registrationResponse) {
                    }

                    @Override
                    public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                    }

                };
                pushNotificationTask = new PushNotificationTask(Applozic.getInstance(context).getDeviceRegistrationId(), listener, context);
                pushNotificationTask.execute((Void) null);
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                DataStore.setApplozicLoginRequesting(context, false);
                new UserLoginTask(user, this, getActivity()).execute((Void) null);
            }
        };

        new UserLoginTask(user, listener, getActivity()).execute((Void) null);
    }

    private void updateDetailsAtApplozic(final String displayName) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    UserService.getInstance(context).updateDisplayNameORImageLink(displayName, null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }




    protected void showNoResult(String noResultText, View containerView,
                                final NORESULTBUTTON buttonType, final int pageId, final String searchQuery,
                                final boolean isfromSearch) {
        if (noResultLayout != null) {
            if (containerView != null)
                containerView.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
            if (noResultTextTV != null) {
                noResultTextTV.setText(noResultText);
            }
            if (noResultContinueBT != null) {

                switch (buttonType) {
                    case CATALOGUE_FILTER:
                        noResultContinueBT.setText(getString(R.string.clear_filters));
                        break;
                    case CATALOGUE:
                        noResultContinueBT.setText(getString(R.string.go_to_catalogue));
                        noResultIV.setVisibility(View.GONE);
                        break;
                    case SEARCH:
                        noResultContinueBT.setText(getString(R.string.search_again));
                        break;
                }
                noResultContinueBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        switch (buttonType) {
                            case HOME:
                                if (getActivity() != null)
                                    popToHome(getActivity());
                                return;
                            case CATALOGUE_FILTER:
                                popToHome(getActivity());
                                BaseFragment catalogueFragment = null;
                                if (isfromSearch) {
                                    catalogueFragment = CatalogueFragment.newInstance(searchQuery, true, false);
                                } else {
                                    catalogueFragment = CatalogueFragment.newInstance(pageId);
                                }
                                BaseFragment.addToBackStack((BaseActivity) getActivity(), catalogueFragment, CatalogueFragment.getTagName());
                                return;
                            case SEARCH:
                                launchSearch();
                                return;
                            case CATALOGUE:
                                BaseFragment cataFragment = CatalogueFragment.newInstance(pageId);
                                BaseFragment.addToBackStack((BaseActivity) getActivity(), cataFragment, CatalogueFragment.getTagName());
                                return;

                            default:
                                popToHome(getActivity());
                                return;
                        }


                    }
                });
            }
        }
    }

    protected void showNoResult(String noResultText, View containerView) {
        if (noResultLayout != null) {
            if (containerView != null)
                containerView.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
            if (noResultTextTV != null) {
                noResultTextTV.setText(noResultText);
            }
            if (noResultContinueBT != null) {
                noResultContinueBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popToHome(getActivity());
                    }
                });
            }
        }
    }

    protected void showNoResult(String noResultText, int ImageId, View containerView) {
        if (noResultLayout != null) {
            if (containerView != null)
                containerView.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
            if (noResultTextTV != null) {
                noResultTextTV.setText(noResultText);
            }
            if (noResultIV != null) {
                noResultIV.setImageResource(ImageId);
            }
            if (noResultContinueBT != null) {
                noResultContinueBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popToHome(getActivity());
                    }
                });
            }
        }
    }

    protected void hideNoResult(View containerView) {
        if (noResultLayout != null && noResultLayout.getVisibility() == View.VISIBLE) {
            if (containerView != null)
                containerView.setVisibility(View.VISIBLE);
            noResultLayout.setVisibility(View.GONE);

        }
    }


    protected void showProgressDialog(String msg) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getActivity());
        ((ProgressDialog) progressDialog).setMessage(msg);
        (progressDialog).setCancelable(true);
        ((ProgressDialog) progressDialog).setIndeterminate(true);
        progressDialog.show();

    }

    protected void showProgressDialog(String msg, boolean isCancelable) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getActivity());
        ((ProgressDialog) progressDialog).setMessage(msg);
        (progressDialog).setCancelable(isCancelable);
        ((ProgressDialog) progressDialog).setIndeterminate(true);
        progressDialog.show();

    }

    public void showProgressDialog() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getActivity());


        ((ProgressDialog) progressDialog).setMessage(getContext().getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(true);
        ((ProgressDialog) progressDialog).setIndeterminate(true);
        progressDialog.show();

    }


    public void showProgressDialog(boolean isCancelable) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getActivity());
        ((ProgressDialog) progressDialog).setMessage(getContext().getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(isCancelable);
        ((ProgressDialog) progressDialog).setIndeterminate(true);
        progressDialog.show();

    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            //get the Context object that was used to great the dialog
            Context context = ((ContextWrapper) progressDialog.getContext()).getBaseContext();

            // if the Context used here was an activity AND it hasn't been finished or destroyed
            // then dismiss it
            if (context instanceof Activity) {

                // Api >=17
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        dismissWithTryCatch(progressDialog);
                    }
                } else {

                    // Api < 17. Unfortunately cannot check for isDestroyed()
                    if (!((Activity) context).isFinishing()) {
                        dismissWithTryCatch(progressDialog);
                    }
                }
            } else
                // if the Context used wasn't an Activity, then dismiss it too
                dismissWithTryCatch(progressDialog);
        }
        progressDialog = null;
    }

    public void dismissWithTryCatch(Dialog dialog) {
        try {
            dialog.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
        }
    }

    public void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) view
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            IBinder binder = view.getWindowToken();
            inputManager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    protected void hideToolbarMenu() {
        if (cartMenu != null) {
            cartMenu.setVisible(false);
        }
        if (searchMenu != null) {
            searchMenu.setVisible(false);
        }

    }


    public void showMessage(String message, MESSAGETYPE type) {
        showError(message, type);
    }

    /**
     * show Default error message
     */
    public void showError() {

        if (noResultLayout != null && noResultLayout.getVisibility() == View.VISIBLE)
            return;

        if (isNetworkSnakbarShown())
            return;

        if (getActivity() != null)
            showError(getActivity().getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
    }

    /**
     * Call this method to show error
     *
     * @param message
     * @param type
     */
    public void showError(String message, MESSAGETYPE type) {
        switch (type) {
            case SNACK_BAR:
                if (getActivity() != null)
                    showSnackbar(message, 0);
                break;
            case TOAST:
                if (getActivity() != null)
                    ((PartsEazyApplication) getActivity().getApplication()).showToast(message);
                break;
            case ALERT:
                if (getActivity() != null)
                    DialogUtil.showAlertDialog(getActivity(), true, null, message, getString(R.string.OK)
                            , null, null, new DialogListener() {
                                @Override
                                public void onPositiveButton(DialogInterface dialog) {
                                }
                            });
                break;
            default:
                break;
        }
    }


    protected boolean isNetworkSnakbarShown() {
        return isSnackBarshown && !CommonUtility.isNetworkAvailable(getContext());
    }

    /**
     * show default snackbar with given duration
     *
     * @param message
     * @param duration - if 0 show Snackbar for default time
     *                 if >=1 show Snackbar for indefinite time
     */
    protected void showSnackbar(String message, int duration) {
        SnackbarFactory.showSnackbar(getActivity(), message, duration);
    }

    /**
     * show snackbar with button on right and button callback
     *
     * @param message    - message to show
     * @param duration   - if 0 show Snackbar for default time
     *                   if >=1 show Snackbar for indefinite time
     * @param buttonText - button name to show
     * @param listener   - listener for button tap or null
     */
    protected void showSnackbar(String message, int duration, String
            buttonText, SnackbarFactory.SnackBarButtonListener listener) {
        SnackbarFactory.showSnackbar(getActivity(), message, duration, buttonText, listener);
    }

    /*
     Event bus on event method if you want to receive any event then you have to override it
    */
    public void onEvent(EventObject eventObject) {
    }


    public void onEventMainThread(EventObject eventObject) {


    }

    public void initCollapsingToolbar(View view, final String titleName, final String topHeading) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        TextView topMessageTV = (TextView) view.findViewById(R.id.topHeadingTV);
        if (topHeading != null) {
            topMessageTV.setText(topHeading);
        }
        collapsingToolbar.setTitle(titleName);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbarHome);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);

    }


    public void launchCart() {

        addToBackStack(getContext(), CartHomeFragment.newInstance(false), CartHomeFragment.getTagName());
    }

    public void launchFav() {
        addToBackStack(getContext(), CartHomeFragment.newInstance(true), CartHomeFragment.getTagName());
    }

    public void launchSearch() {
        SearchCommon searchCommon = new SearchCommon(getContext());
        searchCommon.setSearchBox();
    }


    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                String otpCode = intent.getStringExtra(IntentConstants.OTP);
                CustomLogger.d("onReciver method otpCode" + otpCode);
                updateOTPView(otpCode);

            }
        }
    };

    protected void updateOTPView(String otpCode) {
        //Subclass needs to override this
    }


    @Override
    public boolean onBackPressed() {
        return true;
    }


    @Override
    public void onStop() {
        super.onStop();
/*
        ((BaseActivity) getActivity()).mHelper.onFragmentStop(getActivity(), getClass().getSimpleName());
*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleapUp();
    }

    private void cleapUp() {
        PartsEazyEventBus.getInstance().removeObserver(this);
//        FavouriteUtility.clearMap();
    }


    protected void showOrderFailureDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        // set title
        alertDialogBuilder.setTitle("Cancel Transaction");
        alertDialogBuilder.setCancelable(true);

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(com.payu.payuui.R.string.cancel_transaction))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popToHome(getActivity());
                                addToBackStack(getContext(), OrderFailureFragment.newInstance(true), OrderFailureFragment.class.getSimpleName());
                            }
                        }, 200);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}

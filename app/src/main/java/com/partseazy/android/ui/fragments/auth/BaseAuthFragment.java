package com.partseazy.android.ui.fragments.auth;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.AnalyticsManager;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.fragments.forceupgrade.ForceUpdateDialog;
import com.partseazy.android.ui.fragments.forceupgrade.ForceUpgradeDialogUtility;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.fragments.splash.NoUISplashScreen;
import com.partseazy.android.ui.fragments.splash.SplashFragment;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.registration.OnBoardingStatus;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class BaseAuthFragment extends BaseFragment {
    public enum OnBoardingSt {

        SPLASHBANNER("startup_banner"),
        SCREEN0("b2c-customer"),
        SCREEN1("mobile-number"),
        SCREEN2("verify-otp"),
        SCREEN3("user-type"),
        SCREEN4("basic-information"),
        SCREEN5_1("l1-selection"),
        SCREEN5("l2-selection"),
        SCREEN6("l3-segments"),
        SCREEN7("home");

        private final String status;

        private OnBoardingSt(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }


    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected String getFragmentTitle() {
        return BaseAuthFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Start Tracker
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (AnalyticsManager.getInstance() == null) //Re initiating it.
            AnalyticsManager.createInstance(getContext());

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!CommonUtility.isNetworkAvailable(context)) {
            SnackbarFactory.showNoConnectionSnackBar(getActivity(), getString(R.string.txtErrNoConnection), getString(R.string.txtSetting));
            return;
        }

        init();
        // dummyLaunchScreen();

    }

    public static String getTagName() {
        return BaseAuthFragment.class.getSimpleName();
    }

    private void init() {
        if (getAuthToken()) {
            loadFeatureMap();
        }
    }


    public boolean getAuthToken() {
        CustomLogger.d("Going to Initiate the session");
        loadSession();
        return false;
    }


    /**
     * load feature map for features ( driven through Server only)
     */
    private void loadFeatureMap() {
        getNetworkManager().GetRequest(RequestIdentifier.FEATURE_MAP_REQUEST_ID.ordinal(),
                WebServiceConstants.FEATURE_MAP, null, null, this, this, false);
    }


    /**
     * load default values for few constants e.g. customer care no, base url etc
     */
    private void loadStaticMap() {
        getNetworkManager().GetRequest(RequestIdentifier.STATIC_MAP_REQUEST_ID.ordinal(),
                WebServiceConstants.STATIC_MAP, null, null, this, this, false);
    }


    private void loadOnBoardingStatus() {

        Map requestParamsMap = WebServicePostParams.onBoardingParams();
        getNetworkManager().GetRequest(RequestIdentifier.ON_BOARDING_ST_REQ_ID.ordinal(),
                WebServiceConstants.ON_BOARDING_STATUS, requestParamsMap, null, this, this, false);
    }


    @Override
    public void onNetworkConnectionChanged(boolean connected) {
        super.onNetworkConnectionChanged(connected);

//        if (connected)
//            init();
    }

    @Override
    public void onStop() {
        super.onStop();
        getNetworkManager().cancelAll();
    }


    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.FEATURE_MAP_REQUEST_ID.ordinal()) {
            FeatureMap.setFeatureMap(responseObject);
            FeatureMap.Feature featuremap = FeatureMap.getFeatureMap(FeatureMapKeys.app_upgrades);

            ForceUpgradeDialogUtility.parseForceUpgradeMap(featuremap.PARAMS, featuremap.isActive(), new StaticMap.OnStaticMapParseListener() {
                @Override
                public void onParseError() {
                    handleErrorResponse(request, null, null);
                }
            });
            try {
                boolean shouldShow = shouldShowForceUpgradeDialog();
                boolean showForceUpgrade = false;
                loadStaticMap();

                if (shouldShow) {
                    int versionCode = BuildConfig.VERSION_CODE;

                    if (ForceUpgradeDialogUtility.VERSION_STATUS.LATER == ForceUpgradeDialogUtility.isUpgradeRequired(versionCode)) {
                        showForceUpgrade = false;
                    } else if (ForceUpgradeDialogUtility.VERSION_STATUS.EXPIRED == ForceUpgradeDialogUtility.isUpgradeRequired(versionCode)) {
                        showForceUpgrade = true;

                    }
                }
                if (shouldShow) {
                    showUpGradeDialog(new ForceUpdateDialog.OnForceUpdateClickListener() {
                        @Override
                        public void shouldGoFurther(boolean shouldGo) {

                            if (shouldGo)
                                loadStaticMap();
                        }
                    }, showForceUpgrade);
                } else {
                    loadStaticMap();
                }
            } catch (Exception e) {
                loadStaticMap();
            }
            return true;
        }


        if (request.getIdentifier() == RequestIdentifier.STATIC_MAP_REQUEST_ID.ordinal()) {
            StaticMap.parseStaticMap(responseObject, new StaticMap.OnMapListener() {
                @Override
                public void onParseError() {
                    handleErrorResponse(null, null, null);

                }
            });
            loadOnBoardingStatus();
            return true;
        }

        if (request.getIdentifier() == RequestIdentifier.ON_BOARDING_ST_REQ_ID.ordinal()) {
            onBoardingLauncherScreen(responseObject);
            return true;
        }


        return true;
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

  /*      SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong), 1, getString(R.string.tryAgain), new SnackbarFactory.SnackBarButtonListener() {
            @Override
            public void onButtonClick() {
                init();
            }
        });*/
        final Dialog dialog=new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_no_network_found);
        dialog.findViewById(R.id.btn_retry_no_internet_connection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                init();

            }
        });
        dialog.show();
        hideProgressBar();
        return true;
    }

    private void onBoardingLauncherScreen(JSONObject responseObject) {

        getGsonHelper().parse(responseObject.toString(), OnBoardingStatus.class, new OnGsonParseCompleteListener<OnBoardingStatus>() {
                    @Override
                    public void onParseComplete(OnBoardingStatus data) {
                        try {
                            if (data.invitationCode != null && !data.invitationCode.equals("")) {
                                DataStore.setInvitationCode(context, data.invitationCode);
                            }

                            OnBoardingUtility.OnBoardFragments fragment = OnBoardingUtility.onBoardingNavigation(context, data);
                            if (fragment.tagName.toLowerCase().contains("buydealdetailfragment") || fragment.tagName.toLowerCase().contains("productdetailfragment")
                                    || fragment.tagName.toLowerCase().contains("selldealfragment") || fragment.tagName.toLowerCase().contains("selldealdetailfragment")) {
                                BaseFragment.replaceFragment(getActivity().getSupportFragmentManager(),
                                        R.id.container, HomeFragment.newInstance(), true, true, HomeFragment.getTagName());
                            }
                            replaceLaunchScreen(fragment.fragment, fragment.tagName);
                        } catch (Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }

                    @Override
                    public void onParseFailure(Exception exception) {
                        CustomLogger.e("Exception ", exception);
                    }
                }

        );

    }

    protected void replaceLaunchScreen(BaseFragment fragment, final String tagName) {


        //By default, It would launch Home Fragment
        if (getActivity() != null) {
            BaseFragment.replaceFragment(getActivity().getSupportFragmentManager(),
                    R.id.container, fragment, false, true, tagName);

        }
    }


    @Override
    public void onEvent(EventObject eventObject) {

        CustomLogger.d("Inside OnEvent ");

        if (eventObject.id == EventConstant.SESSION_SAVED) {

            if (DataStore.getSessionID(getContext()) != null && (BaseFragment.getTopFragment(getFragmentManager()) instanceof SplashFragment)
                    || (BaseFragment.getTopFragment(getFragmentManager()) instanceof NoUISplashScreen)) {

                loadFeatureMap();

            }
        }
    }


    @Override
    protected void retryFailedRequest(int identifier, Request oldRequest, VolleyError error) {
        if (identifier == RequestIdentifier.FEATURE_MAP_REQUEST_ID.ordinal()) {
            try {
                showError(getString(R.string.wait_trying_to_reconnect), MESSAGETYPE.SNACK_BAR);
            } catch (Exception e) {
                CustomLogger.e("Exception ", e);
            }
            loadFeatureMap();
        }
    }

    protected boolean shouldShowForceUpgradeDialog() {

        int versionCode = BuildConfig.VERSION_CODE;

        if (ForceUpgradeDialogUtility.VERSION_STATUS.LATER == ForceUpgradeDialogUtility.isUpgradeRequired(versionCode) ||
                ForceUpgradeDialogUtility.VERSION_STATUS.EXPIRED == ForceUpgradeDialogUtility.isUpgradeRequired(versionCode)) {
            return true;
        }

        return false;
    }

    protected void showUpGradeDialog(ForceUpdateDialog.OnForceUpdateClickListener onForceUpdateClickListener, boolean forceStatus) {
        ForceUpdateDialog forceUpgradeDialog = new ForceUpdateDialog(onForceUpdateClickListener);
        forceUpgradeDialog.showUpgradeDialog(getActivity(), "", forceStatus);
    }


}

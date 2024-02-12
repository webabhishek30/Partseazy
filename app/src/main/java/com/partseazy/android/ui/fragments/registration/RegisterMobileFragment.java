package com.partseazy.android.ui.fragments.registration;


import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.moe.pushlibrary.MoEHelper;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.constants.IntentConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.services.MySMSBroadcastReceiver;
import com.partseazy.android.ui.adapters.registration.RegisterMobileFragmentAdapter;
import com.partseazy.android.ui.fragments.auth.BaseAuthFragment;
import com.partseazy.android.ui.fragments.auth.OnBoardingUtility;
import com.partseazy.android.ui.fragments.forceupgrade.AppExitDailog;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.registration.OnBoardingStatus;
import com.partseazy.android.ui.model.registration.banner.Banner;
import com.partseazy.android.ui.model.registration.banner.OTPVerification;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.android.utility.error.ErrorHandlerUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class RegisterMobileFragment extends BaseFragment implements RegisterMobileFragmentAdapter.SMSNotifier {

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;

    private RegisterMobileFragmentAdapter registerMobileAdapter;
    public static final String OTP_LAUNCH = "OTP_LAUNCH";
    private boolean isOTPLaunch;
    private LinearLayoutManager lm;
    private String mobileNumber;

    private boolean otpVerified = false;

    public static RegisterMobileFragment newInstance(boolean isOTPLaunch) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(OTP_LAUNCH, isOTPLaunch);
        RegisterMobileFragment fragment = new RegisterMobileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOTPLaunch = getArguments().getBoolean(OTP_LAUNCH, false);
        PermissionUtil.requestPermission((BaseActivity) getActivity());
        //setUserAttribute();
    }




    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_mobile;
    }

    @Override
    protected String getFragmentTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    public static String getTagName() {
        return RegisterMobileFragment.class.getSimpleName();
    }

    private void loadStartupBanners() {
        getNetworkManager().GetRequest(RequestIdentifier.STARTUP_BANNER_REQUEST_ID.ordinal(),
                WebServiceConstants.START_UP_BANNER, null, null, this, this, false);
    }

    private void callOTPRequest(String mobileNumber) {
        if (CommonUtility.isValidMobileNumber(mobileNumber.trim(), false, 10, 10)) {

            hideKeyBoard(getView());
            if (getActivity() != null && isAdded()) {
                showProgressDialog(getString(R.string.send_otp), false);
            }

            DataStore.setUserPhoneNUmber(getContext(), mobileNumber);
            Map params = WebServicePostParams.SendOTPParams(mobileNumber);
            getNetworkManager().PutRequest(RequestIdentifier.OTP_REQUEST_ID.ordinal(),
                    WebServiceConstants.SEND_OTP, params, null, this, this, false);
        } else {
            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.invalid_number));
        }
    }

    private void callOTPVerification(String mobile, String otpCode) {
        if (otpCode.length() == AppConstants.OTP_LENGTH) {
            hideKeyBoard(getView());
            showProgressDialog(false);
            Map otpVerifyParams = WebServicePostParams.OTPVerifyParams(mobile, otpCode);
            getNetworkManager().PutRequest(RequestIdentifier.OTP_VERIFICATION_ID.ordinal(),
                    WebServiceConstants.OTP_VERIFY, otpVerifyParams, null, this, this, false);
        } else {
            if (getActivity() != null) {
                SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.wrong_otp));
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadStartupBanners();

        if (registerMobileAdapter == null)
            registerMobileAdapter = new RegisterMobileFragmentAdapter(this, this);


        lm = new LinearLayoutManager(getContext());
        lm.setOrientation( LinearLayoutManager.VERTICAL );
        lm.setAutoMeasureEnabled( true );
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(registerMobileAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        // lm.setStackFromEnd(true);


        if (isOTPLaunch)
            registerMobileAdapter.updateSmsSentStatus(true);


        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        SmsRetrieverClient client = SmsRetriever.getClient(getActivity());
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                MySMSBroadcastReceiver.registerCallback(new MySMSBroadcastReceiver.onSMSRetrieved() {
                    @Override
                    public void onSMSRetrieved(String otp) {
                        Log.d("OTP_","onSMSRetrieved::"+otp+registerMobileAdapter.getMobileNumber());
                        sendOTPVerification(registerMobileAdapter.getMobileNumber(), otp);
                    }
                });

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
            }
        });

    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.OTP_VERIFICATION_ID.ordinal()) {
            if (ErrorHandlerUtility.getAPIErrorData(error) != null && ErrorHandlerUtility.getAPIErrorData(error).message != null)
                showError(ErrorHandlerUtility.getAPIErrorData(error).message, MESSAGETYPE.SNACK_BAR);
        } else if (request.getIdentifier() == RequestIdentifier.OTP_REQUEST_ID.ordinal()) {
            if (apiError != null) {
                if (BaseAuthFragment.OnBoardingSt.SCREEN0.getStatus().equals(apiError.issue)) {
                    AppExitDailog appExitDailog = new AppExitDailog();
                    appExitDailog.showUpgradeDialog(context);
                }
            } else {
                showError();
            }
        } else {
            showError();
        }

        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.STARTUP_BANNER_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), Banner.class, new OnGsonParseCompleteListener<Banner>() {
                        @Override
                        public void onParseComplete(Banner data) {
                            try {
                                registerMobileAdapter.updateBannerList(data.banners);
                            }catch (Exception exception){
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
        if (request.getIdentifier() == RequestIdentifier.OTP_REQUEST_ID.ordinal()) {
            hideProgressDialog();
            if (registerMobileAdapter != null)
                registerMobileAdapter.updateSmsSentStatus(true);
            lm.setStackFromEnd(true);


            CustomLogger.d("OTP Hit Response");
        }

        if (request.getIdentifier() == RequestIdentifier.UPLOAD_CONTACTS_LIST.ordinal()) {
            CustomLogger.d("Upload ContactList Sucessfully");
        }

        if (request.getIdentifier() == RequestIdentifier.OTP_VERIFICATION_ID.ordinal()) {
            CustomLogger.d("OTP Verify Response");
            getGsonHelper().parse(responseObject.toString(), OTPVerification.class, new OnGsonParseCompleteListener<OTPVerification>() {
                @Override
                public void onParseComplete(OTPVerification data) {
                    try {
//                    hideProgressDialog();
                        if (data != null && data.getMobileVerified() == 1) {
                            if (data.getId() != 0)
                                DataStore.setUserId(getContext(), "" + data.getId());
                            DataStore.setUserPhoneNUmber(getContext(), data.getMobile());
                            DataStore.setOTPVerified(getContext(), true);

                            //saving Moengage stats LOGIN
                            MoEHelper.getInstance(getApplicationContext()).setUniqueId(data.getMobile());


                            if (data.getName() != null) {
                                DataStore.setUserName(getContext(), data.getName().toString());
                            }
                            if (data.getEmail() != null) {
                                DataStore.setAppUserEmail(getContext(), data.getEmail().toString());
                            }

            /*
            *
            * Get User Contact List 
            *
            * */
        /*                    if (FeatureMap.isFeatureEnabled(FeatureMapKeys.contact_click_stream)) {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                                    FeatureMap.Feature featureParam = FeatureMap.getFeatureMap(FeatureMapKeys.contact_click_stream);
                                    String contactUrl = "";
                                    if (featureParam.PARAMS != null && !featureParam.PARAMS.isEmpty()) {
                                        JSONObject getPath = new JSONObject(featureParam.PARAMS);
                                        contactUrl = getPath.getString("post_url");
                                    }
                                    final String finalContactUrl = contactUrl;
                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            JsonArray contactList = CommonUtility.getContactsList(context);
                                            Gson arrayList = new Gson();
                                            String json = arrayList.toJson(contactList);
                                            Log.d("contactList",""+json);
                                            sentContactDataToServer(finalContactUrl,json);
                                        }
                                    });

                                } else {
                                    DialogUtil.showAlertDialog(context, true, null, getString(R.string.contact_permission_message), getString(R.string.OK)
                                            , null, null, new DialogListener() {
                                                @Override
                                                public void onPositiveButton(DialogInterface dialog) {
                                                    PermissionUtil.requestContactPermission(context);
                                                }
                                            });
                                }

                            }
*/


                            launchLoginTaskForApplogic();

                            otpVerified = true;

                            loadOnBoardingStatus();

//                        if (forcedBoardingST.equals(actualBoardingST)) {
//                            actualBoardingST = null;
//                            forcedBoardingST = null;
//                            BaseFragment fragment = RegisterBasicDetailsFragment.newInstance();
//                            BaseFragment.removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, RegisterBasicDetailsFragment.getTagName());
//
//                        } else {
//
//                            OnBoardingStatus onBoardingStatus = new OnBoardingStatus();
//                            onBoardingStatus.actual_status = actualBoardingST;
//                            onBoardingStatus.forced_status = "";
//                            onBoardingStatus.storeId = storeID;
//                            OnBoardingUtility.OnBoardFragments onBoardFragment = OnBoardingUtility.onBoardingNavigation(onBoardingStatus);
//                            removeTopAndAddToBackStack((BaseActivity) getActivity(), onBoardFragment.fragment, onBoardFragment.tagName);
//                            actualBoardingST = null;
//                            forcedBoardingST = null;
//
//                        }


                        } else {

                            hideProgressDialog();
                            showError();
                        }
                    }catch (Exception exception){
                        CustomLogger.e("Exception ", exception);
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    hideProgressDialog();
                    CustomLogger.e("Exception ", exception);
                }
            });

        }
        if (request.getIdentifier() == RequestIdentifier.ON_BOARDING_ST_REQ_ID.ordinal()) {

            hideProgressDialog();

            getGsonHelper().parse(responseObject.toString(), OnBoardingStatus.class, new OnGsonParseCompleteListener<OnBoardingStatus>() {


                        @Override
                        public void onParseComplete(OnBoardingStatus data) {
                            try {
                                OnBoardingUtility.OnBoardFragments fragment = OnBoardingUtility.onBoardingNavigation(context, data);
                                if (fragment.tagName.toLowerCase().contains("buydealdetailfragment") || fragment.tagName.toLowerCase().contains("productdetailfragment")
                                        || fragment.tagName.toLowerCase().contains("selldealfragment") || fragment.tagName.toLowerCase().contains("selldealdetailfragment")) {
                                    BaseFragment.replaceFragment(getActivity().getSupportFragmentManager(),
                                            R.id.container, HomeFragment.newInstance(), true, true, HomeFragment.getTagName());
                                }
                                removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment.fragment, fragment.tagName);
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }
                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );
            return true;
        }

        return true;
    }

    private void sentContactDataToServer(String contactUrl, String contactList)
    {
        Map<String, Object> params =new HashMap<>();
        params.put("mobile",""+DataStore.getUserPhoneNumber(context));
        params.put("IMEI",""+CommonUtility.getIEMINumber(context));
        params.put("SIM_Type","Primary");
        params.put("contacts",contactList);
        params.put("email",""+DataStore.getUserEmail(context));
        params.put("app_type","app");

        getNetworkManager().PostRequestContactAPI(RequestIdentifier.UPLOAD_CONTACTS_LIST.ordinal(),
                contactUrl, params, null, this, this, true);

    }

    private void loadOnBoardingStatus() {

        Map requestParamsMap = WebServicePostParams.onBoardingParams();
        getNetworkManager().GetRequest(RequestIdentifier.ON_BOARDING_ST_REQ_ID.ordinal(),
                WebServiceConstants.ON_BOARDING_STATUS, requestParamsMap, null, this, this, false);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(IntentConstants.OTP));
        super.onResume();
        PartsEazyEventBus.getInstance().addObserver(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        PartsEazyEventBus.getInstance().removeObserver(this);
    }

    @Override
    protected void updateOTPView(String otpCode) {
        super.updateOTPView(otpCode);
        registerMobileAdapter.updateOTPView(otpCode);

    }


    @Override
    public void sendOTPRequest(String mobileNumber) {
        CustomLogger.d("Mobile number " + mobileNumber);
        callOTPRequest(mobileNumber);
    }

    @Override
    public void sendOTPVerification(String mobileNumber, String OTP) {
        if (!otpVerified)
            callOTPVerification(mobileNumber, OTP);

    }

    @Override
    public boolean onBackPressed() {
        CommonUtility.exitApp(getActivity());
        return false;
    }
}

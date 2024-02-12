package com.partseazy.android.ui.fragments.registration;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.moe.pushlibrary.MoEHelper;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.registration.StoreAreaAdapter;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealDetailFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealFragment;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.registration.StoreSize;
import com.partseazy.android.ui.model.registration.banner.StoreData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.error.ErrorHandlerUtility;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;


/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class RegisterBasicDetailsFragment extends BaseFragment {

    @BindView(R.id.shopET)
    protected EditText shopET;
    @BindView(R.id.inviteCodeET)
    protected EditText inviteCodeET;
    @BindView(R.id.nameET)
    protected EditText nameET;
    @BindView(R.id.emailET)
    protected EditText emailET;
    @BindView(R.id.pincodeET)
    protected EditText pincodeET;
    @BindView(R.id.companyNameET)
    protected EditText companyNameET;
    @BindView(R.id.inviteCodeTIL)
    protected TextInputLayout inviteCodeTIL;
    @BindView(R.id.shopTIL)
    protected TextInputLayout shopTIL;
    @BindView(R.id.nameTIL)
    protected TextInputLayout nameTIL;
    @BindView(R.id.emailTIL)
    protected TextInputLayout emailTIL;
    @BindView(R.id.pincodeTIL)
    protected TextInputLayout pincodeTIL;
    @BindView(R.id.companyNameTIL)
    protected TextInputLayout companyNameTIL;
    @BindView(R.id.reyclicView)
    protected RecyclerView recyclerView;
    @BindView(R.id.inviteIV)
    protected ImageView inviteIV;
    @BindView(R.id.continue_regsiter_priceBT)
    protected Button continue_regsiter_priceBT;

    @BindView(R.id.shopSizeTV)
    protected TextView shopSizeTV;
    @BindView(R.id.inviteCodeLL)
    protected LinearLayout inviteCodeLL;

    @BindView(R.id.registerScroll)
    protected NestedScrollView mRegisterScroll;

    private StoreAreaAdapter storeAreaAdapter;
    private String inviteCode, name, shopName, email, pincode, errorMessage, companyName;
    private String storeType = "";
    private List<StoreSize> storeSizeList;
    private ArrayList<String> roles;

    private MoEHelper helper =null;

    private final static String INVITE = "invite";
    private final static String ROLES = "roles";

    public static RegisterBasicDetailsFragment newInstance(ArrayList<String> roles) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(RegisterBasicDetailsFragment.ROLES, roles);
        RegisterBasicDetailsFragment fragment = new RegisterBasicDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roles = getArguments().getStringArrayList(RegisterBasicDetailsFragment.ROLES);
        helper = MoEHelper.getInstance(getContext());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_details;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.provide_basic_details);
    }

    public static String getTagName() {
        return RegisterBasicDetailsFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initCollapsingToolbar(view, getString(R.string.provide_basic_details), getString(R.string.great));
        toolbar.setTitle(getString(R.string.provide_basic_details));
        storeSizeList = getStoreSize();
        if (storeSizeList != null && storeSizeList.size() > 0) {
            setStoreAreaAdapter();
        }

        mRegisterScroll.fullScroll(View.FOCUS_DOWN);

        if (roles.contains("retailer")) {
            shopTIL.setVisibility(View.VISIBLE);
            shopSizeTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (roles.contains("distributor") || roles.contains("wholesaler")) {
            companyNameTIL.setVisibility(View.VISIBLE);
        }

        handleClick();
        return view;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.USER_REGISTRATION_ID.ordinal()) {

            if (ErrorHandlerUtility.getAPIErrorData(error).message != null) {

                if (ErrorHandlerUtility.getAPIErrorData(error).issue.contains(INVITE)) {
                    if (getActivity() != null) {
                        hideKeyBoard(getView());
                        inviteCodeLL.setVisibility(View.VISIBLE);
                        DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.app_is_in_alpha_version), getString(R.string.OK)
                                , null, null, new DialogListener() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                    }
                                });
                    }
                } else {
                    showError(ErrorHandlerUtility.getAPIErrorData(error).message, MESSAGETYPE.SNACK_BAR);
                }
            } else {
                showError();
            }
        }
        return true;
    }

    private void setStoreAreaAdapter() {
        if (storeAreaAdapter == null)
            storeAreaAdapter = new StoreAreaAdapter(this, storeSizeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(storeAreaAdapter);
    }


    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.USER_REGISTRATION_ID.ordinal()) {
            CustomLogger.d("Retailer details api success");
            getGsonHelper().parse(responseObject.toString(), StoreData.class, new OnGsonParseCompleteListener<StoreData>() {
                        @Override
                        public void onParseComplete(StoreData data) {
                            try {
                                hideProgressDialog();
                                DataStore dataStore = new DataStore();
                                if (data.user.id != 0)
                                    dataStore.setUserId(context, "" + data.user.id);
                                dataStore.setUserName(context, data.user.name);
                                dataStore.setAppUserEmail(context, data.user.email);
                                if (data.shop != null) {
                                    dataStore.setUserShopId(context, data.shop.id + "");
                                }

                                // register at applozic
                                launchLoginTaskForApplogic();

                                BaseFragment fragment;

                                if (DataStore.getProductId(context) != null && !DataStore.getProductId(context).equals("")) {
                                    fragment = ProductDetailFragment.newInstance(Integer.parseInt(DataStore.getProductId(context)), "");
                                    //fragment = HomeFragment.newInstance();
                                    DataStore.setProductId(context, null);
                                } else if ("1".equals(DataStore.getGoToDeals(context))) {
                                  fragment = DealHomeFragment.newInstance();
                               //     fragment = HomeFragment.newInstance();
                                    DataStore.setGoToDeals(context, "0");
                                } else if ("2".equals(DataStore.getGoToDeals(context))) {
                                   fragment = DealHomeFragment.newInstance(true);
                                    //    fragment = HomeFragment.newInstance();
                                    DataStore.setGoToDeals(context, "0");
                                } else if (DataStore.getGoToReport(context)) {
                                    fragment = SellDealFragment.newInstance(true);
                                    //  fragment = HomeFragment.newInstance();
                                    DataStore.setGoToReport(context, false);
                                } else if (DataStore.getDealId(context) != null && !DataStore.getDealId(context).equals("")) {
                                    fragment = BuyDealDetailFragment.newInstance(Integer.parseInt(DataStore.getDealId(context)), "");
                                    //fragment = HomeFragment.newInstance();
                                    DataStore.setDealId(context, null);
                                } else {
                                    if (DataStore.getGoToChat(context)) {
                                        DataStore.setGoToChat(context, false);
                                        Intent intent = new Intent(context, ConversationActivity.class);
                                        if (ApplozicClient.getInstance(context).isContextBasedChat()) {
                                            intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
                                        }
                                        context.startActivity(intent);
                                    }
                                  //  fragment = DealHomeFragment.newInstance();
                                    fragment = HomeFragment.newInstance();
                                }

                                BaseFragment.removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, fragment.getTag());
//                            RegisterL2SegmentFragment fragment = RegisterL2SegmentFragment.newInstance(data.shop.id);
//                            removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, RegisterL2SegmentFragment.getTagName());
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }
                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            showError();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }
        return true;
    }

    private void registerRetailerDetails() {
        showProgressDialog(getString(R.string.please_wait), false);
        Map params = WebServicePostParams.retailerParams(name, shopName, email, storeType, pincode, inviteCode, companyName);
        //Moengage basic details
        setUserAttribute();
        getNetworkManager().PutRequest(RequestIdentifier.USER_REGISTRATION_ID.ordinal(),
                WebServiceConstants.USER_REGISTRATION, params, null, this, this, false);
    }

    private void setUserAttribute(){
        helper.setUserAttribute(MoengageConstant.User.NAME,name);
        helper.setUserAttribute(MoengageConstant.User.EMAIL,email);
        helper.setUserAttribute(MoengageConstant.User.PINCODE,pincode);
        helper.setUserAttribute(MoengageConstant.User.SHOPNAME,shopName);
        helper.setUserAttribute(MoengageConstant.User.SHOP_AREA ,storeType);
        // If you have first and last name separately
        helper.setFirstName(name);
        // If you have full name
        helper.setEmail(email);

    }

    public void handleClick() {

        String mobileEmail = CommonUtility.getEmailFromMobile(getContext());
        if (mobileEmail != null) {
            emailET.setText(mobileEmail);
        }

        /** Uncomment this code when you want autofill of invitation code to work **/
//        if (DataStore.getInvitationCode(context) != null && !DataStore.getInvitationCode(context).equals("")) {
//            inviteCodeET.setText(DataStore.getInvitationCode(context));
//            inviteCodeLL.setVisibility(View.GONE);
//        }
        inviteCodeET.setText(R.string.invite_code);
        inviteCodeLL.setVisibility(View.GONE);

        inviteCodeET.addTextChangedListener(new MyTextWatcher(inviteCodeET));
        nameET.addTextChangedListener(new MyTextWatcher(nameET));
        shopET.addTextChangedListener(new MyTextWatcher(shopET));
        emailET.addTextChangedListener(new MyTextWatcher(emailET));
        pincodeET.addTextChangedListener(new MyTextWatcher(pincodeET));
        companyNameET.addTextChangedListener(new MyTextWatcher(companyNameET));
        continue_regsiter_priceBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteCode = inviteCodeET.getText().toString().trim();
                name = nameET.getText().toString().trim();
                shopName = shopET.getText().toString().trim();
                email = emailET.getText().toString().trim();
                pincode = pincodeET.getText().toString().trim();
                companyName = companyNameET.getText().toString().trim();
                if (isValidInformation(name, shopName, email, pincode, inviteCode, companyName)) {
                    registerRetailerDetails();
                }
            }
        });

        inviteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    hideKeyBoard(getView());
                    DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.app_is_in_alpha_version), getString(R.string.OK)
                            , null, null, new DialogListener() {
                                @Override
                                public void onPositiveButton(DialogInterface dialog) {
                                }
                            });
                }
            }
        });
    }


    private boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            nameTIL.setError(getString(R.string.err_name_msg));
            requestFocus(nameET);
            return false;
        } else {
            nameTIL.setError(null);
            nameTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateInviteCode(String inviteCode) {
        if (inviteCode == null || inviteCode.isEmpty()) {
            inviteCodeTIL.setError(getString(R.string.err_invite_code_msg));
            requestFocus(inviteCodeET);
            return false;
        } else {
            inviteCodeTIL.setError(null);
            inviteCodeTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateShopName(String shopName) {
        if (shopName == null || shopName.isEmpty()) {
            shopTIL.setError(getString(R.string.err_shop_msg));
            requestFocus(shopET);
            return false;
        } else {
            shopTIL.setError(null);
            shopTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePincode(String pincode) {
        if (pincode == null || pincode.isEmpty() || !CommonUtility.isValidPincode(pincode)) {
            pincodeTIL.setError(getString(R.string.err_pincode_msg));
            requestFocus(pincodeET);
            return false;
        } else {
            pincodeET.setError(null);
            pincodeTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty() || !CommonUtility.isValidEmail(email)) {
            emailTIL.setError(getString(R.string.err_email_msg));
            requestFocus(emailET);
            return false;
        } else {
            emailTIL.setError(null);
            emailTIL.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCompanyName(String companyName) {
        if (companyName == null || companyName.isEmpty()) {
            companyNameTIL.setError(getString(R.string.err_company_name));
            requestFocus(companyNameET);
            return false;
        } else {
            companyNameTIL.setError(null);
            companyNameTIL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidInformation(String name, String shopname, String email, String pincode, String inviteCode, String companyName) {
        if (!validateInviteCode(inviteCode)) {
            inviteCodeLL.setVisibility(View.VISIBLE);
            return false;
        }
        if (TextUtils.isEmpty(email))
            email ="Parts_"+getRandomEMailString()+"@partseazy.com";

        if (roles.contains("distributor") || roles.contains("wholesale")) {
            if (!validateCompanyName(companyName)) {
                return false;
            }
        }
        if (!validateName(name)) {
            return false;
        }
        if (!validateEmail(email)) {
            return false;
        }
        if (!validatePincode(pincode)) {
            return false;
        }
        if (roles.contains("retailer")) {
            if (storeType == null || storeType.equals("")) {
                showMessage(getString(R.string.select_shop_area), MESSAGETYPE.SNACK_BAR);
                return false;
            }
            if (shopTIL.getVisibility() == View.VISIBLE && !validateShopName(shopname)) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<StoreSize> getStoreSize() {
        ArrayList<StoreSize> storeList = new ArrayList<>();

        storeList.add(new StoreSize("xs", StaticMap.size_xs));
        storeList.add(new StoreSize("s", StaticMap.size_s));
        storeList.add(new StoreSize("m", StaticMap.size_m));
        storeList.add(new StoreSize("l", StaticMap.size_l));
        storeList.add(new StoreSize("xl", StaticMap.size_xl));
        storeList.add(new StoreSize("xxl", StaticMap.size_xxl));

        return storeList;
    }

    @Override
    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.SEND_STORE_SIZE) {
            StoreSize store = (StoreSize) eventObject.objects[0];
            storeType = store.sizeType;
            CustomLogger.d("Store id selected " + store.sizeType);

        }

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private EditText view;

        public MyTextWatcher(EditText view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inviteCodeET:
                    String inviteCode = inviteCodeET.getText().toString().trim();
                    validateInviteCode(inviteCode);
                    break;
                case R.id.emailET:
                    String email = emailET.getText().toString().trim();
                    if (TextUtils.isEmpty(email))
                        email ="Parts_"+getRandomEMailString()+"@partseazy.com";
                    validateEmail(email);
                    break;
                case R.id.nameET:
                    String name = nameET.getText().toString().trim();
                    validateName(name);
                    break;
                case R.id.shopET:
                    String shopName = shopET.getText().toString().trim();
                    validateShopName(shopName);
                    break;
                case R.id.pincodeET:
                    String pinCode = pincodeET.getText().toString().trim();
                    validatePincode(pinCode);
                    break;
                case R.id.companyNameET:
                    String companyName = companyNameET.getText().toString().trim();
                    validateCompanyName(companyName);
                    break;
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        CommonUtility.exitApp(getActivity());
        return false;
    }


    protected String getRandomEMailString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}


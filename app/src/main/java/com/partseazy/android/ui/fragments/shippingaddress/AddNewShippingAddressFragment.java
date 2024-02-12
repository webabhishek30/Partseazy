package com.partseazy.android.ui.fragments.shippingaddress;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.AddAddress;
import com.partseazy.android.ui.model.shippingaddress.CityDetail;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by can on 22/12/16.
 */

public class AddNewShippingAddressFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.deliveryOptionBtn)
    Button deliveryOptionBtn;
    @BindView(R.id.submit_newaddressBT)
    Button submit_newaddressBT;
    @BindView(R.id.mobileno_addressET)
    TextView mobileno_addressET;
    @BindView(R.id.pincodeET)
    TextView pincodeET;
    @BindView(R.id.deliveryNameET)
    TextView deliveryNameET;
    @BindView(R.id.new_addressET)
    TextView new_addressET;
    @BindView(R.id.lankmarkET)
    TextView lankmarkET;
    @BindView(R.id.cityET)
    TextView cityET;
    @BindView(R.id.stateET)
    TextView stateET;
    @BindView(R.id.pincodeTIL)
    TextInputLayout pincodeTIL;
    @BindView(R.id.deliveryNameTIL)
    TextInputLayout deliveryNameTIL;
    @BindView(R.id.mobileno_addressTIL)
    TextInputLayout mobileno_addressTIL;
    @BindView(R.id.new_addressTIL)
    TextInputLayout new_addressTIL;
    @BindView(R.id.homeRadioBtn)
    RadioButton homeRadioBtn;
    @BindView(R.id.shopRadioBtn)
    RadioButton shopRadioBtn;
    @BindView(R.id.officeRadioBtn)
    RadioButton officeRadioBtn;
    @BindView(R.id.deliveryRadioGrp)
    RadioGroup deliveryRadioGrp;


    @BindView(R.id.billingAddressCB)
    protected CheckBox billingAddressCB;

    @BindView(R.id.billingLL)
    protected LinearLayout billingLL;

    @BindView(R.id.topBillNameTIL)
    protected TextInputLayout topBillNameTIL;

    @BindView(R.id.topBillNameET)
    protected EditText topBillNameET;

    @BindView(R.id.topBillGSTTIL)
    protected TextInputLayout topBillGSTTIL;

    @BindView(R.id.topBillGSTET)
    protected EditText topBillGSTET;

    @BindView(R.id.bottomBillNameTIL)
    protected TextInputLayout bottomBillNameTIL;

    @BindView(R.id.bottomBillNameET)
    protected EditText bottomBillNameET;

    @BindView(R.id.bottomGSTTIL)
    protected TextInputLayout bottomGSTTIL;

    @BindView(R.id.bottomGSTET)
    protected EditText bottomGSTET;



    public static final String LAUNCH_FROM = "launchFrom";
    public static final String FROM_CART ="isFromCart";
    public static final String FROM_BOOKING ="isFromBooking";
    public static final String FROM_BILLING ="isFromBilling";

    private boolean isPinCodeVaid;


    private String  city, state, deliveryOption;
    private String launchScreenName;
    private AddressModel addressModel;
    private boolean isSameBillingAdd=false;

    public static AddNewShippingAddressFragment newInstance(String launchFrom ) {
        Bundle bundle = new Bundle();
        bundle.putString(LAUNCH_FROM,launchFrom);
        AddNewShippingAddressFragment fragment = new AddNewShippingAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_new_address;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.add_newaddress);
    }

    public static String getTagName() {
        return AddNewShippingAddressFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PartsEazyEventBus.getInstance().postEvent(EventConstant.SHIPPING_ADDRESS_ADDED, true);
        launchScreenName = getArguments().getString(LAUNCH_FROM);
        addressModel = new AddressModel();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        setAddressViewField();
        deliveryOptionBtn.setOnClickListener(this);
        submit_newaddressBT.setOnClickListener(this);
        billingAddressCB.setOnClickListener(this);

       // if (DataStore.getUserName(getActivity()) != null)
         //   deliveryNameET.setText(DataStore.getUserName(getActivity()));

//        if (DataStore.getPincode(getActivity()) != null)
     //   pincodeET.setText(DataStore.getPincode(getActivity()));

       // if (DataStore.getUserPhoneNumber(getActivity()) != null)
           // mobileno_addressET.setText(DataStore.getUserPhoneNumber(getActivity()));


        pincodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pincodeTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    String pincode = s.toString();
                    callPincodeAPI(pincode);
                }
            }

        });

        deliveryNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                deliveryNameTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        mobileno_addressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobileno_addressTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        new_addressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new_addressTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        topBillNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                topBillNameTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bottomBillNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bottomBillNameTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bottomGSTET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bottomGSTTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        topBillGSTET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                topBillGSTTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        deliveryRadioGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == homeRadioBtn.getId()) {
                    deliveryOption = getString(R.string.home);
                } else if (checkedId == shopRadioBtn.getId()) {
                    deliveryOption = getString(R.string.shop);

                } else if (checkedId == officeRadioBtn.getId()) {
                    deliveryOption = getString(R.string.office);
                }
                CustomLogger.d("deliveryOption ::" + deliveryOption);
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        toolbar.setTitle(R.string.add_newaddress);
        return view;
    }


//    private void showDeliveryDialog() {
//        String[] optionName = {"Home", "Shop", "Office"}; //TODO REMOVE THESE...
//        Dialog dialog = new Dialog(getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_delivery_option);
//        RecyclerView scrollable = (RecyclerView) dialog.findViewById(R.id.scrollable);
//        scrollable.setLayoutManager(new LinearLayoutManager(getContext()));
//        ShippingDialogAdapter shippingDialogAdapter = new ShippingDialogAdapter(this, optionName);
//        scrollable.setAdapter(shippingDialogAdapter);
//        scrollable.setHasFixedSize(true);
//        dialog.show();
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deliveryOptionBtn:
                // showDeliveryDialog();
                break;
            case R.id.submit_newaddressBT:
                if (checkValidBillingAddress()) {
                    callAddAddressAPIRequest();
                }
                break;

            case R.id.billingAddressCB:
                if(billingAddressCB.isChecked())
                {
                    billingLL.setVisibility(View.VISIBLE);
                }else{
                    billingLL.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void removeErrorFromInputLayout(TextInputLayout textInputLayout) {

    }


    void setAddressViewField()
    {
        if(launchScreenName.equals(FROM_BILLING))
        {
            billingAddressCB.setVisibility(View.GONE);
            billingLL.setVisibility(View.GONE);
            deliveryNameTIL.setVisibility(View.GONE);
            topBillNameTIL.setVisibility(View.VISIBLE);
            topBillGSTTIL.setVisibility(View.VISIBLE);
        }
        if(launchScreenName.equals(FROM_CART))
        {
            billingAddressCB.setVisibility(View.VISIBLE);
        }

        if(launchScreenName.equals(FROM_BOOKING))
        {
            billingAddressCB.setVisibility(View.GONE);
            billingLL.setVisibility(View.GONE);
            topBillNameTIL.setVisibility(View.GONE);
            topBillGSTTIL.setVisibility(View.GONE);
        }

    }

    void callAddAddressAPIRequest() {
        if (CommonUtility.isValidMobileNumber(addressModel.mobileNo.trim(), false, 10, 10)) {

            if (deliveryRadioGrp.getCheckedRadioButtonId() == -1) {
                showError("Select Address option", MESSAGETYPE.TOAST);

                return;

            }

            if (!isPinCodeVaid) {

                showError(getActivity().getString(R.string.inavlid_pincode), MESSAGETYPE.TOAST);
                return;
            }

            isPinCodeVaid = false;

            showProgressDialog(getString(R.string.loading), false);
            Map params = WebServicePostParams.sendAddressParam(addressModel);

            getNetworkManager().PostRequest(RequestIdentifier.ADD_ADDRESS_ID.ordinal(),
                    WebServiceConstants.POST_NEW_ADDRESS, params, null, this, this, false);
        } else {
            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.invalid_number));
        }
    }

    void callPincodeAPI(String pincode) {
        showProgressDialog();
        String completeUrl = WebServiceConstants.GET_CITY + pincode;
        getNetworkManager().GetRequest(RequestIdentifier.GET_CITY_ID.ordinal(),
                completeUrl, null, null, this, this, false);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();

        KeyPadUtility.hideSoftKeypad(getActivity());
        CustomLogger.d("Exception ", error);


        if (request.getIdentifier() == RequestIdentifier.GET_CITY_ID.ordinal()) {
            if (getActivity() != null)
                showError(getActivity().getString(R.string.inavlid_pincode), MESSAGETYPE.TOAST);
            pincodeTIL.setError("Please try another pincode");


        }
        if (request.getIdentifier() == RequestIdentifier.ADD_ADDRESS_ID.ordinal()) {

            if (apiError != null)
                showError(apiError.message + " " + apiError.issue, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }

        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.ADD_ADDRESS_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), AddAddress.class, new OnGsonParseCompleteListener<AddAddress>() {
                        @Override
                        public void onParseComplete(AddAddress data) {
                            try {
                                hideProgressDialog();
                                popBackStack(getFragmentManager());
                                if (isSameBillingAdd) {
                                    ShippingAddressFragment checkoutFragment = ShippingAddressFragment.newInstance(true);
                                    if (getActivity() != null)
                                        addToBackStack((BaseActivity) getActivity(), checkoutFragment, checkoutFragment.getTagName());
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            KeyPadUtility.hideSoftKeypad(getActivity());
                            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.err_somthin_wrong));
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        if (request.getIdentifier() == RequestIdentifier.GET_CITY_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), CityDetail.class, new OnGsonParseCompleteListener<CityDetail>() {
                @Override
                public void onParseComplete(CityDetail getCity) {
                    try {
                        hideProgressDialog();
                        hideKeyBoard(getView());
                        addressModel.cityId = getCity.cityId;
                        addressModel.stateId = getCity.stateId;
                        city = getCity.city;
                        state = getCity.state;
                        cityET.setText(city);
                        stateET.setText(state);
                        isPinCodeVaid = true;
                    }catch (Exception exception){
                        CustomLogger.e("Exception ", exception);
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    hideProgressDialog();
                    KeyPadUtility.hideSoftKeypad(getActivity());
                    pincodeTIL.setError(getString(R.string.change_pincode));
                    isPinCodeVaid = false;
//                    SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.change_pincode));
                }


            });
        }

        return true;
    }

    boolean getValuesFromViews() {
        boolean isValidData = true;

        addressModel.deliveryOption = deliveryOption;

        if (mobileno_addressET.getText().toString().trim().length() > 0) {
            addressModel.mobileNo = mobileno_addressET.getText().toString();

        } else {
            isValidData = false;
            mobileno_addressTIL.setError(getString(R.string.enter_valid_mobile));
        }
        if(!launchScreenName.equals(FROM_BILLING)) {
            if (deliveryNameET.getText().toString().trim().length() > 0) {
                addressModel.deliveryName = deliveryNameET.getText().toString();
            } else {
                isValidData = false;
                deliveryNameTIL.setError(getString(R.string.enter_name));
            }
        }
        if (pincodeET.getText().toString().trim().length() > 0 && pincodeET.getText().length() == 6) {
            addressModel.pincode = pincodeET.getText().toString();
        } else {
            isValidData = false;
            pincodeTIL.setError(getString(R.string.enter_pincode));
        }
        if (new_addressET.getText().toString().trim().length() > 0) {
            addressModel.address = new_addressET.getText().toString();
        } else {
            isValidData = false;
            new_addressTIL.setError(getString(R.string.enter_address));
        }
        if (lankmarkET.getText().toString().trim().length() > 0)
            addressModel.landmark = lankmarkET.getText().toString();


        return isValidData;

    }

    private boolean checkValidBillingAddress()
    {
        final boolean[] isValidData = {true};

        isValidData[0] = getValuesFromViews();

        if(launchScreenName.equals(FROM_BILLING))
        {
            String GSTINNumber = topBillGSTET.getText().toString().trim();
            String billingName = topBillNameET.getText().toString().trim();

            if (billingName!=null && billingName.length()>0) {
                addressModel.billingName = topBillNameET.getText().toString();
            }else{
                isValidData[0] = false;
                topBillNameTIL.setError(getString(R.string.enter_billing_name));
            }

            if (GSTINNumber!=null && GSTINNumber.length()> 0) {
                if(CommonUtility.isValidGSTIN(GSTINNumber)) {
                    addressModel.GSTN = GSTINNumber;
                }else{
                    topBillGSTTIL.setError(getString(R.string.invalid_gstin));
                    isValidData[0] =false;
                }
            }else{
                if (getActivity() != null && isAdded() && isValidData[0]) {
                    callAddAddressAPIRequest();
//                    DialogUtil.showAlertDialog(getActivity(), true, null, context.getString(R.string.gst_message), context.getString(R.string.agree)
//                            , context.getString(R.string.no), null, new DialogListener() {
//                                @Override
//                                public void onPositiveButton(DialogInterface dialog) {
//                                    KeyPadUtility.hideSoftKeypad(getActivity());
//                                    if(isValidData[0]){
//                                        callAddAddressAPIRequest();
//                                    }
//
//                                }
//                            });
                }
                return false;
            }


        }else if(launchScreenName.equals(FROM_CART)) {
            if(billingAddressCB.isChecked()) {
                String GSTINNumber = bottomGSTET.getText().toString().trim();
                if (bottomBillNameET.getText().toString().trim().length() > 0) {
                    addressModel.billingName = bottomBillNameET.getText().toString();
                } else {
                    isValidData[0] = false;
                    bottomBillNameTIL.setError(getString(R.string.enter_billing_name));
                }

                if (GSTINNumber.length() > 0) {
                    if(CommonUtility.isValidGSTIN(GSTINNumber)) {
                        addressModel.GSTN = GSTINNumber;
                    }else{
                        isValidData[0] = false;
                        bottomGSTTIL.setError(getString(R.string.invalid_gstin));
                    }
                }else{
                    if (context != null && isValidData[0]) {
                        DialogUtil.showAlertDialog((BaseActivity)context, true, null, context.getString(R.string.gst_message), context.getString(R.string.agree)
                                , context.getString(R.string.no), null, new DialogListener() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {

                                        KeyPadUtility.hideSoftKeypad((BaseActivity)context);
                                        if(isValidData[0]) {
                                            isSameBillingAdd= true;
                                            callAddAddressAPIRequest();
                                        };

                                    }
                                });
                    }
                    return false;
                }

                if(bottomBillNameET.getText().toString().trim().length()>0 && GSTINNumber.length()>0)
                {
                    isSameBillingAdd = true;
                }
            }
        }else if(launchScreenName.equals(FROM_BOOKING))
        {
            isValidData[0] = getValuesFromViews();
        }

        return isValidData[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        PartsEazyEventBus.getInstance().addObserver(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        PartsEazyEventBus.getInstance().removeObserver(this);
    }


    public class AddressModel{
        public String deliveryName;
        public String mobileNo;
        public String address;
        public String landmark;
        public int cityId;
        public int stateId;
        public String pincode;
        public String deliveryOption;

        public String GSTN;
        public String billingName;


        public AddressModel()
        {

        }

    }
}

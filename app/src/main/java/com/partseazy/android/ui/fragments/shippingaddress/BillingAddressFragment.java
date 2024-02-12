package com.partseazy.android.ui.fragments.shippingaddress;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.IntentConstants;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.shipping.ShippingAddressAdapter;
import com.partseazy.android.ui.fragments.cart_checkout.CartCheckoutBaseFragment;
import com.partseazy.android.ui.fragments.checkout.CheckoutFragment;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.AddAddress;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressList;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

/**
 * Created by naveen on 10/7/17.
 */

public class BillingAddressFragment extends CartCheckoutBaseFragment implements View.OnClickListener {


    @BindView(R.id.scrollable)
    RecyclerView scrollable;
    @BindView(R.id.add_addressBtn)
    TextView add_addressBtn;
    @BindView(R.id.checkout_cartBT)
    Button checkout_cartBT;

    @BindView(R.id.firstLineView)
    protected View firstLineView;

    @BindView(R.id.secondIV)
    protected ImageView secondIV;

    @BindView(R.id.saved_addressTV)
    protected TextView saved_addressTV;

    @BindView(R.id.bookingProgressBarLL)
    protected LinearLayout bookingProgressBarLL;


    private ShippingAddressAdapter addressAdapter;
    private ArrayList<ShippingAddressDetail> addressList;
    private int addressPosition = -1;
    private int preSelectedAddressIndex;
    private static boolean isAddressAdded;

    public final static String SHIPPING_ADD = "shipping_address";
    public final static String SHIPPING_ADD_INDEX = "shipping_address_index";
    public final static String PRE_SELECTED_ADD_INDEX = "pre_selected_address_index";
    public final static String PAYMENT_INDEX = "PAYMENT_INDEX";

    private ShippingAddressDetail shippingAddressDetails;
    private int selectedIndex;
    private String pincode;
    private boolean isNoSelected = false;

    public static BillingAddressFragment newInstance() {
        Bundle bundle = new Bundle();
        BillingAddressFragment fragment = new BillingAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BillingAddressFragment newInstance(ShippingAddressDetail addressDetails, int index) {
        Bundle bundle = new Bundle();
        BillingAddressFragment fragment = new BillingAddressFragment();
        bundle.putSerializable(SHIPPING_ADD, (Serializable) addressDetails);
        bundle.putInt(SHIPPING_ADD_INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shipping_address;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.billing_address);
    }


    public static String getTagName() {
        return ShippingAddressFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(IntentConstants.OTP));
        selectedIndex = getArguments().getInt(SHIPPING_ADD_INDEX);
        shippingAddressDetails = (ShippingAddressDetail) getArguments().getSerializable(SHIPPING_ADD);
        preSelectedAddressIndex = getArguments().getInt(PRE_SELECTED_ADD_INDEX);
        pincode = shippingAddressDetails.pincode;
        finalCartAttributes.addressId = "" + shippingAddressDetails.id;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        setProgressView();
        scrollable.setLayoutManager(new LinearLayoutManager(getContext()));
        shippingAddressListRequest();
        add_addressBtn.setOnClickListener(this);
        checkout_cartBT.setOnClickListener(this);

    }

    public void setProgressView() {
        saved_addressTV.setText(getString(R.string.select_billing_address));
        secondIV.setImageResource(R.drawable.check_green_circle);
        firstLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));

        bookingProgressBarLL.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void initAdapter(ArrayList<ShippingAddressDetail> addressDetails, int preSelectedAddressIndex) {

        checkout_cartBT.setEnabled(true);
        scrollable.setHasFixedSize(true);
        scrollable.setNestedScrollingEnabled(false);

        if (addressAdapter == null) {
            addressAdapter = new ShippingAddressAdapter(context, addressDetails, preSelectedAddressIndex, true);
            scrollable.setAdapter(addressAdapter);
        } else {
            scrollable.setAdapter(addressAdapter);
            addressAdapter.notifyAdapter(addressDetails, preSelectedAddressIndex);
        }


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        AddNewShippingAddressFragment fragment = (AddNewShippingAddressFragment) getFragmentManager().findFragmentByTag(AddNewShippingAddressFragment.getTagName());

        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_addressBtn:
                BaseFragment fragment = AddNewShippingAddressFragment.newInstance(AddNewShippingAddressFragment.FROM_BILLING);
                BaseFragment.addToBackStack((BaseActivity) getActivity(), fragment, AddNewShippingAddressFragment.getTagName());
                fragment.setTargetFragment(this, 1);
                break;

            case R.id.checkout_cartBT:
                callCheckoutRequest();
                break;

            default:
                break;
        }
    }

    private void callCheckoutRequest() {

        if (addressList == null)
            return;

        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).selectedItem == true) {
                addressPosition = i;
            }
        }

        if (addressPosition != -1) {
            //DataStore.setPincode(context, addressList.get(addressPosition).pincode);
            ShippingAddressDetail selectedAddDetail = addressList.get(addressPosition);
            if (selectedAddDetail.billingName != null && !selectedAddDetail.billingName.equals("")) {
                if (isNoSelected || selectedAddDetail.gstn != null) {
                    callCartCheckoutRequest(finalCartAttributes);
                } else {
                    showEditGSTINDailog(addressList.get(addressPosition));
                }
            } else {
                showEditGSTINDailog(addressList.get(addressPosition));
            }
        } else
            SnackbarFactory.showSnackbar(getActivity(), getString(R.string.error_fill_address));

    }

    @Override
    protected boolean handleCartCheckoutResponse(CartCheckoutBaseData cartCheckoutBaseData) {

        // checkForShipeableItem(cartCheckoutBaseData, addressDetails.get(addressPosition), addressPosition, false);

        context.onPopBackStack(true);

        CheckoutFragment checkoutFragment = CheckoutFragment.newInstance(shippingAddressDetails, selectedIndex, addressList.get(addressPosition), addressPosition);
        if (getActivity() != null)
            addToBackStack((BaseActivity) getActivity(), checkoutFragment, checkoutFragment.getTagName());
        return true;
    }

    @Override
    protected boolean handleGSTUpdateResponse(AddAddress updatedAddress) {
        addressList.get(addressPosition).gstn = updatedAddress.gstn;
        addressList.get(addressPosition).billingName = updatedAddress.billingName;
        callCheckoutRequest();
        return true;
    }


    @Override
    protected boolean handleCartCheckoutError(Request request, APIError error) {
        super.handleCartCheckoutError(request, error);

        return true;
    }

    protected boolean handleShippingListResponse(ShippingAddressList data) {

        addressList = (ArrayList<ShippingAddressDetail>) data.result;
        Collections.sort(addressList, new GSTINComparator());
        if (addressList.size() == 0 && !isAddressAdded) {
            BaseFragment fragment = AddNewShippingAddressFragment.newInstance(AddNewShippingAddressFragment.FROM_BILLING);
            BaseFragment.addToBackStack((BaseActivity) getActivity(), fragment, AddNewShippingAddressFragment.getTagName());
        } else {
            preSelectedAddressIndex = data.selected;
            for (int i = 0; i < addressList.size(); i++) {
                if (addressList.get(i).id == preSelectedAddressIndex)
                    addressList.get(i).selectedItem = true;
            }

            initAdapter(addressList, preSelectedAddressIndex);

        }
        return true;
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.BILLING_ADDRESS) {
            addressPosition = (int) eventObject.objects[0];
            ShippingAddressDetail shippingAddressDetail = (ShippingAddressDetail) eventObject.objects[2];
            if (shippingAddressDetail.gstn == null || shippingAddressDetail.billingName == null) {
                showEditGSTINDailog(shippingAddressDetail);
            }
        }

        if (eventObject.id == EventConstant.SHIPPING_ADDRESS_ADDED) {
            isAddressAdded = (boolean) eventObject.objects[0];
        }
    }


    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        popToHome(getActivity());
        launchCart();
        return false;
    }

    private void showEditGSTINDailog(final ShippingAddressDetail addressDetail) {

        View view = context.getLayoutInflater().inflate(R.layout.dailog_gst_update, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        final AlertDialog dailogSKU = dialog.show();

        final EditText gstET = (EditText) view.findViewById(R.id.gstET);
        final EditText billingNameET = (EditText) view.findViewById(R.id.billingNameET);
        final RadioGroup gstRadioGroup = (RadioGroup) view.findViewById(R.id.gstRadioGroup);
        final RadioButton gstRB = (RadioButton) view.findViewById(R.id.gstRB);
        final RadioButton noGstRB = (RadioButton) view.findViewById(R.id.noGstRB);
        final TextView gstMsgTV = (TextView) view.findViewById(R.id.gstMsgTV);

        final TextInputLayout gstTIL = (TextInputLayout) view.findViewById(R.id.gstTIL);
        final TextInputLayout billingNameTIL = (TextInputLayout) view.findViewById(R.id.billingNameTIL);

        Button submitBT = (Button) view.findViewById(R.id.submitBT);

        gstET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gstTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        billingNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                billingNameTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });


        if (addressDetail.billingName != null && !addressDetail.billingName.equals("")) {
            billingNameET.setText(addressDetail.billingName);
        }

        final String[] selectedRadio = {"yesGST"};
        gstRB.setChecked(true);


        gstMsgTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noGstRB.setChecked(true);
                gstET.setText("");
                gstET.setEnabled(false);
                gstTIL.setErrorEnabled(false);
            }
        });

        gstRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == gstRB.getId()) {
                    selectedRadio[0] = "yesGST";
                    gstET.setEnabled(true);
                } else if (checkedId == noGstRB.getId()) {
                    selectedRadio[0] = "noGST";
                    gstET.setText("");
                    gstET.setEnabled(false);
                    gstTIL.setErrorEnabled(false);
                }
            }
        });


        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String GSTIN = gstET.getText().toString().trim();
                String billingName = billingNameET.getText().toString().trim();
                if (selectedRadio[0].equals("yesGST")) {

                    if ((GSTIN != null && !GSTIN.equals("")) && (billingName != null && !billingName.equals(""))) {
                        if (CommonUtility.isValidGSTIN(GSTIN)) {
                            dailogSKU.dismiss();
                            callupdateGSTNRequest(GSTIN, addressDetail.id, billingName);
                        } else {
                            gstTIL.setError(getString(R.string.invalid_gstin));
                            return;
                        }
                    } else if (GSTIN == null || GSTIN.equals("")) {
                        gstTIL.setError(getString(R.string.enter_gstn));
                        return;
                    } else if (billingName == null || billingName.equals("")) {
                        billingNameTIL.setError(getString(R.string.enter_billing_name));
                    }
                } else if (selectedRadio[0].equals("noGST")) {
                    isNoSelected = true;
                    if (billingName != null && !billingName.equals("")) {
                        dailogSKU.dismiss();
                        callupdateGSTNRequest(GSTIN, addressDetail.id, billingName);
                    } else {
                        billingNameTIL.setError(getString(R.string.enter_billing_name));
                    }
                } else {
                    dailogSKU.dismiss();
                }

            }
        });


    }
}
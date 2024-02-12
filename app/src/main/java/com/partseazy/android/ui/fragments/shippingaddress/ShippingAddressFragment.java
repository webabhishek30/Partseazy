package com.partseazy.android.ui.fragments.shippingaddress;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.shipping.ShippingAddressAdapter;
import com.partseazy.android.ui.fragments.cart_checkout.CartCheckoutBaseFragment;
import com.partseazy.android.ui.fragments.deals.booking_deal.CreateBookingDealFragment;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.deal.FinalDealSKU;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.AddAddress;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressList;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by can on 22/12/16.
 */

public class ShippingAddressFragment extends CartCheckoutBaseFragment implements View.OnClickListener {


    @BindView(R.id.scrollable)
    RecyclerView scrollable;
    @BindView(R.id.add_addressBtn)
    TextView add_addressBtn;
    @BindView(R.id.checkout_cartBT)
    Button checkout_cartBT;

    @BindView(R.id.bookingProgressBarLL)
    LinearLayout bookingProgressBarLL;

    @BindView(R.id.shippingProgressbarRL)
    LinearLayout shippingProgressbarRL;


    public static final String DEAL_BOOKING = "deal_booking";
    public static String SAME_BILLING_ADD = "same_billing_add";
    public static String DEAL_HOLDER = "deal_holder";
    public static String SELECTED_DEAL_SKU_MAP = "sku_map";

    private ShippingAddressAdapter addressAdapter;
    private List<ShippingAddressDetail> addressList;
    private int addressPosition = -1;
    private int preSelectedAddressIndex;
    private boolean isAddressAdded;
    private boolean isfromDealBooking = false;
    private boolean isSameBillingAdd = false;

    private Deal dealDetailHolder;
    private Map<Integer, FinalDealSKU> selectedSKUMap;


    public ShippingAddressFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAddressAdded = false;
        isSameBillingAdd = getArguments().getBoolean(SAME_BILLING_ADD, isSameBillingAdd);
        isfromDealBooking = getArguments().getBoolean(DEAL_BOOKING, false);
        dealDetailHolder = new Gson().fromJson(getArguments().getString(DEAL_HOLDER), Deal.class);

        Type dealSku = new TypeToken<Map<String, FinalDealSKU>>() {
        }.getType();
        selectedSKUMap = new Gson().fromJson(getArguments().getString(SELECTED_DEAL_SKU_MAP), dealSku);

    }


    public static ShippingAddressFragment newInstance() {
        Bundle bundle = new Bundle();
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ShippingAddressFragment newInstance(boolean isSameBillingAdd) {
        Bundle bundle = new Bundle();
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        bundle.putBoolean(SAME_BILLING_ADD, isSameBillingAdd);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static ShippingAddressFragment newInstance(boolean fromDealBooking, String dealdetailHolder, String selectedMap) {
        Bundle bundle = new Bundle();
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        bundle.putBoolean(DEAL_BOOKING, fromDealBooking);
        bundle.putString(DEAL_HOLDER, dealdetailHolder);
        bundle.putString(SELECTED_DEAL_SKU_MAP, selectedMap);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shipping_address;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.shipping_address);
    }


    public static String getTagName() {
        return ShippingAddressFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        showHeaderProgressBar();

        scrollable.setLayoutManager(new LinearLayoutManager(getContext()));
        shippingAddressListRequest();
        add_addressBtn.setOnClickListener(this);
        checkout_cartBT.setOnClickListener(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        toolbar.setTitle(R.string.shipping_address);
        return view;
    }

    private void showHeaderProgressBar() {
        if (isfromDealBooking) {
            shippingProgressbarRL.setVisibility(View.GONE);
            bookingProgressBarLL.setVisibility(View.VISIBLE);
        } else {
            bookingProgressBarLL.setVisibility(View.GONE);
            shippingProgressbarRL.setVisibility(View.VISIBLE);
        }
    }

    private void initAdapter(List<ShippingAddressDetail> addressDetails, int preSelectedAddressIndex) {

        checkout_cartBT.setEnabled(true);
        scrollable.setHasFixedSize(true);
        scrollable.setNestedScrollingEnabled(false);

        if (addressAdapter == null) {
            addressAdapter = new ShippingAddressAdapter(context, addressDetails, preSelectedAddressIndex, false);
            scrollable.setAdapter(addressAdapter);
        } else {
            scrollable.setAdapter(addressAdapter);
            addressAdapter.notifyAdapter(addressDetails, preSelectedAddressIndex);
        }


    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser) {
//            if (getTargetFragment() != null) {
//                if (getTargetFragment() == ShippingAddressFragment.newInstance()) {
//                    if (getTargetRequestCode() == 0) {
//                        shippingAddressListRequest();
//                    }
//                }
//            }
//        }
//       /* Fragment invoker = getTargetFragment();
//        if(invoker != null) {
//            invoker.callPublicMethod();
//        }*/
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_addressBtn:
                BaseFragment fragment = AddNewShippingAddressFragment.newInstance(AddNewShippingAddressFragment.FROM_CART);
                if (isfromDealBooking) {
                    fragment = AddNewShippingAddressFragment.newInstance(AddNewShippingAddressFragment.FROM_BOOKING);
                }
                BaseFragment.addToBackStack((BaseActivity) getActivity(), fragment, AddNewShippingAddressFragment.getTagName());
                fragment.setTargetFragment(this,1);
                break;

            case R.id.checkout_cartBT:

                callCheckoutRequest();
                break;

            default:
                break;
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
    protected boolean handleCartCheckoutResponse(CartCheckoutBaseData cartCheckoutBaseData) {
        if (isfromDealBooking) {
            launchCreateBooking(addressList.get(addressPosition));
        } else {
            checkForShipeableItem(cartCheckoutBaseData, addressList.get(addressPosition), addressPosition, false, isSameBillingAdd);
        }
        return true;
    }

    @Override
    protected boolean handleGSTUpdateResponse(AddAddress updatedAddress) {
        addressList.get(addressPosition).gstn = updatedAddress.gstn;
        addressList.get(addressPosition).name = updatedAddress.name;
        callCheckoutRequest();
        return true;
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
            ShippingAddressDetail selectedAddDetail = addressList.get(addressPosition);
            if (selectedAddDetail.name != null && !selectedAddDetail.name.equals("")) {
                DataStore.setPincode(context, addressList.get(addressPosition).pincode);
                callCartCheckoutRequest(finalCartAttributes);
            } else {
                showShippingNameDailog(selectedAddDetail);
            }
        } else
            SnackbarFactory.showSnackbar(getActivity(), getString(R.string.error_fill_address));

    }

    @Override
    protected boolean handleCartCheckoutError(Request request, APIError error) {
        super.handleCartCheckoutError(request, error);

        return true;
    }


    protected boolean handleShippingListResponse(ShippingAddressList data) {

        addressList = (ArrayList<ShippingAddressDetail>) data.result;
        if (addressList.size() == 0 && !isAddressAdded) {
            BaseFragment fragment = AddNewShippingAddressFragment.newInstance(AddNewShippingAddressFragment.FROM_CART);
            if (isfromDealBooking) {
                fragment = AddNewShippingAddressFragment.newInstance(AddNewShippingAddressFragment.FROM_BOOKING);
            }
            BaseFragment.addToBackStack((BaseActivity) getActivity(), fragment, AddNewShippingAddressFragment.getTagName());
        } else {
            preSelectedAddressIndex = data.selected;
            for (int i = 0; i < addressList.size(); i++) {
                if (addressList.get(i).id == preSelectedAddressIndex) {
                    addressList.get(i).selectedItem = true;
                    addressPosition = i;
                }
            }

            initAdapter(addressList, preSelectedAddressIndex);

        }
        return true;
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.SHIPPING_ADDRESS) {
            addressPosition = (int) eventObject.objects[0];
            ShippingAddressDetail shippingAddressDetail = (ShippingAddressDetail) eventObject.objects[2];
            if (shippingAddressDetail.name == null) {
                showShippingNameDailog(shippingAddressDetail);
            }
        }

        if (eventObject.id == EventConstant.SHIPPING_ADDRESS_ADDED) {
            isAddressAdded = (boolean) eventObject.objects[0];
        }
    }


    private void launchCreateBooking(ShippingAddressDetail addressDetails) {
        context.onPopBackStack(true);
        CreateBookingDealFragment createBookingDealFragment = CreateBookingDealFragment.newInstance(new Gson().toJson(dealDetailHolder), new Gson().toJson(selectedSKUMap), addressDetails);
        addToBackStack((BaseActivity) getActivity(), createBookingDealFragment, createBookingDealFragment.getTagName());
    }

    private void showShippingNameDailog(final ShippingAddressDetail addressDetail) {

        View view = context.getLayoutInflater().inflate(R.layout.dailog_update_shipping_name, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        final AlertDialog dailogSKU = dialog.show();

        final EditText shippingNameET = (EditText) view.findViewById(R.id.shippingNameET);
        final TextInputLayout shippingNameTIL = (TextInputLayout) view.findViewById(R.id.shippingNameTIL);

        Button submitBT = (Button) view.findViewById(R.id.submitBT);

        shippingNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shippingNameTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });


        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shippingName = shippingNameET.getText().toString().trim();
                if (shippingName != null && !shippingName.equals("")) {
                    dailogSKU.dismiss();
                    callupdateShippingNameRequest(addressDetail.id, shippingName);
                } else {
                    shippingNameTIL.setError(getString(R.string.enter_delivery_name));
                }

            }
        });


    }


    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        if (isfromDealBooking) {
            if (addressPosition != -1) {
                popToHome(getActivity());
                CreateBookingDealFragment createBookingDealFragment = CreateBookingDealFragment.newInstance(new Gson().toJson(dealDetailHolder), new Gson().toJson(selectedSKUMap), addressList.get(addressPosition));
                addToBackStack((BaseActivity) getActivity(), createBookingDealFragment, createBookingDealFragment.getTagName());
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.error_fill_address));
            }
        } else {
            popToHome(getActivity());
            launchCart();
        }

        return false;
    }
}

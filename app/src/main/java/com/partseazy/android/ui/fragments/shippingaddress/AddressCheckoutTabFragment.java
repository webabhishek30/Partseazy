package com.partseazy.android.ui.fragments.shippingaddress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;

import java.io.Serializable;

import butterknife.BindView;

/**
 * Created by naveen on 13/7/17.
 */

public class AddressCheckoutTabFragment extends BaseFragment {

    @BindView(R.id.nameTV)
    TextView nameTV;

    @BindView(R.id.addressTV)
    TextView addressTV;

    @BindView(R.id.landmarkTV)
    TextView landmarkTV;

    @BindView(R.id.pincodeTV)
    TextView pincodeTV;

    @BindView(R.id.mobileTV)
    TextView mobileTV;

    @BindView(R.id.gstnTV)
    TextView gstnTV;

    @BindView(R.id.changeAddressTV)
    TextView changeAddressTV;

    private final static String IS_SHIPPING_FRAMENT = "isShippingFragment";
    private final static String ADDRESS_DETAIL = "address_detail";
    private final static String ADDRESS_INDEX= "address_index";

    private boolean isShippingFragment;
    private ShippingAddressDetail addressDetail;
    private int selectedAddressIndex;

    public static AddressCheckoutTabFragment newInstance(boolean isShippingFragment,ShippingAddressDetail addressDetail,int addressIndex) {
        Bundle bundle = new Bundle();
        AddressCheckoutTabFragment fragment = new AddressCheckoutTabFragment();
        bundle.putBoolean(IS_SHIPPING_FRAMENT, isShippingFragment);
        bundle.putSerializable(ADDRESS_DETAIL,(Serializable) addressDetail);
        bundle.putInt(ADDRESS_INDEX, addressIndex);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_address_tab;
    }

    @Override
    protected String getFragmentTitle() {
        return AddressCheckoutTabFragment.class.getSimpleName();
    }

    public static String getTagName() {
        return AddressCheckoutTabFragment.class.getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShippingFragment = getArguments().getBoolean(IS_SHIPPING_FRAMENT);
        selectedAddressIndex = getArguments().getInt(ADDRESS_INDEX);
        addressDetail = (ShippingAddressDetail)getArguments().getSerializable(ADDRESS_DETAIL);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
    }

    private void setViews()
    {
        if(isShippingFragment){
            nameTV.setText(addressDetail.name);
            changeAddressTV.setText(getString(R.string.change_shipping_address));

        }else {
            changeAddressTV.setText(getString(R.string.change_billing_address));
            if (addressDetail.billingName != null) {
                nameTV.setText(addressDetail.billingName);
            } else {
                nameTV.setText(addressDetail.name);
            }
        }

        if (addressDetail.gstn != null && !addressDetail.gstn.equals("")) {
            gstnTV.setText(context.getString(R.string.gstn_caps_str, addressDetail.gstn.toUpperCase()));
            gstnTV.setVisibility(View.VISIBLE);
        } else {
            gstnTV.setVisibility(View.GONE);
        }

        addressTV.setText(addressDetail.street + ", " + addressDetail.city + ", " + addressDetail.state + " - " + addressDetail.pincode);

        if (addressDetail.landmark != null) {
            landmarkTV.setVisibility(View.VISIBLE);
            landmarkTV.setText(context.getString(R.string.shipping_landmark, addressDetail.landmark));
        } else
            landmarkTV.setVisibility(View.GONE);
        mobileTV.setText("# "+ addressDetail.mobile);

        changeAddressTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

             if(isShippingFragment)
             {
                 launchShippingScreen();
             }else{
                 launchBillingScreen();
             }
            }
        });
    }

    private void launchBillingScreen()
    {
        BillingAddressFragment fragment = new BillingAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(fragment.SHIPPING_ADD_INDEX, selectedAddressIndex);
        bundle.putSerializable(fragment.SHIPPING_ADD, addressDetail);
        fragment.setArguments(bundle);
        BaseFragment baseFragment = fragment;
        BaseFragment.removeTopAndAddToBackStack(context, baseFragment, BillingAddressFragment.getTagName());
    }

    private void launchShippingScreen()
    {
        Bundle bundle = new Bundle();
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        bundle.putInt(context.getString(R.string.selectedValue), selectedAddressIndex);
        fragment.setArguments(bundle);
        BaseFragment baseFragment = fragment;
        BaseFragment.removeTopAndAddToBackStack(context, baseFragment, ShippingAddressFragment.getTagName());

    }
}

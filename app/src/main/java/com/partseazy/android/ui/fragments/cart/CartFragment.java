package com.partseazy.android.ui.fragments.cart;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.ui.adapters.cart.CartAdapter;
import com.partseazy.android.ui.fragments.cart_checkout.CartCheckoutBaseFragment;
import com.partseazy.android.ui.fragments.shippingaddress.ShippingAddressFragment;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.partseazy.android.utility.CommonUtility;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by CAN on 1/7/2017.
 */

public class CartFragment extends CartCheckoutBaseFragment {


    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.total_rs_value_cartTV)
    protected TextView total_rs_value_cartTV;
    @BindView(R.id.checkout_cartBT)
    protected Button checkout_cartBT;
    @BindView(R.id.cartRL)
    protected RelativeLayout cartRL;
    @BindView(R.id.noResultContinueBT)
    protected Button noResultContinueBT;


    private CartAdapter cartAdapter;
    private CartCheckoutBaseData data;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable recyclerViewState;


    public static CartFragment newInstance() {
        Bundle bundle = new Bundle();
        CartFragment fragment = new CartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public FinalCartAttributes getFinalCartAttributes() {
        //Everytime new Final Cart attribute on starting
        return new FinalCartAttributes();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cart_home;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.cart_name);
    }

    public static String getTagName() {
        return CartFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        cartRL.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getContext());
        callCartCheckoutRequest(finalCartAttributes);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        hideKeyBoard(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyBoard(getView());

    }


    @Override
    protected boolean handleCartCheckoutResponse(CartCheckoutBaseData cartCheckoutBaseData) {
        data = cartCheckoutBaseData;


        if (data != null && data.list.size() != 0) {
            cartRL.setVisibility(View.VISIBLE);
            if (getActivity() != null && isAdded()) {
                total_rs_value_cartTV.setText(getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(data.summary.grandTotal)));

                if (!data._continue) {

                    if (isOnlyServiceableError(cartCheckoutBaseData)) {
                        checkout_cartBT.setEnabled(!data._continue);
                        checkout_cartBT.setText(getString(R.string.select_alternate_address));
                    } else {
                        checkout_cartBT.setEnabled(data._continue);
                    }

                } else {

                    checkout_cartBT.setText(getString(R.string.checkout));
                    checkout_cartBT.setEnabled(data._continue);
                }

            }
            setAdapter(data);
        } else {
            //Empty Cart
            RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) noResultContinueBT.getLayoutParams();
            buttonLayoutParams.setMargins(buttonLayoutParams.leftMargin, buttonLayoutParams.topMargin, buttonLayoutParams.rightMargin, 200);
            noResultContinueBT.setLayoutParams(buttonLayoutParams);
            PartsEazyEventBus.getInstance().postEvent(EventConstant.UPDATE_TAB_COUNT_CART_ID, 0);
            if (getActivity() != null && isAdded()) {
                showNoResult(getString(R.string.empty_cart), R.drawable.empty_cart, cartRL);
            }


        }
        return true;
    }

    @Override
    protected boolean handleCartCheckoutError(Request request, APIError error) {
        cartRL.setVisibility(View.GONE);
        showError(error.message, MESSAGETYPE.SNACK_BAR);
        return true;
    }

    private void setAdapter(final CartCheckoutBaseData data) {

        mRecyclerView.setLayoutManager(linearLayoutManager);


        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(CartFragment.this, data);
            mRecyclerView.setAdapter(cartAdapter);
        } else {
            mRecyclerView.setAdapter(cartAdapter);
            cartAdapter.updateData(data);
            if (recyclerViewState != null)
                mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        }
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.POST_SELECTED_SHIPPING) {
            String shippingMethod = (String) eventObject.objects[0];
            finalCartAttributes.ShippingMethod = shippingMethod;
            recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            showProgressBar();
            callCartCheckoutRequest(finalCartAttributes);
        }

        if (eventObject.id == EventConstant.POST_PROMOCODE) {
            String promoCode = (String) eventObject.objects[0];
            finalCartAttributes.couponCode = promoCode;
            recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            callCartCheckoutRequest(finalCartAttributes);

        }

        if (eventObject.id == EventConstant.CART_CFORM) {
            int cform = (int) eventObject.objects[0];
            finalCartAttributes.cForm = cform;
            recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            callCartCheckoutRequest(finalCartAttributes);
        }

        if (eventObject.id == EventConstant.CART_ITEM_QUANTITY) {
            int productSKUID = (int) eventObject.objects[0];
            int quantity = (int) eventObject.objects[1];
            boolean isProductSet = (boolean) eventObject.objects[2];
            int position = (int) eventObject.objects[3]; // Could be useful later

            recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            callCartUpdate(finalCartAttributes, productSKUID, quantity, isProductSet);
        }

        if (eventObject.id == EventConstant.CART_ITEM_REMOVED) {
            String skuStr = (String) eventObject.objects[0];
            String[] skuValue = skuStr.split(":");
            String skuName = skuValue[0];
            String skuId = skuValue[1];

            // MoEHelper REMOVED FROM CART Event
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrString(MoengageConstant.Events.SKU, skuName)
                    .putAttrString(MoengageConstant.Events.SKU_ID,skuId)
                    .putAttrString(MoengageConstant.Events.PARAMS , eventObject.objects.toString());
            MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.CART_ITEM_REMOVED, builder);

            recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            removeCartItemRequest(finalCartAttributes, skuId, skuName);


        }

        if (eventObject.id == EventConstant.CART_ITEM_MOVED_WISHLIST) {
            String skuStr = (String) eventObject.objects[0];
            String[] skuValue = skuStr.split(":");
            String skuName = skuValue[0];
            String skuId = skuValue[1];
            // MoEHelper REMOVED FROM CART Event
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrString(MoengageConstant.Events.SKU, skuName)
                    .putAttrString(MoengageConstant.Events.SKU_ID,skuId)
                    .putAttrString(MoengageConstant.Events.PARAMS , eventObject.objects.toString());
            MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.CART_ITEM_MOVED_WISHLIST, builder);
            recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            moveCartItemToFavRequest(finalCartAttributes, skuId, skuName);
        }

        if (eventObject.id == EventConstant.CART_ERROR) {
            int parentPosition = (int) eventObject.objects[0];
            String message = (String) eventObject.objects[1];
            cartAdapter.updateCartOnError(parentPosition, message);

        }


    }


    @OnClick(R.id.checkout_cartBT)
    public void onSubmitButtonClick() {
        ShippingAddressFragment fragment = ShippingAddressFragment.newInstance();
        addToBackStack((BaseActivity) getActivity(), fragment, ShippingAddressFragment.getTagName());

    }


    @Override
    protected void retryFailedRequest(int identifier, Request<?> oldRequest, VolleyError error) {
        if (isAdded() && getActivity() != null && oldRequest.getIdentifier() == RequestIdentifier.CART_CHECKOUT_ID.ordinal()) {
            showError(getString(R.string.wait_trying_to_reconnect), MESSAGETYPE.SNACK_BAR);
            callCartCheckoutRequest(finalCartAttributes);
        }

    }
}
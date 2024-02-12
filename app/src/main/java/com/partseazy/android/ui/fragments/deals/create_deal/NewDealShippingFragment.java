package com.partseazy.android.ui.fragments.deals.create_deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.create.DealDispatchAdapter;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.model.deal.CheckboxModel;
import com.partseazy.android.ui.model.deal.address.AddressResult;
import com.partseazy.android.ui.model.deal.create_deal.NewDealData;
import com.partseazy.android.ui.model.deal.create_deal.NewDealResult;
import com.partseazy.android.ui.model.deal.deal_detail.Address;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.CityDetail;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 3/5/17.
 */

public class NewDealShippingFragment extends NewDealBaseFragment implements View.OnClickListener {


    @BindView(R.id.pincodeTIL)
    protected TextInputLayout pincodeTIL;

    @BindView(R.id.pincodeET)
    protected EditText pincodeET;

    @BindView(R.id.addressTIL)
    protected TextInputLayout addressTIL;

    @BindView(R.id.addressET)
    protected EditText addressET;

    @BindView(R.id.cityTIL)
    protected TextInputLayout cityTIL;

    @BindView(R.id.cityET)
    protected EditText cityET;

    @BindView(R.id.stateTIL)
    protected TextInputLayout stateTIL;

    @BindView(R.id.stateET)
    protected EditText stateET;

    @BindView(R.id.priceLineView)
    protected View priceLineView;

    @BindView(R.id.priceCheckIV)
    protected ImageView priceCheckIV;

    @BindView(R.id.shippingLineView)
    protected View shippingLineView;

    @BindView(R.id.shippingCheckIV)
    protected ImageView shippingCheckIV;

    @BindView(R.id.continueBT)
    protected Button continueBT;

    @BindView(R.id.pickupAdressLL)
    protected LinearLayout pickupAdressLL;


    @BindView(R.id.pickupCB)
    protected CheckBox pickupCB;

    @BindView(R.id.shipCB)
    protected CheckBox shipCB;

    @BindView(R.id.dispatchRV)
    protected RecyclerView dispatchRV;


    @BindView(R.id.shippingAmountET)
    protected EditText shippingAmountET;


    @BindView(R.id.shipChargeLL)
    protected LinearLayout shipChargeLL;



    private boolean isPinCodeVaid = false;
    private DealDispatchAdapter dispatchAdapter;


    public static NewDealShippingFragment newInstance() {
        Bundle bundle = new Bundle();
        NewDealShippingFragment fragment = new NewDealShippingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shipping_new_deal;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.create_deal);
    }


    public static String getTagName() {
        return NewDealShippingFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        setShippingProgress();
        setDispatchAdapter();
        // pickupCB.setOnClickListener(this);
        shippingAmountET.setText(0+"");
        continueBT.setOnClickListener(this);
        pincodeET.addTextChangedListener(new EditTextWatcher(pincodeET, pincodeTIL));
        handleShippingClicks();
        shippingAddressCall();
        return view;

    }


    public void handleShippingClicks() {


        shipCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shipCB.isChecked()) {
                    shipChargeLL.setVisibility(View.VISIBLE);
                } else {
                    shipChargeLL.setVisibility(View.GONE);
                    shippingAmountET.setText(0+"");
                }
            }
        });

    }

    private void setAddressView(Address address)
    {
        addressET.setText(address.street);
        cityET.setText(address.city);
        stateET.setText(address.state);
        pincodeET.setText(address.pincode);
    }

    private void setDispatchAdapter() {
        List<CheckboxModel> categoryList = new ArrayList<>();
        categoryList.add(new CheckboxModel("1", "1"));
        categoryList.add(new CheckboxModel("2", "2"));
        categoryList.add(new CheckboxModel("3", "3"));
        categoryList.add(new CheckboxModel("5", "5"));
        categoryList.add(new CheckboxModel("7", "7"));
        categoryList.add(new CheckboxModel("10", "10"));
        categoryList.add(new CheckboxModel("11", "11+"));

        if (dispatchAdapter == null)
            dispatchAdapter = new DealDispatchAdapter(context, categoryList);
        dispatchRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        dispatchRV.setAdapter(dispatchAdapter);
    }


    private void setShippingProgress() {
        priceCheckIV.setImageResource(R.drawable.check_green_circle);
        shippingCheckIV.setImageResource(R.drawable.check_green_circle);
        priceLineView.setBackgroundColor(getResources().getColor(R.color.green_checkout));
        shippingLineView.setBackgroundColor(getResources().getColor(R.color.green_checkout));
    }

    private void createDealCall() {
        Map<String, Object> paramMap = WebServicePostParams.getCreateDealParams(dealData);

        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);

        getNetworkManager().PostRequest(RequestIdentifier.CREATE_DEAL_ID.ordinal(),
                WebServiceConstants.CREATE_DEAL, paramMap, params, this, this, false);
    }

    private void shippingAddressCall()
    {
        getNetworkManager().GetRequest(RequestIdentifier.GET_DEAL_SHIPPING_ADDRESS.ordinal(),
                WebServiceConstants.DEAL_SHIPPING_ADDRESS, null, null, this, this, true);
    }


    private boolean isValidData() {
        boolean isValidData = true;


        if (addressET.getText().toString().trim().length() > 0) {
            dealData.address = addressET.getText().toString();
        } else {
            isValidData = false;
            addressTIL.setError(getString(R.string.enter_address));
            return false;
        }

        isValidData = isPinCodeVaid;

        if (shipCB.isChecked()) {
            String shippingCharge = shippingAmountET.getText().toString().trim();
            if (shippingCharge != null && !shippingCharge.equals("")) {
                dealData.shippingCharge = Integer.parseInt(shippingCharge);
                dealData.shipMethodList.add(DealConstants.DROP_SHIP);
            }
        } else {
            dealData.shippingCharge = 0;
            dealData.shipMethodList.remove(DealConstants.DROP_SHIP);
        }

        if(pickupCB.isChecked())
        {
            dealData.shipMethodList.add(DealConstants.PICK_UP);
        }else{
            dealData.shipMethodList.remove(DealConstants.PICK_UP);
        }

        if(!pickupCB.isChecked() && !shipCB.isChecked())
        {
            showError(getString(R.string.please_select_shipping_method),MESSAGETYPE.SNACK_BAR);
            isValidData = false;
        }

        CustomLogger.d("Data Json :" + new Gson().toJson(dealData));
        return isValidData;
    }


    @Override
    protected boolean handlePincodeError(Request request, APIError error) {

        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_CITY_ID.ordinal()) {
            if (getActivity() != null)
                showError(getActivity().getString(R.string.inavlid_pincode), MESSAGETYPE.TOAST);
            pincodeTIL.setError("Please try another pincode");
            cityET.setText("");
            stateET.setText("");
            isPinCodeVaid = false;

        }

        return true;
    }

    @Override
    protected boolean handlePincodeResponse(CityDetail cityDetail) {

        if (cityDetail != null) {
            cityET.setText(cityDetail.city);
            stateET.setText(cityDetail.state);
            isPinCodeVaid = true;
        }

        return true;
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.CREATE_DEAL_ID.ordinal()) {
            if (apiError != null) {
                showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
            } else {
                showError();
            }
        }


        return super.handleErrorResponse(request, error, apiError);
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.CREATE_DEAL_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), NewDealResult.class, new OnGsonParseCompleteListener<NewDealResult>() {
                        @Override
                        public void onParseComplete(NewDealResult data) {

                            if (data != null) {
                                launchSendDealFragment(data.result);
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                        }
                    }
            );
        }


        if(request.getIdentifier() == RequestIdentifier.GET_DEAL_SHIPPING_ADDRESS.ordinal())
        {
            getGsonHelper().parse(responseObject.toString(), AddressResult.class, new OnGsonParseCompleteListener<AddressResult>() {
                        @Override
                        public void onParseComplete(AddressResult addressDetail) {

                            if (addressDetail.address!= null) {
                                try {
                                    CustomLogger.d("AddressDetail ::" + addressDetail.address.state);
                                    setAddressView(addressDetail.address);
                                    isPinCodeVaid = true;
                                }catch (Exception exception){
                                    CustomLogger.e("Exception ", exception);
                                }
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                        }
                    }
            );
        }

        return super.handleResponse(request, responseObject, response);
    }


    private void launchSendDealFragment(NewDealData newDealData) {
        NewDealSendFragment newDealSendFragment = NewDealSendFragment.newInstance(newDealData.id);
        addToBackStack((BaseActivity) getActivity(), newDealSendFragment, newDealSendFragment.getTagName());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pickupCB:
                if (pickupCB.isChecked())
                    pickupAdressLL.setVisibility(View.VISIBLE);
                else
                    pickupAdressLL.setVisibility(View.GONE);
                break;

            case R.id.continueBT:
                if (isValidData()) {
                    //   addToBackStack((BaseActivity) getActivity(), NewDealSendFragment.newInstance(), NewDealSendFragment.getTagName());
                    createDealCall();
                }
                break;
        }
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.DEAL_DISPATCH) {
            String dispatchDay = (String) eventObject.objects[0];
            dealData.dispatchDays = Integer.parseInt(dispatchDay);

        }
    }

}

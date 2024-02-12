package com.partseazy.android.ui.fragments.complete;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.fragments.account.OrderHomeFragment;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.order.OrderSummary;

import org.json.JSONObject;

import butterknife.BindView;

import static com.partseazy.android.R.color.green_checkout;

/**
 * Created by CAN on 1/13/2017.
 */

public class ThankYouFragment extends BaseFragment implements View.OnClickListener {


    private static final String ODIN_KEY = "_ODIN";

    @BindView(R.id.thirdLineView)
    protected View thirdLineView;

    @BindView(R.id.secondLineView)
    protected View secondLineView;

    @BindView(R.id.firstLineView)
    protected View firstLineView;


    @BindView(R.id.fourthIV)
    protected ImageView fourthIV;

    @BindView(R.id.thirdIV)
    protected ImageView thirdIV;

    @BindView(R.id.secondIV)
    protected ImageView secondIV;


    private String orderNumber;
    @BindView(R.id.nameTV)
    protected TextView nameTV;
    @BindView(R.id.orderNumber)
    protected TextView orderNumberTV;
    @BindView(R.id.goToMyOrdersTV)
    protected TextView goToMyOrdersTV;
    @BindView(R.id.continueBTN)
    protected Button continueBTN;


    public static ThankYouFragment newInstance(String ODIN) {
        Bundle bundle = new Bundle();
        ThankYouFragment fragment = new ThankYouFragment();
        bundle.putString(ODIN_KEY, ODIN);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_complete;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.payment_header);
    }

    public static String getTagName() {
        return ThankYouFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProgressView();
        orderNumber = getArguments().getString(ODIN_KEY);
        goToMyOrdersTV.setOnClickListener(this);
        continueBTN.setOnClickListener(this);
        fetchOrderDetails();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setProgressView()
    {
        thirdLineView.setBackgroundColor(getResources().getColor(green_checkout));
        secondLineView.setBackgroundColor(getResources().getColor(green_checkout));
        firstLineView.setBackgroundColor(getResources().getColor(green_checkout));
        fourthIV.setImageResource(R.drawable.check_green_circle);
        thirdIV.setImageResource(R.drawable.check_green_circle);
        secondIV.setImageResource(R.drawable.check_green_circle);
    }

    private void fetchOrderDetails() {
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.ORDER_DETAILS_ID.ordinal(),
                WebServiceConstants.ORDER_SUMMARY + orderNumber, null, null, this, this, false);
    }

    private void updateOrderSummaryUI(OrderSummary orderSummary) {

        if (orderSummary.deliveryAddress != null) {
            if (orderSummary.deliveryAddress.name != null)
                nameTV.setText(getContext().getString(R.string.thank_you_name, orderSummary.deliveryAddress.name));

            //TODO
            if (orderSummary.deliveryAddress.name != null)
                orderNumberTV.setText(getContext().getString(R.string.order_no, orderSummary.orderSub.get(0).orderMasterOdin));

        }
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.ORDER_DETAILS_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), OrderSummary.class, new OnGsonParseCompleteListener<OrderSummary>() {
                        @Override
                        public void onParseComplete(OrderSummary data) {
                            try {
                                hideProgressDialog();
                                updateOrderSummaryUI(data);
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            APIError er = new APIError();
                            er.message = exception.getMessage();

                        }
                    }
            );

        }
        return true;
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == goToMyOrdersTV.getId()) {
            popToHome(getActivity());
            addToBackStack(getContext(), OrderHomeFragment.newInstance(false), OrderHomeFragment.getTagName());

        }
        if (view.getId() == continueBTN.getId()) {
            popToMall(getActivity());
        }

    }

    @Override
    protected void retryFailedRequest(int identifier, Request<?> oldRequest, VolleyError error) {
        if (oldRequest.getIdentifier() == RequestIdentifier.ORDER_DETAILS_ID.ordinal()){
            showError(getString(R.string.wait_trying_to_reconnect), MESSAGETYPE.SNACK_BAR);
            fetchOrderDetails();
        }

    }

    @Override
    public boolean onBackPressed() {
        popToMall(getActivity());
        return false;
    }

}

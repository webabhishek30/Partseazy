package com.partseazy.android.ui.fragments.checkout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.ui.adapters.checkout.CheckoutPaymentAdapter;
import com.partseazy.android.ui.fragments.fos.AgentAppFragment;
import com.partseazy.android.ui.fragments.fos.AgentSingleton;
import com.partseazy.android.ui.model.cart_checkout.PaymentPrepaidData;
import com.partseazy.android.ui.model.prepaid.PrepaidData;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.partseazy.android.utility.CommonUtility;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by can on 26/12/16.
 */

public class PrepaidFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.payment_optionRV)
    RecyclerView payment_optionRV;
    @BindView(R.id.discountPriceTv)
    TextView discountPriceTv;
    @BindView(R.id.paymentsTV)
    TextView paymentsTV;

    @BindView(R.id.place_orderTV)
    protected Button placeOrderButton;

    private CheckoutPaymentAdapter paymentAdapter;
    private PaymentPrepaidData prepaidData;

    public final static String key = "PREPAID";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_prepaid;
    }

    @Override
    protected String getFragmentTitle() {
        return "";
    }

    public static PrepaidFragment newInstance(PaymentPrepaidData prepaid) {
        Bundle bundle = new Bundle();
        PrepaidFragment fragment = new PrepaidFragment();
        bundle.putSerializable(key, (Serializable) prepaid);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getTagName() {
        return PrepaidFragment.class.getSimpleName();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (savedInstanceState != null)
            placeOrderButton.setEnabled(savedInstanceState.getBoolean("BUTTONSTATE"));
        else
            placeOrderButton.setEnabled(false);

        placeOrderButton.setOnClickListener(this);
        prepaidData = (PaymentPrepaidData) bundle.getSerializable(key);


        if (prepaidData != null) {
            if (prepaidData.pcent != 0) {
                discountPriceTv.setVisibility(View.VISIBLE);
                paymentsTV.setVisibility(View.VISIBLE);
                discountPriceTv.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.text_dark) + getString(R.string.additional) +
                        "<font color=" + getResources().getColor(R.color.green_checkout) + ">" + prepaidData.pcent + "% Discount (CD)</font>" +
                        "<font color=" + getResources().getColor(R.color.text_dark) + getString(R.string.on_prepaid_payment)));
                paymentsTV.setText(Html.fromHtml(getString(R.string.you_need_to_pay) + CommonUtility.convertionPaiseToRupeeString(prepaidData.total) + "</font>" +
                        "<font color=" + getResources().getColor(R.color.green_checkout) + "></font>"));
            } else {
                discountPriceTv.setVisibility(View.GONE);
                paymentsTV.setVisibility(View.GONE);
            }
        }
        payment_optionRV.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initAdapter() {
        //should be driven by fetauremap

        ArrayList<PrepaidData> datas = new ArrayList<>();

        PrepaidData prepaidData;

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.checkout_prepaid_card)) {
            prepaidData = new PrepaidData();
            prepaidData.setOption(StaticMap.PAYMENY_PREPAID_CC_DC);
            prepaidData.methodId = StaticMap.PAYMENY_PREPAID_CC_DC_KEY;
            datas.add(prepaidData);


        }

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.checkout_prepaid_nb)) {
            prepaidData = new PrepaidData();
            prepaidData.setOption(StaticMap.PAYMENY_PREPAID_NB);
            prepaidData.methodId = StaticMap.PAYMENY_PREPAID_NB_KEY;
            datas.add(prepaidData);


        }

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.checkout_prepaid_upi)) {
            prepaidData = new PrepaidData();
            prepaidData.setOption(StaticMap.PAYMENY_PREPAID_UPI);
            prepaidData.methodId = StaticMap.PAYMENY_PREPAID_UPI_KEY;
            datas.add(prepaidData);


        }


//        String[] prepaidOption = {getString(R.string.credit_pay), getString(R.string.netbanking_pay), getString(R.string.upi)};
//        String[] prepaidID = {"card", "nb", "upi"};
//
//        ArrayList<PrepaidData> datas = new ArrayList<>();
//        PrepaidData prepaidData;
//        for (int i = 0; i < prepaidOption.length; i++) {
//            prepaidData = new PrepaidData();
//            prepaidData.setOption(prepaidOption[i]);
//            prepaidData.methodId = prepaidID[i];
////            prepaidData.setChooseOption(0);
//            datas.add(prepaidData);
//        }

        if (paymentAdapter == null) {
            paymentAdapter = new CheckoutPaymentAdapter(getContext(), datas, key);
            payment_optionRV.setAdapter(paymentAdapter);

        } else {
            payment_optionRV.setAdapter(paymentAdapter);
            placeOrderButton.setSelected(paymentAdapter.isAnyOptionAreadySelected());
        }


        payment_optionRV.setHasFixedSize(true);
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.CHECKOUT_ORDER_ERROR) {
            Boolean ISENABLED = (Boolean) eventObject.objects[0];
            placeOrderButton.setEnabled(true);
        }

        if (eventObject.id == EventConstant.SELCTED_PAY_METHOD) {
            String paymentMethod = (String) eventObject.objects[1];
            if (paymentMethod.equals(key)) {
                placeOrderButton.setEnabled(true);
            }

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("BUTTONSTATE", placeOrderButton.isSelected());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.place_orderTV:
                final String selectedPaymentMode = paymentAdapter.getSelectedPaymentMethod();
                CustomLogger.d("SelectedPayment Method ::" + selectedPaymentMode);
                if (DataStore.isAgentType(context)) {
                    AgentSingleton agentSingleton = AgentSingleton.getInstance();
                    if (agentSingleton != null && agentSingleton.getRetailerData() != null) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.PLACE_ORDER, key, selectedPaymentMode);
                    } else {
                       // showError(getString(R.string.associate_with_retailer), MESSAGETYPE.TOAST);
                        if (getActivity() != null) {
                            DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.associate_with_retailer_msg), getString(R.string.YES)
                                    ,  getString(R.string.NO), null, new DialogListener() {
                                        @Override
                                        public void onPositiveButton(DialogInterface dialog) {
                                            PartsEazyEventBus.getInstance().postEvent(EventConstant.PLACE_ORDER, key, selectedPaymentMode);
                                        }

                                        @Override
                                        public void onNegativeButton(DialogInterface dialog) {
                                            popToHome((BaseActivity)context);
                                            BaseFragment fosFragment = AgentAppFragment.newInstance();
                                            BaseFragment.addToBackStack((BaseActivity) context, fosFragment, fosFragment.getTag());
                                        }
                                    });
                        }
                    }

                }else{
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.PLACE_ORDER, key, selectedPaymentMode);
                }

                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

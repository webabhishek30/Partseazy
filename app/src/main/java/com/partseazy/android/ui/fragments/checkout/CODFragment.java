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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.checkout.CheckoutPaymentAdapter;
import com.partseazy.android.ui.fragments.fos.AgentAppFragment;
import com.partseazy.android.ui.fragments.fos.AgentSingleton;
import com.partseazy.android.ui.model.cart_checkout.PaymentCodData;
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

public class CODFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.payment_optionRV)
    protected RecyclerView payment_optionRV;
    @BindView(R.id.small_amount_paymentBtn)
    protected Button small_amount_paymentBtn;
    @BindView(R.id.codRL)
    protected RelativeLayout codRL;
    @BindView(R.id.upfront_paymentRL)
    protected RelativeLayout upfront_paymentRL;
    @BindView(R.id.cod_msgTV)
    protected TextView cod_msgTV;
    @BindView(R.id.prepaid_amountTV)
    TextView prepaid_amountTV;
    @BindView(R.id.total_amountTV)
    TextView total_amountTV;
    @BindView(R.id.prepaid_paymentTV)
    TextView prepaid_paymentTV;
    @BindView(R.id.cod_balanceTV)
    TextView cod_balanceTV;
    @BindView(R.id.prepaid_discountTV)
    TextView prepaid_discountTV;
    @BindView(R.id.cod_balance_discountTV)
    TextView cod_balance_discountTV;
    @BindView(R.id.discount_msg_prepaidTV)
    TextView code_FooterTitle;
    @BindView(R.id.place_orderBTN)
    Button place_orderBTN;
    @BindView(R.id.codChargeTV)
    TextView codChargeTV;

    private CheckoutPaymentAdapter paymentAdapter;
    private PaymentCodData paymentCod;

    public final static String key = "COD";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_cod;
    }

    @Override
    protected String getFragmentTitle() {
        return "";
    }

    public static CODFragment newInstance(PaymentCodData cod) {
        Bundle bundle = new Bundle();
        CODFragment fragment = new CODFragment();
        bundle.putSerializable(key, (Serializable) cod);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getTagName() {
        return CODFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        setRetainInstance(true);
        Bundle bundle = getArguments();
        cod_balance_discountTV.setEnabled(false);
        place_orderBTN.setEnabled(false);
        place_orderBTN.setOnClickListener(this);
        boolean cod_available = bundle.getBoolean("cod_available"); //TODO check use in future..
        paymentCod = (PaymentCodData) bundle.getSerializable(key);
        initView();
        payment_optionRV.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();
    }

    private void initView() {

        cod_msgTV.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.black) + ">" + getString(R.string.for_order_above) + " </font>" +
                "<font color=" + getResources().getColor(R.color.black) + ">" + getString(R.string.not_available_msg) + " </font>" +
                "<font color=" + getResources().getColor(R.color.black) + "> " + getString(R.string.rs, AppConstants.COD_RANGE) + ". </font>" +
                "<font color=" + getResources().getColor(R.color.black) + ">" + getString(R.string.amount_msg, paymentCod.prepay.pcent) + " </font>" +
                "<font color=" + getResources().getColor(R.color.colorPrimary) + "> (" + getString(R.string.rs, CommonUtility.convertionPaiseToRupee(paymentCod.prepay.total)) + ") </font>"));

        small_amount_paymentBtn.setOnClickListener(this);

        if (!paymentCod.prepay.required) {
            codRL.setVisibility(View.VISIBLE);
            upfront_paymentRL.setVisibility(View.GONE);
            if(paymentCod.charges>0)
            {
                prepaid_amountTV.setText(getString(R.string.rs, CommonUtility.convertionPaiseToRupee(paymentCod.charges)));
                codChargeTV.setVisibility(View.GONE);
                prepaid_amountTV.setVisibility(View.GONE);
            }else{
                codChargeTV.setVisibility(View.GONE);
                prepaid_amountTV.setVisibility(View.GONE);
            }


            total_amountTV.setText(getString(R.string.rs, CommonUtility.convertionPaiseToRupee(paymentCod.total)));
            cod_balance_discountTV.setEnabled(true);
            code_FooterTitle.setText(getString(R.string.discount_msg_prepaidTV));
            code_FooterTitle.setVisibility(View.VISIBLE);


//            if (StaticMap.COD_DISCOUNT != null && !TextUtils.isEmpty(StaticMap.COD_DISCOUNT))
//                code_FooterTitle.setText(context.getString(R.string.discount_msg_prepaidTV, StaticMap.COD_DISCOUNT));
//            else
//                code_FooterTitle.setText(context.getString(R.string.discount_msg_prepaidTV, "2%"));

        } else {
            upfront_paymentRL.setVisibility(View.VISIBLE);
            codRL.setVisibility(View.GONE);
            prepaid_paymentTV.setText(getString(R.string.rs, CommonUtility.convertionPaiseToRupee(paymentCod.prepay.total)));
            cod_balanceTV.setText(getString(R.string.rs, CommonUtility.convertionPaiseToRupee(paymentCod.prepay.pending)));
            prepaid_discountTV.setText(getString(R.string.prepaid_charges, paymentCod.prepay.pcent + ""));
            int cod_pcent = 100 - paymentCod.prepay.pcent;
            cod_balance_discountTV.setText(getString(R.string.cod_balance, cod_pcent));
            cod_balance_discountTV.setEnabled(false);
            code_FooterTitle.setVisibility(View.GONE);


        }
    }

    private void initAdapter() {
        String[] prepaidOption = {getString(R.string.credit_pay), getString(R.string.netbanking_pay), getString(R.string.upi)};
        String[] prepaidID = {"card", "nb", "upi"};
        ArrayList<PrepaidData> datas = new ArrayList<>();
        PrepaidData prepaidData;
        for (int i = 0; i < prepaidOption.length; i++) {
            prepaidData = new PrepaidData();
            prepaidData.setOption(prepaidOption[i]);
            prepaidData.methodId = prepaidID[i];
            datas.add(prepaidData);
        }
        if (paymentAdapter == null)
            paymentAdapter = new CheckoutPaymentAdapter(getContext(), datas, key);
        payment_optionRV.setAdapter(paymentAdapter);
        payment_optionRV.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == place_orderBTN.getId() || v.getId() == small_amount_paymentBtn.getId()) {
          final   String selectedPaymentMode = paymentAdapter.getSelectedPaymentMethod();



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
                                        //PartsEazyEventBus.getInstance().postEvent(EventConstant.PLACE_ORDER, key, selectedPaymentMode);
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

        }
// else if (v.getId() == small_amount_paymentBtn.getId()) {
//            String selectedPaymentMode = paymentAdapter.getSelectedPaymentMethod();
//
//            PartsEazyEventBus.getInstance().postEvent(EventConstant.PLACE_ORDER, key, selectedPaymentMode);
//        }
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.CHECKOUT_ORDER_ERROR) {
            Boolean ISENABLED = (Boolean) eventObject.objects[0];
            place_orderBTN.setEnabled(ISENABLED);
        }

        if (eventObject.id == EventConstant.SELCTED_PAY_METHOD) {
            String paymentMethod = (String) eventObject.objects[1];
            if (paymentMethod.equals(key)) {
                place_orderBTN.setEnabled(true);
            }

        }
    }
}

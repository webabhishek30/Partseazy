package com.partseazy.android.ui.adapters.credit;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.checkout.CreditFragment;
import com.partseazy.android.ui.model.credit.CreditOptionList;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by CAN Solutions on 1/2/2017.
 */

public class CreditOptionAdapter extends RecyclerView.Adapter {

    private List<CreditOptionList> mData;
    private BaseFragment mContext;
    private int amount;
    private String selectedPaymentMode;
    private String parentPaymentMethod;

    public CreditOptionAdapter(CreditFragment context, List<CreditOptionList> lists, int amount, String payMethod) {
        mData = lists;
        mContext = context;
        this.amount = amount;
        parentPaymentMethod = payMethod;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CreditOptionAdapterVH(layoutInflater.inflate(R.layout.row_payment_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CreditOptionAdapterVH adapterVH = (CreditOptionAdapterVH) holder;
        final CreditOptionList optionList = mData.get(position);
        adapterVH.creditRB.setText(optionList.days + "");
        adapterVH.creditRB.setTag("" + optionList.days);

        String intrest = String.valueOf(optionList.interest);
        adapterVH.intrest_rateTV.setText(mContext.getString(R.string.intrest_value, intrest));
        final int totalAmount = (int) getIntrestAmnt(amount, optionList.interest);
        adapterVH.intrest_amountTV.setText(mContext.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(totalAmount)));
        if (mData.get(position).getChooseOption() == 1)
            adapterVH.creditRB.setChecked(true);
        else adapterVH.creditRB.setChecked(false);

        adapterVH.creditRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPaymentMode = (String) adapterVH.creditRB.getTag();
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELCTED_PAY_METHOD);

                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).setChooseOption(0);
                }
                if (mData.get(position).getChooseOption() == 0)
                    mData.get(position).setChooseOption(1);
                else mData.get(position).setChooseOption(0);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.INTREST_OPTION, totalAmount, false);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void notifyCreditAdapter(List<CreditOptionList> data) {
        mData = data;
        notifyDataSetChanged();
    }

    class CreditOptionAdapterVH extends BaseViewHolder {

        @BindView(R.id.creditRB)
        RadioButton creditRB;
        @BindView(R.id.intrest_rateTV)
        TextView intrest_rateTV;
        @BindView(R.id.intrest_amountTV)
        TextView intrest_amountTV;

        public CreditOptionAdapterVH(View itemView) {
            super(itemView);
        }
    }

    private double getIntrestAmnt(int amount, double intrest) {
        return (amount * intrest) / 100;
    }

    public String getSelectedPaymentMethod() {
        return selectedPaymentMode;
    }
}

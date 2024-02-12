package com.partseazy.android.ui.adapters.checkout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.prepaid.PrepaidData;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by can on 26/12/16.
 */

public class CheckoutPaymentAdapter extends RecyclerView.Adapter<CheckoutPaymentAdapter.CheckoutPaymentAdapterVH> {


    private ArrayList<PrepaidData> mData;
    private String selectedPaymentMode;
    private String parentPaymentMethod;
    private Context context;

    public CheckoutPaymentAdapter(Context context, ArrayList<PrepaidData> datas, String payMethod) {
        this.context = context;
        mData = datas;
        parentPaymentMethod = payMethod;
    }


    @Override
    public CheckoutPaymentAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CheckoutPaymentAdapterVH(inflater.inflate(R.layout.row_pay_prepaid, parent, false));
    }

    @Override
    public void onBindViewHolder(final CheckoutPaymentAdapterVH holder, final int position) {

        PrepaidData prepaidData = mData.get(position);
        holder.payment_typeRB.setText(prepaidData.getOption());

        if (prepaidData.getOption().equals(context.getString(R.string.credit_pay))) {
            holder.paymentIconIV.setImageResource(R.drawable.credit_card_icon);
        } else if (prepaidData.getOption().equals(context.getString(R.string.netbanking_pay))) {
            holder.paymentIconIV.setImageResource(R.drawable.net_banking_icon);
        } else if (prepaidData.getOption().equals(context.getString(R.string.upi))) {
            holder.paymentIconIV.setImageResource(R.drawable.upi_icon);
        }


        if (prepaidData.getChooseOption() == 1) {
            holder.payment_typeRB.setChecked(true);

        } else holder.payment_typeRB.setChecked(false);


        holder.payment_typeRB.setTag(prepaidData.methodId);
        holder.payment_typeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPaymentMode = (String) holder.payment_typeRB.getTag();
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELCTED_PAY_METHOD, selectedPaymentMode, parentPaymentMethod);


                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).setChooseOption(0);
                }
                if (mData.get(position).getChooseOption() == 0)
                    mData.get(position).setChooseOption(1);
                else mData.get(position).setChooseOption(0);

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CheckoutPaymentAdapterVH extends BaseViewHolder {

        @BindView(R.id.payment_typeRB)
        RadioButton payment_typeRB;
        @BindView(R.id.paymentIconIV)
        ImageView paymentIconIV;

        public CheckoutPaymentAdapterVH(View itemView) {
            super(itemView);
        }
    }

    public String getSelectedPaymentMethod() {
        return selectedPaymentMode;
    }

    public boolean isAnyOptionAreadySelected() {

        for (PrepaidData data : mData) {
            if (data.getChooseOption() == 1)
                return true;
        }

        return false;
    }

}

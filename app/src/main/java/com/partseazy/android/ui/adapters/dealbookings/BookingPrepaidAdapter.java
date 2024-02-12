package com.partseazy.android.ui.adapters.dealbookings;

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

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 21/7/17.
 */

public class BookingPrepaidAdapter extends RecyclerView.Adapter<BookingPrepaidAdapter.PaymentOptionVH> {


    private List<PrepaidData> mData;
    private String selectedPaymentMode;
    private String parentPaymentMethod;
    private Context context;
    private boolean isFromBookingSummary;

    public BookingPrepaidAdapter(Context context, List<PrepaidData> datas, String payMethod, boolean isFromBookingSummary) {
        this.context = context;
        mData = datas;
        parentPaymentMethod = payMethod;
        this.isFromBookingSummary = isFromBookingSummary;

    }


    @Override
    public PaymentOptionVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PaymentOptionVH(inflater.inflate(R.layout.row_pay_prepaid, parent, false));
    }

    @Override
    public void onBindViewHolder(final PaymentOptionVH holder, final int position) {

        PrepaidData prepaidData = mData.get(position);
        holder.payment_typeRB.setText(prepaidData.getOption());

        if (context.getString(R.string.credit_pay).equals(prepaidData.getOption())) {
            holder.paymentIconIV.setImageResource(R.drawable.credit_card_icon);
        } else if (context.getString(R.string.netbanking_pay).equals(prepaidData.getOption())) {
            holder.paymentIconIV.setImageResource(R.drawable.net_banking_icon);
        } else if (context.getString(R.string.upi).equals(prepaidData.getOption())) {
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
                if (isFromBookingSummary) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.DEAL_BOOKING_PAYMENT_FROM_SUMMARY, selectedPaymentMode, parentPaymentMethod);

                } else {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.DEAL_BOOKING_PAYMENT_METHOD, selectedPaymentMode, parentPaymentMethod);
                }


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

    class PaymentOptionVH extends BaseViewHolder {

        @BindView(R.id.payment_typeRB)
        RadioButton payment_typeRB;
        @BindView(R.id.paymentIconIV)
        ImageView paymentIconIV;

        public PaymentOptionVH(View itemView) {
            super(itemView);
        }
    }


}


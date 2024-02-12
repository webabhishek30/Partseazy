package com.partseazy.android.ui.adapters.checkout;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.cart_checkout.ShippinMethodData;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 2/3/17.
 */


public class ShippingRadioAdapter extends RecyclerView.Adapter<ShippingRadioAdapter.CheckoutPaymentAdapterVH> {


    private List<ShippinMethodData> itemList;
    private String selectedMode;
    private Context context;
    private boolean isFromCart;

    public ShippingRadioAdapter(Context context, List<ShippinMethodData> itemList, String selectedMode,boolean isFromCart) {
        this.context = context;
        this.itemList = itemList;
        this.selectedMode = selectedMode;
        this.isFromCart = isFromCart;
    }


    @Override
    public CheckoutPaymentAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CheckoutPaymentAdapterVH(inflater.inflate(R.layout.row_shipping_radio, parent, false));
    }

    @Override
    public void onBindViewHolder(final CheckoutPaymentAdapterVH holder, final int position) {

        ShippinMethodData shippingRadioData = itemList.get(position);
        final Map<String, String> availableMethodMap = StaticMap.shippingMap;
        String shippingText = availableMethodMap.get(shippingRadioData.type.toString());
        holder.itemRB.setText(context.getString(R.string.radio_btn_txt, shippingText, CommonUtility.convertionPaiseToRupee(shippingRadioData.charge)));


        if (shippingRadioData.getChooseOption() == 1 || selectedMode.equals(shippingRadioData.type))
            holder.itemRB.setChecked(true);
        else holder.itemRB.setChecked(false);

        if (shippingRadioData.type.equals("self")) {
            holder.itemIconIV.setVisibility(View.VISIBLE);
            holder.itemIconIV.setImageResource(R.drawable.icon_info_green);
        } else {
            holder.itemIconIV.setVisibility(View.INVISIBLE);
        }

        holder.itemRB.setTag(shippingRadioData.type);
        holder.itemRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectedMode = (String) holder.itemRB.getTag();
                if(isFromCart) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.POST_SELECTED_SHIPPING, selectedMode);
                }else{
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CHECKOUT_POST_SELECTED_SHIPPING, selectedMode);
                }

                for (int i = 0; i < itemList.size(); i++) {
                    itemList.get(i).setChooseOption(0);
                }
                if (itemList.get(position).getChooseOption() == 0)
                    itemList.get(position).setChooseOption(1);
                else itemList.get(position).setChooseOption(0);

                notifyDataSetChanged();
            }
        });

        holder.itemIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context != null) {
                    DialogUtil.showAlertDialog((BaseActivity)context, true, null, context.getString(R.string.self_pick_up), context.getString(R.string.OK)
                            , null, null, new DialogListener() {
                                @Override
                                public void onPositiveButton(DialogInterface dialog) {

                                    KeyPadUtility.hideSoftKeypad((BaseActivity)context);

                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class CheckoutPaymentAdapterVH extends BaseViewHolder {

        @BindView(R.id.itemRB)
        RadioButton itemRB;
        @BindView(R.id.itemIconIV)
        ImageView itemIconIV;

        public CheckoutPaymentAdapterVH(View itemView) {
            super(itemView);
        }
    }


}


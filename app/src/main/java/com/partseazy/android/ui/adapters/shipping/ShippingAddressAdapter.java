package com.partseazy.android.ui.adapters.shipping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by can on 22/12/16.
 */

public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ShippingAddressAdapterVH> {

    private List<ShippingAddressDetail> mAddressDetailList;
    int preSelectedAddressIndex;
    private Context context;
    private boolean isFromBilling;

    public ShippingAddressAdapter(Context context,List<ShippingAddressDetail> addressDetails, int preSelectedAddressIndex,boolean isFromBilling ) {
        this.context = context;
        mAddressDetailList = addressDetails;
        this.preSelectedAddressIndex = preSelectedAddressIndex;
        this.isFromBilling = isFromBilling;
    }

    @Override
    public ShippingAddressAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ShippingAddressAdapterVH(inflater.inflate(R.layout.row_shipping_address, parent, false));
    }

    @Override
    public void onBindViewHolder(final ShippingAddressAdapterVH holder, final int position) {

        final ShippingAddressDetail addressDetail = mAddressDetailList.get(position);

        if(isFromBilling) {
            if (addressDetail.billingName != null) {
                holder.nameTV.setText(addressDetail.billingName);
                holder.nameTV.setVisibility(View.VISIBLE);
            }else{
                holder.nameTV.setVisibility(View.GONE);
            }
        }else{
            if (addressDetail.name != null) {
                holder.nameTV.setText(addressDetail.name);
                holder.nameTV.setVisibility(View.VISIBLE);
            }else{
                holder.nameTV.setVisibility(View.GONE);
            }
        }

        if(addressDetail.gstn!=null && !addressDetail.gstn.equals(""))
        {
            holder.gstnTV.setText(context.getString(R.string.gstn_caps_str,addressDetail.gstn.toUpperCase()));
            holder.gstnTV.setVisibility(View.VISIBLE);
        }else{
            holder.gstnTV.setVisibility(View.GONE);
        }

        holder.addressTV.setText(addressDetail.street + ", " + addressDetail.city + ", " + addressDetail.state + " - " + addressDetail.pincode);

        if (addressDetail.landmark != null) {
            holder.landmarkTV.setText(PartsEazyApplication.getInstance().getBaseContext().getString(R.string.shipping_landmark, addressDetail.landmark));
            holder.landmarkTV.setVisibility(View.VISIBLE);
        } else {
            holder.landmarkTV.setVisibility(View.GONE);
        }
        holder.mobileTV.setText(PartsEazyApplication.getInstance().getBaseContext().getString(R.string.country_code) + addressDetail.mobile);

        if (addressDetail.selectedItem) {
            holder.select_addressRL.setBackgroundResource(R.drawable.rectangle_green_border);
            holder.checkIV.setVisibility(View.VISIBLE);
        } else {
            holder.select_addressRL.setBackgroundResource(R.drawable.rectangle_grey_border_white_solid);
            holder.checkIV.setVisibility(View.GONE);
        }




        holder.select_addressRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < mAddressDetailList.size(); i++) {
                    mAddressDetailList.get(i).selectedItem = false;
                }

                if (!mAddressDetailList.get(position).selectedItem) {
                    mAddressDetailList.get(position).selectedItem = true;
                } else if (mAddressDetailList.get(position).selectedItem) {
                    mAddressDetailList.get(position).selectedItem = false;
                }
                if(isFromBilling ) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.BILLING_ADDRESS, position, false,addressDetail);
                }else{
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.SHIPPING_ADDRESS, position, false,addressDetail);
                }

                notifyDataSetChanged();
            }
        });
    }


    public void notifyAdapter(List<ShippingAddressDetail> addressDetails, int preSelectedAddressIndex) {
        mAddressDetailList = addressDetails;
        this.preSelectedAddressIndex = preSelectedAddressIndex;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAddressDetailList.size();
    }

    class ShippingAddressAdapterVH extends BaseViewHolder {

        @BindView(R.id.select_addressRL)
        RelativeLayout select_addressRL;
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
        @BindView(R.id.checkIV)
        ImageView checkIV;

        @BindView(R.id.gstnTV)
        protected TextView gstnTV;

        ShippingAddressAdapterVH(View itemView) {
            super(itemView);
        }
    }
}

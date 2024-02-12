package com.partseazy.android.ui.adapters.shipping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.shippingaddress.AddNewShippingAddressFragment;

import butterknife.BindView;

/**
 * Created by can on 22/12/16.
 */

public class ShippingDialogAdapter extends RecyclerView.Adapter<ShippingDialogAdapter.ShippingDialogAdapterVH> {

    private String [] mOptionName; //TODO for Dummy UI

    public ShippingDialogAdapter(AddNewShippingAddressFragment context, String []optionName){
        mOptionName=optionName;
    }

    @Override
    public ShippingDialogAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ShippingDialogAdapterVH(inflater.inflate(R.layout.row_popup_deliveryoption, parent, false));
    }

    @Override
    public void onBindViewHolder(ShippingDialogAdapterVH holder, int position) {
        holder.optionTV.setText(mOptionName[position]);
    }

    @Override
    public int getItemCount() {
        return mOptionName.length;
    }

    class ShippingDialogAdapterVH extends BaseViewHolder{

        @BindView(R.id.optionTV)
        TextView optionTV;

        public ShippingDialogAdapterVH(View itemView) {
            super(itemView);
        }
    }
}

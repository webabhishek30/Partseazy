package com.partseazy.android.ui.adapters.deals.booking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.bookingorder.summary.Sku;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 21/6/17.
 */

public class BookingSummarySkuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Sku> skuList;
    private LayoutInflater layoutInflater;


    public BookingSummarySkuAdapter(Context context, List<Sku> skuList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.skuList = skuList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkuItemVH(layoutInflater.inflate(R.layout.row_booking_sku_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SkuItemVH skuItemVH = (SkuItemVH) holder;

        Sku skuItem = skuList.get(position);

        skuItemVH.skuDescTV.setText(skuItem.desc);

        skuItemVH.qtyTV.setText(context.getString(R.string.cart_qty, skuItem.qty));
        skuItemVH.priceTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(skuItem.price)));


    }

    @Override
    public int getItemCount() {
        return skuList.size();
    }

    public class SkuItemVH extends BaseViewHolder {

        @BindView(R.id.skuDescTV)
        protected TextView skuDescTV;

        @BindView(R.id.priceTV)
        protected TextView priceTV;

        @BindView(R.id.qtyTV)
        protected TextView qtyTV;


        public SkuItemVH(View view) {
            super(view);
        }
    }


}


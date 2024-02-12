package com.partseazy.android.ui.adapters.deals.booking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.FinalDealSKU;
import com.partseazy.android.ui.model.deal.selldeallist.Sku;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 2/6/17.
 */

public class BookSkuItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Sku> skuList;
    private Map<Integer, FinalDealSKU> skuMap;
    private LayoutInflater layoutInflater;


    public BookSkuItemAdapter(Context context, List<Sku> skuList, Map<Integer, FinalDealSKU> skuMap) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.skuList = skuList;
        this.skuMap = skuMap;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkuItemVH(layoutInflater.inflate(R.layout.row_booking_sku_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SkuItemVH skuItemVH = (SkuItemVH) holder;

        Sku skuItem = skuList.get(position);

        FinalDealSKU finalDealSKU = getDealSKU(skuItem.id);

        if (finalDealSKU != null) {

            skuItemVH.skuDescTV.setText(skuItem.desc);
            skuItemVH.qtyTV.setText(context.getString(R.string.qty, finalDealSKU.selectedQty + ""));
            skuItemVH.priceTV.setText(context.getString(R.string.rs_str, (CommonUtility.convertionPaiseToRupee(skuItem.price) * finalDealSKU.selectedQty) + ""));
        } else {
            skuItemVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }


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

    private FinalDealSKU getDealSKU(int id) {
        for (Map.Entry<Integer, FinalDealSKU> finalDealSKUEntry : skuMap.entrySet()) {
            FinalDealSKU sku = finalDealSKUEntry.getValue();
            if (sku.skuId == id) {
                return sku;
            }

        }

        return null;
    }

}

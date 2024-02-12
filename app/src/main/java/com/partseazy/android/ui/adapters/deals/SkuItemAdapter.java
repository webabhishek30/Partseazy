package com.partseazy.android.ui.adapters.deals;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealDetailFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealDetailFragment;
import com.partseazy.android.ui.model.deal.selldeallist.DealItem;
import com.partseazy.android.ui.model.deal.selldeallist.Sku;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 29/5/17.
 */

public class SkuItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Sku> skuList;
    private LayoutInflater mInflater;
    private boolean isFromSell;
    private DealItem dealItem;

    public SkuItemAdapter(Context context, List<Sku> dealList, DealItem dealItem, boolean isFromSell) {
        this.context = context;
        this.skuList = dealList;
        this.dealItem = dealItem;
        this.isFromSell = isFromSell;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkuVH(mInflater.inflate(R.layout.row_sku_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SkuVH skuVH = (SkuVH) holder;
        Sku skuItem = skuList.get(position);


        skuVH.skuPriceTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(skuItem.price)));
        skuVH.skuMrpTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(skuItem.mrp)));

        skuVH.skuMrpTV.setPaintFlags(skuVH.skuMrpTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        skuVH.skuDiscountTV.setText(context.getString(R.string.off_str, skuItem.discount));
        if(skuItem.desc!=null) {
            skuVH.skuAttrTV.setText(skuItem.desc);
        }

        skuVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dealItem!=null) {
                    if (isFromSell) {
                        SellDealDetailFragment sellDealDetailFragment = SellDealDetailFragment.newInstance(dealItem.id, dealItem.name);
                        BaseFragment.addToBackStack((BaseActivity) context, sellDealDetailFragment, sellDealDetailFragment.getTag());
                    } else {
                        BuyDealDetailFragment sellDealDetailFragment = BuyDealDetailFragment.newInstance(dealItem.id, dealItem.name,true);
                        BaseFragment.addToBackStack((BaseActivity) context, sellDealDetailFragment, sellDealDetailFragment.getTag());

                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return skuList.size();
    }

    public class SkuVH extends BaseViewHolder {
        @BindView(R.id.skuAttrTV)
        protected TextView skuAttrTV;

        @BindView(R.id.skuPriceTV)
        protected TextView skuPriceTV;

        @BindView(R.id.skuMrpTV)
        protected TextView skuMrpTV;

        @BindView(R.id.skuDiscountTV)
        protected TextView skuDiscountTV;


        public SkuVH(View itemView) {
            super(itemView);
        }
    }
}

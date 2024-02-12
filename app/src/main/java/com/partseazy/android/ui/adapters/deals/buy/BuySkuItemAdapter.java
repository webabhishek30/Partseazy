package com.partseazy.android.ui.adapters.deals.buy;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.deal.selldeallist.Sku;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 29/5/17.
 */

public class BuySkuItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<Sku> skuList= new ArrayList<>();
    private LayoutInflater mInflater;

    private int totalPrice = 0;
    private int totalQuantity = 0;
    private boolean isBuyLanding = false;
    private Deal dealDetailHolder;


    public BuySkuItemAdapter(Context context, Deal dealDetailHolder, boolean isBuyLanding) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.dealDetailHolder = dealDetailHolder;
        this.skuList.addAll(dealDetailHolder.skus);
        this.isBuyLanding = isBuyLanding;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkuVH(mInflater.inflate(R.layout.row_buy_sell_sku_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SkuVH skuVH = (SkuVH) holder;
        final Sku skuItem = skuList.get(position);


        if (skuItem.desc != null && !skuItem.desc.equals("")) {
            skuVH.descTV.setText(skuItem.desc);
            skuVH.descTV.setVisibility(View.VISIBLE);
        } else {
            skuVH.descTV.setVisibility(View.GONE);
        }
        skuVH.descTV.setTag(skuItem);

        skuVH.moqTV.setText(context.getString(R.string.moq, skuItem.minQty + ""));

        skuVH.priceTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupee(skuItem.price) + ""));
        skuVH.mrpTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupee(skuItem.mrp) + ""));
        skuVH.mrpTV.setPaintFlags(skuVH.mrpTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        skuVH.skuDiscountTV.setText(context.getString(R.string.off_str, skuItem.discount));

        skuVH.stockTV.setText(context.getString(R.string.stock_qty, skuItem.stock));
        skuVH.soldTV.setText(context.getString(R.string.sold_qty, skuItem.sold));

        int claimPercentage = CommonUtility.getPercentage(skuItem.sold, skuItem.stock);
        skuVH.claimTV.setText(context.getString(R.string.claimed_percantage, claimPercentage + ""));
        if (claimPercentage > 5) {
            skuVH.simpleProgressBar.setProgress(claimPercentage);
        } else {
            skuVH.simpleProgressBar.setProgress(5);
        }

        if (isBuyLanding) {
            skuVH.editLL.setVisibility(View.VISIBLE);
            skuVH.stockLL.setVisibility(View.GONE);
            skuVH.editSKUTV.setVisibility(View.GONE);

        } else {
            skuVH.editLL.setVisibility(View.GONE);
            skuVH.stockLL.setVisibility(View.VISIBLE);
            if((dealDetailHolder.trade.active==0 )||(CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(dealDetailHolder.trade.endingAt) < 0))
            {
                skuVH.editSKUTV.setVisibility(View.GONE);
            }else{
                skuVH.editSKUTV.setVisibility(View.VISIBLE);
            }
        }


        skuVH.editSKUTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.EDIT_DEAL_SKU,skuItem);
            }
        });


        if ((skuItem.remaining == 0 || skuItem.remaining < skuItem.minQty)&& isBuyLanding) {
            skuVH.overlay.setVisibility(View.VISIBLE);
        } else {
            skuVH.increaseQtyTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Sku skuModel = (Sku) skuVH.descTV.getTag();
                    int quantity = Integer.parseInt(skuVH.itemQtyTV.getText().toString());

                    int maxItem = skuModel.maxQty;
                    if (skuModel.maxQty == 0) {
                        maxItem = skuModel.remaining;
                    } else if (skuModel.remaining < skuModel.maxQty) {
                        maxItem = skuModel.remaining;
                    }


                    if (quantity >= maxItem) {

                        Toast.makeText(context, context.getString(R.string.stock_max_message, maxItem), Toast.LENGTH_SHORT).show();
                        return;
                    }

//                quantity++;
//                totalPrice = totalPrice + skuModel.price;
//                totalQuantity = totalQuantity + 1;


                    if (quantity == 0) {
                        quantity = quantity + skuModel.minQty;
                        totalPrice = totalPrice + skuModel.price * skuModel.minQty;
                        totalQuantity = totalQuantity + skuModel.minQty;
                    } else {
                        quantity++;
                        totalPrice = totalPrice + skuModel.price;
                        totalQuantity = totalQuantity + 1;
                    }

                    int totalItemPrice = quantity * skuModel.price;
                    int totalItemMRP = quantity * skuModel.mrp;


                    skuVH.itemQtyTV.setText(quantity + "");
                    skuVH.priceTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(totalItemPrice)));

                    skuVH.mrpTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(totalItemMRP)));

                    PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DEAL_SKU, skuItem, quantity);

                }
            });

            skuVH.decreaseQtyTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Sku skuModel = (Sku) skuVH.descTV.getTag();
                    int quantity = Integer.parseInt(skuVH.itemQtyTV.getText().toString());


                    if (quantity > 0) {

                        if (quantity == skuModel.minQty) {

                            quantity = 0;
                            totalPrice = totalPrice - skuModel.price * skuModel.minQty;
                            totalQuantity = totalQuantity - skuModel.minQty;
                        } else {
                            quantity--;
                            totalPrice = totalPrice - skuModel.price;
                            totalQuantity = totalQuantity - 1;
                        }

                        if (quantity == 0)
                            //finalSelectedProduct.selectedAttrbiteMap.remove(model.productID);

                            if (totalPrice < 0)
                                totalPrice = 0;

                        if (totalQuantity < 0)
                            totalQuantity = 0;
                    }


                    int totalItemPrice = quantity * skuModel.price;
                    int totalItemMRP = quantity * skuModel.mrp;

                    if (totalItemPrice == 0) {
                        totalItemPrice = skuModel.price;
                    }

                    if (totalItemMRP == 0)
                        totalItemMRP = skuModel.mrp;

                    skuVH.itemQtyTV.setText(quantity + "");
                    skuVH.priceTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(totalItemPrice)));
                    skuVH.mrpTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(totalItemMRP)));

                    PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DEAL_SKU, skuItem, quantity);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return skuList.size();
    }

    public class SkuVH extends BaseViewHolder {

        @BindView(R.id.descTV)
        protected TextView descTV;

        @BindView(R.id.moqTV)
        protected TextView moqTV;

        @BindView(R.id.mrpTV)
        protected TextView mrpTV;

        @BindView(R.id.priceTV)
        protected TextView priceTV;

        @BindView(R.id.decrease_item_qtyTV)
        protected TextView decreaseQtyTV;

        @BindView(R.id.increase_item_qtyTV)
        protected TextView increaseQtyTV;

        @BindView(R.id.item_qtyTV)
        protected TextView itemQtyTV;

        @BindView(R.id.skuDiscountTV)
        protected TextView skuDiscountTV;

        @BindView(R.id.editLL)
        protected LinearLayout editLL;

        @BindView(R.id.stockLL)
        protected LinearLayout stockLL;

        @BindView(R.id.stockTV)
        protected TextView stockTV;

        @BindView(R.id.soldTV)
        protected TextView soldTV;

        @BindView(R.id.claimTV)
        protected TextView claimTV;


        @BindView(R.id.editSKUTV)
        protected TextView editSKUTV;

        @BindView(R.id.simpleProgressBar)
        protected ProgressBar simpleProgressBar;

        @BindView(R.id.overlay)
        protected LinearLayout overlay;

        public SkuVH(View itemView) {
            super(itemView);
        }
    }


}

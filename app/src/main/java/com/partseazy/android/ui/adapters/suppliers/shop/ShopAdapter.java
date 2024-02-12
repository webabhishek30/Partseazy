package com.partseazy.android.ui.adapters.suppliers.shop;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.map.ShopProfileMap;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.supplier.shop.SLIDEIMAGETYPE;
import com.partseazy.android.ui.fragments.supplier.shop.ShopDetailFragment;
import com.partseazy.android.ui.model.supplier.shop.Category;
import com.partseazy.android.ui.model.supplier.shop.Shop;
import com.partseazy.android.ui.model.supplier.shop.ShopInfo;
import com.partseazy.android.ui.model.supplier.shop.ShopResultData;
import com.partseazy.android.utility.CommonUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mInflater;
    private final Context mContext;
    private LinearLayoutManager horizontalLayoutManager = null;
    private List<Shop> shopList;
    private ShopImageSliderAdapter adapter;
    private Integer pageIndex;


    public ShopAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        shopList = new ArrayList<>();
        pageIndex = null;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new ShopImageVH(mInflater.inflate(R.layout.shop_card_view_item, parent, false));


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Shop shopItem = shopList.get(position);
//        Address address = shopList.get(position).address;

        if (holder instanceof ShopImageVH) {

            ShopImageVH productsViewHolder = (ShopImageVH) holder;
            setHorinotalImageAdapter(productsViewHolder, shopItem.shopInfo);

            if (shopItem.address != null) {
                productsViewHolder.retailerAddress1.setText(mContext.getString(R.string.pincode_str, shopItem.address.pincode));
                productsViewHolder.retailerAddress1.setVisibility(View.VISIBLE);
            } else {
                productsViewHolder.retailerAddress1.setVisibility(View.GONE);
            }

            if (shopItem.shopInfo.name != null) {
                productsViewHolder.retailerNameTV.setText(CommonUtility.getFormattedName(shopItem.shopInfo.name));

                if (shopItem.shopInfo.size != null)
                    productsViewHolder.retailerArea.setText(mContext.getString(R.string.shop_square_unit, ShopProfileMap.getShopSizeValue(shopItem.shopInfo.size)));

                if (shopItem.shopInfo.turnover != null)
                    productsViewHolder.retailerTurnover.setText(mContext.getString(R.string.shop_tunrover_unit, ShopProfileMap.getTurnOverValue(shopItem.shopInfo.turnover)));

                if (shopItem.categories != null && shopItem.categories.size() > 0) {
                    List<Category> catList = shopItem.categories;

                    if (catList.size() >= 1 && catList.get(0).categoryPath != null)
                        productsViewHolder.cat1.setText(catList.get(0).categoryPath.get(2).name);

                    if (catList.size() >= 2 && catList.get(1).categoryPath != null)
                        productsViewHolder.cat2.setText(catList.get(1).categoryPath.get(2).name);


                    if (catList.size() >= 3 && catList.get(2).categoryPath != null)
                        productsViewHolder.cat3.setText(catList.get(2).categoryPath.get(2).name);

                    if (catList.size() >= 4) {
                        productsViewHolder.catmore.setVisibility(View.VISIBLE);
                        productsViewHolder.catmore.setText(mContext.getString(R.string.more_catgs, catList.size() - 3));
                    } else {
                        productsViewHolder.catmore.setVisibility(View.GONE);

                    }
                }

                productsViewHolder.recyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShopDetailFragment shopDetailFragment = ShopDetailFragment.newInstance(shopItem.shopInfo.id, shopItem.shopInfo.name);
                        BaseFragment.addToBackStack((BaseActivity) mContext, shopDetailFragment, shopDetailFragment.getTag());

                    }
                });
                productsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShopDetailFragment shopDetailFragment = ShopDetailFragment.newInstance(shopItem.shopInfo.id, shopItem.shopInfo.name);
                        BaseFragment.addToBackStack((BaseActivity) mContext, shopDetailFragment, shopDetailFragment.getTag());

                    }
                });

            }


        }
    }


    @Override
    public int getItemCount() {
        return shopList.size();
    }


    private void setHorinotalImageAdapter(ShopImageVH productsViewHolder, ShopInfo shopInfo) {

        horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        productsViewHolder.recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new ShopImageSliderAdapter(mContext, shopInfo.images, SLIDEIMAGETYPE.RETAIL_LIST, shopInfo);
        productsViewHolder.recyclerView.setAdapter(adapter);
        productsViewHolder.recyclerView.setItemViewCacheSize(0);
        productsViewHolder.recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 0);
        productsViewHolder.recyclerView.setNestedScrollingEnabled(false);


    }


    class ShopImageVH extends BaseViewHolder {

        @BindView(R.id.recyclerView)
        public RecyclerView recyclerView;

        @BindView(R.id.reatilerNameTV)
        public TextView retailerNameTV;


        @BindView(R.id.retailerAddress1)
        public TextView retailerAddress1;


        @BindView(R.id.retailerArea)
        public TextView retailerArea;


        @BindView(R.id.retailerTurnover)
        public TextView retailerTurnover;


        @BindView(R.id.cat1)
        public TextView cat1;


        @BindView(R.id.cat2)
        public TextView cat2;

        @BindView(R.id.cat3)
        public TextView cat3;

        @BindView(R.id.catmore)
        public TextView catmore;


        public ShopImageVH(View itemView) {
            super(itemView);
        }
    }


    public void updateImageSliderAdapter(int p, ShopResultData retailerData) {


        if (pageIndex == null || pageIndex != p) {
            pageIndex = p;
            CustomLogger.d("Page Index has been udpated  " + pageIndex);

        } else {
            CustomLogger.d("Page Index I am returning since I am already supplied " + pageIndex);
            return;
        }


        if (shopList.isEmpty()) {
            shopList = retailerData.shops;
            notifyDataSetChanged();

        } else {
            int index = shopList.size();
            shopList.addAll(retailerData.shops);
            this.notifyItemRangeInserted(index, retailerData.shops.size());
        }


    }

    public void clearAdapter() {
        shopList.clear();
        pageIndex = null;
        this.notifyDataSetChanged();
    }


}


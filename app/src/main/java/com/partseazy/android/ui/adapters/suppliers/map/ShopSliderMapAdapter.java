package com.partseazy.android.ui.adapters.suppliers.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.map.ShopProfileMap;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.supplier.shop.ShopDetailFragment;
import com.partseazy.android.ui.model.supplier.shop.Address;
import com.partseazy.android.ui.model.supplier.shop.Category;
import com.partseazy.android.ui.model.supplier.shop.Image;
import com.partseazy.android.ui.model.supplier.shop.Shop;
import com.partseazy.android.ui.model.supplier.shop.ShopInfo;
import com.partseazy.android.ui.model.supplier.shop.ShopResultData;
import com.partseazy.android.utility.CommonUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 7/9/17.
 */

public class ShopSliderMapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Shop> shopList;
    private LayoutInflater mInflater;
    private Integer pageIndex;


    public ShopSliderMapAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        shopList = new ArrayList<>();
        pageIndex = null;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ShopVH(mInflater.inflate(R.layout.shop_map_bottom_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        ShopInfo shopInfo = shopList.get(position).shopInfo;
        Address address = shopList.get(position).address;

        if (holder instanceof ShopVH) {

            ShopVH shopVH = (ShopVH) holder;

            if (shopInfo.name != null)
                shopVH.retailerNameTV.setText(CommonUtility.getFormattedName(shopInfo.name));


            if (address!= null) {
                shopVH.retailerAddress1.setText(address.pincode);
                shopVH.retailerAddress1.setVisibility(View.VISIBLE);
            } else {
                shopVH.retailerAddress1.setVisibility(View.GONE);
            }


            if (shopInfo.size != null)
                shopVH.retailerArea.setText(mContext.getString(R.string.shop_square_unit, ShopProfileMap.getShopSizeValue(shopInfo.size)));

            if (shopInfo.turnover != null)
                shopVH.retailerTurnover.setText(mContext.getString(R.string.shop_tunrover_unit, ShopProfileMap.getTurnOverValue(shopInfo.turnover)));


            List<Category> catList = shopList.get(position).categories;

            if (catList != null) {

                if (catList.size() >= 1 && catList.get(0).categoryPath != null)
                    shopVH.cat1.setText(catList.get(0).categoryPath.get(2).name);

                if (catList.size() >= 2 && catList.get(1).categoryPath != null)
                    shopVH.cat2.setText(catList.get(1).categoryPath.get(2).name);


                if (catList.size() >= 3 && catList.get(2).categoryPath != null)
                    shopVH.cat3.setText(catList.get(2).categoryPath.get(2).name);

                if (catList.size() >= 4) {
                    shopVH.catmore.setVisibility(View.VISIBLE);
                    shopVH.catmore.setText(mContext.getString(R.string.more_catgs, catList.size() - 3));
                } else {
                    shopVH.catmore.setVisibility(View.GONE);

                }
            }


            shopVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShopDetailFragment shopDetailFragment = ShopDetailFragment.newInstance(shopList.get(position).shopInfo.id,shopList.get(position).shopInfo.name);
                    BaseFragment.addToBackStack((BaseActivity) mContext, shopDetailFragment, shopDetailFragment.getTag());

                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }


    public void updateImageList(List<Image> imageList) {

        notifyDataSetChanged();

    }


    class ShopVH extends BaseViewHolder {

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


        public ShopVH(View itemView) {
            super(itemView);
        }
    }


    public void updateAdapter(int p, ShopResultData retailerData) {

        if (shopList.isEmpty()) {
            shopList = retailerData.shops;
            notifyDataSetChanged();

        } else {
            int index = shopList.size();
            shopList.addAll(retailerData.shops);
            this.notifyItemRangeInserted(index, retailerData.shops.size());
        }


    }


}


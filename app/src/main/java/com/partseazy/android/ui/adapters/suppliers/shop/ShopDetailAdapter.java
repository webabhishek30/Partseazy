package com.partseazy.android.ui.adapters.suppliers.shop;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.map.ShopProfileMap;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.supplier.WeekDay;
import com.partseazy.android.ui.model.supplier.shop.Shop;
import com.partseazy.android.ui.widget.CircularImageView;
import com.partseazy.android.utility.ChatUtility;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;

import java.util.List;
import com.partseazy.android.R;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private final Context context;
    private BaseFragment baseFragment;
    private int mTotalItems = 6;
    private Shop shopData;

    private List<WeekDay> weekdayList;
    private List<String> categoryList;
    private ShopTimingsAdapter shopTimingsAdapter;

    private CategoryBrandAdapter categoryAdapter;
    private CategoryBrandAdapter brandAdapter;

    public ShopDetailAdapter(BaseFragment  baseFragment, Shop shopData) {
        this.context = baseFragment.getContext();
        this.mInflater = LayoutInflater.from(context);
        this.shopData = shopData;
        this.baseFragment = baseFragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_SHOP_INFO_CARD:
                return new ShopInfoViewHolder(mInflater.inflate(R.layout.row_shop_item_info, parent, false));

            case VIEW_SHOP_OWNER_CARD:
                return new ShopUserViewHolder(mInflater.inflate(R.layout.row_shop_owner_detail, parent, false));

            case VIEW_SHOP_DETAILS_CARD:
                return new ShopTimingsViewHolder(mInflater.inflate(R.layout.row_shop_timings_detail, parent, false));

            case VIEW_SHOP_DETAIL_CARD:
                return new ShopProfileViewHolder(mInflater.inflate(R.layout.row_shop_profile_detail, parent, false));

            case VIEW_SHOP_CATEGORY_BRAND_CARD:
                return new CategoryBrandVH(mInflater.inflate(R.layout.row_shop_category_brand, parent, false));

            case VIEW_SHOP_CONTACT_CARD:
                return new ContactViewHolder(mInflater.inflate(R.layout.row_retailer_contact_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ShopInfoViewHolder) {
            ShopInfoViewHolder shopInfoViewHolder = (ShopInfoViewHolder) holder;
            shopInfoViewHolder.shopNameTV.setText(CommonUtility.getFormattedName(shopData.shopInfo.name));
            String address = CommonUtility.getShopFormattedAddress(shopData.address);
            shopInfoViewHolder.shopAddressTV.setText(address);
            if(shopData.shopInfo.floor!=null) {
                shopInfoViewHolder.shopFloorTV.setText(ShopProfileMap.getShopFloorValue(shopData.shopInfo.floor));
                shopInfoViewHolder.shopFloorTV.setVisibility(View.VISIBLE);
            }else{
                shopInfoViewHolder.shopFloorTV.setVisibility(View.GONE);
            }

        }

        if (holder instanceof ShopTimingsViewHolder) {

            ShopTimingsViewHolder shopTimingsViewHolder = (ShopTimingsViewHolder) holder;

            if(shopData.shopInfo.openTime!=null && shopData.shopInfo.closeTime!=null)
            {
                shopTimingsViewHolder.openCloseTV.setText(CommonUtility.getShopOpeningClosingTime(shopData.shopInfo));
                shopTimingsViewHolder.openCloseTV.setVisibility(View.VISIBLE);
            }else {
                shopTimingsViewHolder.openCloseTV.setVisibility(View.GONE);
            }

            if (weekdayList == null) {
                weekdayList = ShopProfileMap.getWeekDays(shopData.shopInfo.weeklyOff);
            }

            if (weekdayList != null && weekdayList.size() > 0) {
                shopTimingsAdapter = new ShopTimingsAdapter(context, weekdayList, false);
                shopTimingsViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                shopTimingsViewHolder.recyclerView.setAdapter(shopTimingsAdapter);
                shopTimingsViewHolder.recyclerView.setNestedScrollingEnabled(false);
            } else {
                shopTimingsViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        }

        if (holder instanceof ShopUserViewHolder) {
            ShopUserViewHolder shopUserViewHolder = (ShopUserViewHolder) holder;
            shopUserViewHolder.ownerNameTV.setText(CommonUtility.getFormattedName(shopData.user.name));
            shopUserViewHolder.sellerCircularImage.setDefaultImageResId(R.drawable.supplier_icon);
            shopUserViewHolder.ownerMobileTV.setText(shopData.user.mobile);
            shopUserViewHolder.ownerMobileTV.setVisibility(View.GONE);

            shopUserViewHolder.chatNowTV.setText(context.getString(R.string.chat_with,shopData.user.name));
            shopUserViewHolder.chatNowTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseFragment.showProgressDialog();
                    ChatUtility chat = new ChatUtility(baseFragment, shopData.user.id, shopData.user.name);
                    chat.startChatting();
                }
            });

        }

        if (holder instanceof ShopProfileViewHolder) {
            ShopProfileViewHolder shopProfileViewHolder = (ShopProfileViewHolder) holder;

            if (shopData.shopInfo.format != null) {
                shopProfileViewHolder.shopFormatTV.setText(ShopProfileMap.getShopFormatValue(shopData.shopInfo.format));
                shopProfileViewHolder.shopFormatTV.setVisibility(View.VISIBLE);
            } else {
                shopProfileViewHolder.shopFormatTV.setVisibility(View.GONE);
            }

            if (shopData.shopInfo.footfall != null) {
                shopProfileViewHolder.footfallTV.setText(ShopProfileMap.getFootfallValue(shopData.shopInfo.footfall));
                shopProfileViewHolder.footfallHeadingTV.setPaintFlags(shopProfileViewHolder.footfallTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                shopProfileViewHolder.footfallLL.setVisibility(View.VISIBLE);
            } else {
                shopProfileViewHolder.footfallLL.setVisibility(View.GONE);
            }

            if (shopData.shopInfo.size != null) {
                shopProfileViewHolder.sizeTV.setText(ShopProfileMap.getShopSizeValue(shopData.shopInfo.size));
                shopProfileViewHolder.sizeHeadingTV.setPaintFlags(shopProfileViewHolder.shopFormatTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                shopProfileViewHolder.sizeLL.setVisibility(View.VISIBLE);
            } else {
                shopProfileViewHolder.sizeLL.setVisibility(View.GONE);
            }


            if (shopData.shopInfo.turnover != null) {
                shopProfileViewHolder.turnOverTV.setText(ShopProfileMap.getTurnOverValue(shopData.shopInfo.turnover));
                shopProfileViewHolder.turnOverHeadingTV.setPaintFlags(shopProfileViewHolder.footfallTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                shopProfileViewHolder.turnOverLL.setVisibility(View.VISIBLE);
            } else {
                shopProfileViewHolder.turnOverLL.setVisibility(View.GONE);

            }
        }
        if (holder instanceof ContactViewHolder) {
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0,0));

            if (shopData.user != null) {
                String userName = CommonUtility.getFormattedName(shopData.user.name);
                contactViewHolder.callTV.setText(context.getString(R.string.call_owner, userName));
                contactViewHolder.chatTV.setText(context.getString(R.string.chat_owner, userName));


                contactViewHolder.callRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonUtility.dialPhoneNumber(context, shopData.user.mobile);
                    }
                });
            }

        }

        if (holder instanceof CategoryBrandVH) {
            CategoryBrandVH categoryBrandVH = (CategoryBrandVH) holder;
            if (categoryList == null) {
                categoryList = ShopProfileMap.getCategoryList(shopData.categories);
            }
            if (categoryList != null && categoryList.size() > 0) {
                categoryAdapter = new CategoryBrandAdapter(context, categoryList);
                categoryBrandVH.categoryRV.setLayoutManager(new LinearLayoutManager(context));
                categoryBrandVH.categoryRV.setAdapter(categoryAdapter);
                categoryBrandVH.categoryRV.setNestedScrollingEnabled(false);
                categoryBrandVH.categoryRV.setVisibility(View.VISIBLE);

            }else{
                categoryBrandVH.categoryRV.setVisibility(View.GONE);
            }

            if(shopData.shopInfo.brands!=null && shopData.shopInfo.brands.size()>0) {
                categoryBrandVH.headingTV.setText(context.getString(R.string.category_and_brands));
                brandAdapter = new CategoryBrandAdapter(context, shopData.shopInfo.brands);
                categoryBrandVH.brandRV.setLayoutManager(new LinearLayoutManager(context));
                categoryBrandVH.brandRV.setAdapter(brandAdapter);
                categoryBrandVH.brandRV.setNestedScrollingEnabled(false);
                categoryBrandVH.brandRV.setVisibility(View.VISIBLE);
            }else{
                categoryBrandVH.headingTV.setText(context.getString(R.string.categories));
                categoryBrandVH.brandRV.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mTotalItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HolderType.VIEW_SHOP_INFO_CARD.ordinal();
        } else if (position == 1) {
            return HolderType.VIEW_SHOP_OWNER_CARD.ordinal();
        } else if (position == 2) {
            return HolderType.VIEW_SHOP_DETAILS_CARD.ordinal();
        } else if (position == 3) {
            return HolderType.VIEW_SHOP_DETAIL_CARD.ordinal();
        } else if (position == 4) {
            return HolderType.VIEW_SHOP_CATEGORY_BRAND_CARD.ordinal();
        } else if (position == 5) {
            return HolderType.VIEW_SHOP_CONTACT_CARD.ordinal();
        }
        return 0;
    }


    class ShopInfoViewHolder extends BaseViewHolder {
        @BindView(R.id.shopNameTV)
        protected TextView shopNameTV;
        //
        @BindView(R.id.shopFloorTV)
        protected TextView shopFloorTV;

        @BindView(R.id.shopAddressTV)
        protected TextView shopAddressTV;


        public ShopInfoViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ShopTimingsViewHolder extends BaseViewHolder {
        @BindView(R.id.recyclerView)
        public RecyclerView recyclerView;

        @BindView(R.id.openCloseTV)
        public TextView openCloseTV;


        public ShopTimingsViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ShopUserViewHolder extends BaseViewHolder {
        @BindView(R.id.ownerNameTV)
        protected TextView ownerNameTV;
        @BindView(R.id.ownerMobileTV)
        protected TextView ownerMobileTV;
        @BindView(R.id.sellerCircularImage)
        protected CircularImageView sellerCircularImage;

        @BindView(R.id.chatNowTV)
        protected TextView chatNowTV;

        public ShopUserViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ShopProfileViewHolder extends BaseViewHolder {

        @BindView(R.id.sizeTV)
        protected TextView sizeTV;

        @BindView(R.id.footfallTV)
        protected TextView footfallTV;

        @BindView(R.id.turnOverTV)
        protected TextView turnOverTV;

        @BindView(R.id.shopFormatTV)
        protected TextView shopFormatTV;

        @BindView(R.id.footfallLL)
        protected LinearLayout footfallLL;

        @BindView(R.id.sizeLL)
        protected LinearLayout sizeLL;

        @BindView(R.id.turnOverLL)
        protected LinearLayout turnOverLL;

        @BindView(R.id.sizeHeadingTV)
        protected TextView sizeHeadingTV;

        @BindView(R.id.footfallHeadingTV)
        protected TextView footfallHeadingTV;

        @BindView(R.id.turnOverHeadingTV)
        protected TextView turnOverHeadingTV;


        public ShopProfileViewHolder(View itemView) {
            super(itemView);
        }
    }

    class CategoryBrandVH extends BaseViewHolder {

        @BindView(R.id.categoryRV)
        protected RecyclerView categoryRV;

        @BindView(R.id.brandRV)
        protected RecyclerView brandRV;

        @BindView(R.id.headingTV)
        protected TextView headingTV;

        public CategoryBrandVH(View itemView) {
            super(itemView);
        }
    }

    class ContactViewHolder extends BaseViewHolder {
        @BindView(R.id.callTV)
        protected TextView callTV;

        @BindView(R.id.callRL)
        protected RelativeLayout callRL;

        @BindView(R.id.chatTV)
        protected TextView chatTV;

        @BindView(R.id.sendPortforlioTV)
        protected TextView sendPortforlioTV;

        public ContactViewHolder(View itemView) {
            super(itemView);
        }
    }

}


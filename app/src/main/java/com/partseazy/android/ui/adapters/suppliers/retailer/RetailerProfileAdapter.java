package com.partseazy.android.ui.adapters.suppliers.retailer;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.map.ShopProfileMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.suppliers.shop.CategoryBrandAdapter;
import com.partseazy.android.ui.fragments.supplier.retailer.AddBasicRetailerInfo;
import com.partseazy.android.ui.model.supplier.shop.Shop;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 8/9/17.
 */

public class RetailerProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private final Context context;
    private ImageLoader imageLoader;
    private int mTotalItems = 4;
    private Shop shopData;

    private ShopPictureAdapter shopPictureAdapter;
    private CategoryBrandAdapter categoryAdapter,brandAdapter;


    public RetailerProfileAdapter(Context context, Shop shop) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.shopData = shop;
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_RETAILER_BASIC_INFO:
                return new BasicInfoVH(mInflater.inflate(R.layout.row_retailer_info_item, parent, false));

            case VIEW_RETAILER_KYC_INFO:
                return new KnowYourCustomVH(mInflater.inflate(R.layout.row_retailer_kyc_info_item, parent, false));

            case VIEW_RETAILER_BUSINESS_PROFILE:
                return new BusinessProfileVH(mInflater.inflate(R.layout.row_retailer_business_profile_item, parent, false));

            case VIEW_RETAILER_FINANCIAL_PROFILE:
                return new RetailerFinanceProfileVH(mInflater.inflate(R.layout.row_retailer_finance_profile_item, parent, false));


            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BasicInfoVH) {
            BasicInfoVH basicInfoVH = (BasicInfoVH) holder;
            basicInfoVH.userImageView.setDefaultImageResId(R.drawable.supplier_icon);

            basicInfoVH.nameTV.setText(DataStore.getUserName(context));
            basicInfoVH.emailTV.setText(DataStore.getUserEmail(context));
            basicInfoVH.mobileTV.setText(DataStore.getUserPhoneNumber(context));
            basicInfoVH.shopNameTV.setText(shopData.shopInfo.name);

        }

        if(holder instanceof KnowYourCustomVH)
        {
            KnowYourCustomVH knowYourCustomVH = (KnowYourCustomVH)holder;
            // TODO  Replace this code with proper images urls like different urls for visiing card and shop front
            knowYourCustomVH.noShopFrontRL.setVisibility(View.GONE);
            knowYourCustomVH.noVisitingCardRL.setVisibility(View.GONE);
            knowYourCustomVH.visitingCardPicLL.setVisibility(View.GONE);
            knowYourCustomVH.shopFrontLL.setVisibility(View.GONE);
            if(shopData.shopInfo.images!=null && shopData.shopInfo.images.size()>0)
            {

                String vistingcardURL = CommonUtility.getFormattedImageUrl(shopData.shopInfo.images.get(0).src, knowYourCustomVH.visitingCardIV, CommonUtility.IMGTYPE.FULLIMG);
                CommonUtility.setImageSRC(imageLoader, knowYourCustomVH.visitingCardIV, vistingcardURL);

                String shopPicURL = CommonUtility.getFormattedImageUrl(shopData.shopInfo.images.get(0).src, knowYourCustomVH.shopFrontIV, CommonUtility.IMGTYPE.FULLIMG);
                CommonUtility.setImageSRC(imageLoader, knowYourCustomVH.shopFrontIV, shopPicURL);

                knowYourCustomVH.visitingCardPicLL.setVisibility(View.VISIBLE);
                knowYourCustomVH.shopFrontLL.setVisibility(View.VISIBLE);
            }else {

                knowYourCustomVH.noShopFrontRL.setVisibility(View.VISIBLE);
                knowYourCustomVH.noVisitingCardRL.setVisibility(View.VISIBLE);
            }


            knowYourCustomVH.editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddBasicRetailerInfo addBasicRetailerInfo = AddBasicRetailerInfo.newInstance();
                    BaseFragment.addToBackStack((BaseActivity) context, addBasicRetailerInfo, addBasicRetailerInfo.getTag());
                }
            });

        }

        if(holder instanceof BusinessProfileVH)
        {
            BusinessProfileVH businessProfileVH = (BusinessProfileVH)holder;
            businessProfileVH.shopPicLL.setVisibility(View.GONE);
            businessProfileVH.noShopPicRL.setVisibility(View.GONE);

            if(shopData.shopInfo.images!=null && shopData.shopInfo.images.size()>0)
            {
                shopPictureAdapter = new ShopPictureAdapter(context,shopData.shopInfo.images);
                businessProfileVH.shopPicRV.setLayoutManager(new GridLayoutManager(context,3));
                businessProfileVH.shopPicRV.setAdapter(shopPictureAdapter);
                businessProfileVH.shopPicLL.setVisibility(View.VISIBLE);

            }else{
                businessProfileVH.noShopPicRL.setVisibility(View.VISIBLE);
            }


            List<String> categoryList = ShopProfileMap.getCategoryList(shopData.categories);
            if (categoryList != null && categoryList.size() > 0) {
                categoryAdapter = new CategoryBrandAdapter(context, categoryList);
                businessProfileVH.categoryRV.setLayoutManager(new GridLayoutManager(context,2));
                businessProfileVH.categoryRV.setAdapter(categoryAdapter);
                businessProfileVH.categoryRV.setNestedScrollingEnabled(false);
                businessProfileVH.categoryLL.setVisibility(View.VISIBLE);

            }else{
                businessProfileVH.categoryLL.setVisibility(View.GONE);
            }




            if(shopData.shopInfo.brands!=null && shopData.shopInfo.brands.size()>0) {
                brandAdapter = new CategoryBrandAdapter(context, shopData.shopInfo.brands);
                businessProfileVH.brandRV.setLayoutManager(new GridLayoutManager(context,2));
                businessProfileVH.brandRV.setAdapter(brandAdapter);
                businessProfileVH.brandRV.setNestedScrollingEnabled(false);
                businessProfileVH.brandLL.setVisibility(View.VISIBLE);
            }else{
                businessProfileVH.brandLL.setVisibility(View.GONE);
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
            return HolderType.VIEW_RETAILER_BASIC_INFO.ordinal();
        } else if (position == 1) {
            return HolderType.VIEW_RETAILER_KYC_INFO.ordinal();
        } else if (position == 2) {
            return HolderType.VIEW_RETAILER_BUSINESS_PROFILE.ordinal();
        } else if (position == 3) {
            return HolderType.VIEW_RETAILER_FINANCIAL_PROFILE.ordinal();
        }
        return 0;
    }

    public class BasicInfoVH extends BaseViewHolder {
        @BindView(R.id.userImageView)
        NetworkImageView userImageView;

        @BindView(R.id.nameTV)
        protected TextView nameTV;

        @BindView(R.id.emailTV)
        protected TextView emailTV;

        @BindView(R.id.mobileTV)
        protected TextView mobileTV;

        @BindView(R.id.shopNameTV)
        protected TextView shopNameTV;



        public BasicInfoVH(View itemView) {
            super(itemView);
        }
    }

    public class KnowYourCustomVH extends BaseViewHolder {

        @BindView(R.id.editIV)
        protected ImageView editIV;

        @BindView(R.id.visitingCardPicLL)
        protected LinearLayout visitingCardPicLL;

        @BindView(R.id.shopFrontLL)
        protected LinearLayout shopFrontLL;

        @BindView(R.id.noVisitingCardRL)
        protected RelativeLayout noVisitingCardRL;

        @BindView(R.id.noShopFrontRL)
        protected RelativeLayout noShopFrontRL;

        @BindView(R.id.shopFrontIV)
        protected NetworkImageView shopFrontIV;

        @BindView(R.id.visitingCardIV)
        protected NetworkImageView visitingCardIV;



        public KnowYourCustomVH(View itemView) {
            super(itemView);
        }
    }

    public class BusinessProfileVH extends BaseViewHolder {

        @BindView(R.id.editIV)
        protected ImageView editIV;


        @BindView(R.id.noShopPicRL)
        protected RelativeLayout noShopPicRL;

        @BindView(R.id.shopPicLL)
        protected LinearLayout shopPicLL;

        @BindView(R.id.shopPicRV)
        protected RecyclerView shopPicRV;

        @BindView(R.id.categoryRV)
        protected RecyclerView categoryRV;

        @BindView(R.id.brandRV)
        protected RecyclerView brandRV;

        @BindView(R.id.categoryLL)
        protected LinearLayout categoryLL;

        @BindView(R.id.brandLL)
        protected LinearLayout brandLL;



        public BusinessProfileVH(View itemView) {
            super(itemView);
        }
    }


    public class RetailerFinanceProfileVH extends BaseViewHolder {

        @BindView(R.id.editIV)
        protected ImageView editIV;


        public RetailerFinanceProfileVH(View itemView) {
            super(itemView);
        }
    }

}

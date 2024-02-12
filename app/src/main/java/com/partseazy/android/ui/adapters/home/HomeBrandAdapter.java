package com.partseazy.android.ui.adapters.home;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.finance.CreditFacilityFragment;
import com.partseazy.android.ui.fragments.home.HomeCategoryFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.home.category.DataItem;
import com.partseazy.android.utility.CommonUtility;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 27/12/16.
 */


public class HomeBrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context mContext;
    private List<DataItem> brandList;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;


    public HomeBrandAdapter(Context context, List<DataItem> brandlist) {
        this.brandList = brandlist;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(context).getImageLoader();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BrandViewHolder(mInflater.inflate(R.layout.row_brand_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BrandViewHolder brandViewHolder = (BrandViewHolder)holder;
        final DataItem brandData = brandList.get(position);

//        String formatedURL = brandData.src.replace("\\", "");
//        brandViewHolder.imageView.setImageUrl(formatedURL, imageLoader);

        String formatedURL = CommonUtility.getFormattedImageUrl(brandData.src, brandViewHolder.imageView, CommonUtility.IMGTYPE.HALFIMG);
        CommonUtility.setImageSRC(imageLoader, brandViewHolder.imageView, formatedURL);


        brandViewHolder.bannerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Moengage product clicked : Brand View

                PayloadBuilder builder = new PayloadBuilder();
                builder.putAttrInt(MoengageConstant.Events.PRODUCT_ID, brandData.refId)
                        .putAttrString(MoengageConstant.Events.CATEGORY,brandData.page)
                        .putAttrString(MoengageConstant.Events.PROMOTION_TYPE,"brand_view");


                if(brandData.page.equals(HomeCategoryFragment.BROWSE_PAGE)) {
                    String catalogueParams = new Gson().toJson(brandData.params);
                    builder.putAttrString("Catalogue_params",catalogueParams);
                    BaseFragment catalogueFragment = CatalogueFragment.newInstance(brandData.refId,catalogueParams);
                    BaseFragment.addToBackStack((BaseActivity) mContext, catalogueFragment, CatalogueFragment.getTagName());
                } else if(brandData.page.equals(HomeCategoryFragment.BROWSE_BRAND)) {
                    String catalogueParams = new Gson().toJson(brandData.params);
                    BaseFragment catalogueFragment = CatalogueFragment.newInstance(brandData.refId,catalogueParams);
                    BaseFragment.addToBackStack((BaseActivity) mContext, catalogueFragment, CatalogueFragment.getTagName());
                }
                else if(brandData.page.equals(HomeCategoryFragment.PRODUCT_PAGE))
                {
                    BaseFragment proFragment = ProductDetailFragment.newInstance(brandData.refId,"");
                    BaseFragment.addToBackStack((BaseActivity) mContext, proFragment, ProductDetailFragment.getTagName());
                }
                else if(brandData.page.equals(HomeCategoryFragment.FINANCE_PAGE))
                {
                    BaseFragment fragment = CreditFacilityFragment.newInstance();
                    BaseFragment.addToBackStack((BaseActivity) mContext, fragment, CreditFacilityFragment.getTagName());

                }
                MoEHelper.getInstance(mContext).trackEvent(MoengageConstant.Events.PRODUCT_CLICKED, builder);

            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    class BrandViewHolder extends BaseViewHolder {
        @BindView(R.id.brandLogo)
        public NetworkImageView imageView;
        @BindView(R.id.bannerLL)
        public CardView bannerLL;

        public BrandViewHolder(View view) {
            super(view);

        }
    }
}
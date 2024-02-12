package com.partseazy.android.ui.adapters.home;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.finance.CreditFacilityFragment;
import com.partseazy.android.ui.fragments.home.HomeCategoryFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.home.category.DataItem;
import com.partseazy.android.utility.CommonUtility;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by naveen on 27/12/16.
 */

public class HomeLargeBannerAdapter extends PagerAdapter {

    private Context mContext;
    private ImageLoader imageLoader;
    private List<DataItem> bannerList;

    public HomeLargeBannerAdapter(Context context, List<DataItem> bannerList) {
        this.mContext = context;
        imageLoader = ImageManager.getInstance(this.mContext).getImageLoader();
        this.bannerList = bannerList;

    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.row_home_item_large_banner, container, false);
        final NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.banner_image);
        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.bannerProgressBar);
        final CardView cardView = (CardView) view.findViewById(R.id.card_view);
        final DataItem bannerData = bannerList.get(position);

        pBar.setVisibility(View.GONE);
        // for (int i = 0; i < bannerList.size(); i++) {
        cardView.setVisibility(View.VISIBLE);
//        String formatedURL =bannerData.src.replace("\\", "");
//        imageView.setImageUrl(formatedURL, imageLoader);
//        imageView.setDrawingCacheEnabled(false);
//        imageView.setWillNotCacheDrawing(true);

        String formatedURL = CommonUtility.getFormattedImageUrl(bannerData.src, imageView, CommonUtility.IMGTYPE.HALFIMG);
        CommonUtility.setImageSRC(imageLoader, imageView, formatedURL);



        // }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Moengage product clicked : banner

                PayloadBuilder builder = new PayloadBuilder();
                builder.putAttrInt(MoengageConstant.Events.PRODUCT_ID, bannerData.refId)
                        .putAttrString(MoengageConstant.Events.CATEGORY,bannerData.page)
                .putAttrString(MoengageConstant.Events.PROMOTION_TYPE,"banner");

                CustomLogger.d("bannerData :: "+bannerData.page);
                if (bannerData.page.equals(HomeCategoryFragment.BROWSE_PAGE)) {
                    String catalogueParams = new Gson().toJson(bannerData.params);
                      /*builder.putAttrString(MoengageConstant.Events.SKU,  new Gson().toJson(bannerData.params))
                            .putAttrString(MoengageConstant.Events.CATEGORY, bannerData.label)
                            .putAttrString(MoengageConstant.Events.NAME, bannerData.)
                            .putAttrString(MoengageConstant.Events.BRAND, dealItem.name)
                            .putAttrString(MoengageConstant.Events.VARIANT, dealItem.name)
                            .putAttrString(MoengageConstant.Events.PRICE, dealItem.name)
                            .putAttrString(MoengageConstant.Events.CATEGORY, dealItem.name)
                            .putAttrString(MoengageConstant.Events.PRODUCT_LOCATION, dealItem.name)
                            .putAttrString(MoengageConstant.Events.INSTOCK, dealItem.name)
                            .putAttrString(MoengageConstant.Events.SECTION, dealItem.name)
                            .putAttrString(MoengageConstant.Events.POSITION, dealItem.name);*/
                      builder.putAttrString("Catalogue_params",catalogueParams);
                    CustomLogger.d("Before The params are "+bannerData.params);
                    BaseFragment cartFragment = CatalogueFragment.newInstance(bannerData.refId,catalogueParams);
                    BaseFragment.addToBackStack((BaseActivity) mContext, cartFragment, CatalogueFragment.getTagName());
                }else if(bannerData.page.equals(HomeCategoryFragment.PRODUCT_PAGE))
                {
                    BaseFragment proFragment = ProductDetailFragment.newInstance(bannerData.refId,null);
                    BaseFragment.addToBackStack((BaseActivity) mContext, proFragment, ProductDetailFragment.getTagName());
                }else if(bannerData.page.equals(HomeCategoryFragment.FINANCE_PAGE))
                {
                    BaseFragment fragment = CreditFacilityFragment.newInstance();
                    BaseFragment.addToBackStack((BaseActivity) mContext, fragment, CreditFacilityFragment.getTagName());

                }

                MoEHelper.getInstance(mContext).trackEvent(MoengageConstant.Events.PRODUCT_CLICKED, builder);

            }
        });
        container.addView(view);
        return (view);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }
}

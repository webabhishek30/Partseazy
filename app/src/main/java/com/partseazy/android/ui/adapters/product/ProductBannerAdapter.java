package com.partseazy.android.ui.adapters.product;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.fragments.product.ImageViewPagerFragment;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.utility.CommonUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 23/12/16.
 */

public class ProductBannerAdapter extends PagerAdapter {

    public static String DEAL_BUY_LAUNCH_FOR_YOUTUBE = "launchFromDealBuy";
    public static String DEAL_SELL_LAUNCH_FOR_YOUTUBE = "launchFromDealSell";
    public static String PRODUCT_LAUNCH = "launchFromLaunch";

    Context context;
    ImageLoader imageLoader;
    List<String> productBannerList = new ArrayList<>();
    private String launchScreenName;
    private Deal dealHolder;


    public ProductBannerAdapter(Context context, List<String> productBannerList, String launchScreenName) {
        this.context = context;
        this.productBannerList = productBannerList;
        this.launchScreenName = launchScreenName;
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();
    }

    public ProductBannerAdapter(Context context, List<String> productBannerList, String launchScreenName, Deal dealHolder) {
        this.context = context;
        this.productBannerList = productBannerList;
        this.launchScreenName = launchScreenName;
        this.dealHolder = dealHolder;
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();
    }


    @Override
    public int getCount() {
        return productBannerList == null ? 1 : (productBannerList.size() == 0 ? 1 : productBannerList.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.row_product_banner, container, false);
        final NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.banner_image);
        imageView.setDrawingCacheEnabled(false);
        imageView.setWillNotCacheDrawing(true);
        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.bannerProgressBar);
        final CardView cardView = (CardView) view.findViewById(R.id.card_view);
        if (productBannerList != null && productBannerList.size() > 0) {

            pBar.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);

            String formatedURL = CommonUtility.getFormattedImageUrl(productBannerList.get(position), imageView, CommonUtility.IMGTYPE.HALFIMG);
            CustomLogger.d("The formatted URL is  " + formatedURL);

            CommonUtility.setImageSRC(imageLoader, imageView, formatedURL);


        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (launchScreenName.equals(DEAL_BUY_LAUNCH_FOR_YOUTUBE) && dealHolder.info != null && !"".equals(dealHolder.info.youtubeId)) {
                    BaseFragment.addToBackStack((BaseActivity) context, ImageViewPagerFragment.newInstance(productBannerList, position, DEAL_BUY_LAUNCH_FOR_YOUTUBE, dealHolder.trade.id, dealHolder.trade.name), ImageViewPagerFragment.getTagName());

                }
                if (launchScreenName.equals(DEAL_SELL_LAUNCH_FOR_YOUTUBE) && dealHolder.info != null && !"".equals(dealHolder.info.youtubeId)) {
                    BaseFragment.removeTopAndAddToBackStack((BaseActivity) context, ImageViewPagerFragment.newInstance(productBannerList, position, DEAL_SELL_LAUNCH_FOR_YOUTUBE, dealHolder.trade.id, dealHolder.trade.name), ImageViewPagerFragment.getTagName());

                } else {
                    BaseFragment.addToBackStack((BaseActivity) context, ImageViewPagerFragment.newInstance(productBannerList, position), ImageViewPagerFragment.getTagName());
                }
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
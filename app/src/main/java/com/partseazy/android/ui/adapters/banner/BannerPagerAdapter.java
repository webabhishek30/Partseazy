package com.partseazy.android.ui.adapters.banner;

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
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.model.registration.banner.Banner;
import com.partseazy.android.utility.CommonUtility;

import java.util.ArrayList;
import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {

    Context context;
    BaseFragment fragment;
    List<Banner.Banner_> imageBanners = new ArrayList<>();
    ImageLoader imageLoader;

    public BannerPagerAdapter(BaseFragment context) {
        this.context = context.getContext();
        this.fragment = context;
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();
    }

    @Override
    public int getCount() {
        return imageBanners == null ? 1 : (imageBanners.size() == 0 ? 1 : imageBanners.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        CustomLogger.d("Inside instantiateItem ");
        View view =
                ((Activity) context).getLayoutInflater().inflate(R.layout.row_banner, container, false);
        final NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.banner_image);
        imageView.setDrawingCacheEnabled(false);
        imageView.setWillNotCacheDrawing(true);
        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.bannerProgressBar);
        final CardView cardView = (CardView) view.findViewById(R.id.card_view);
        if (imageBanners != null && imageBanners.size() > 0) {

            pBar.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);


//            String formatedURL = imageBanners.get(position).src.replace("\\", "");
//            CustomLogger.d("The formatted URL is  " + formatedURL);
//            imageView.setImageUrl(formatedURL, imageLoader);
//            imageView.setDefaultImageResId(0);
//            imageView.setErrorImageResId(0);

            String formatedURL = CommonUtility.getFormattedImageUrl(imageBanners.get(position).src, imageView, CommonUtility.IMGTYPE.HALFIMG);
            CommonUtility.setImageSRC(imageLoader, imageView, formatedURL);


        }
        container.addView(view);
        return (view);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    public void update(List<Banner.Banner_> items) {
        imageBanners.clear();
        if (items != null) {
            imageBanners.addAll(items);
        }
        notifyDataSetChanged();
    }

}
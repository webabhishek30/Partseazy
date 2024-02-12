package com.partseazy.android.ui.fragments.product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.GsonHelper;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.product.ProductBannerAdapter;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealDetailFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealDetailFragment;
import com.partseazy.android.ui.widget.AutoScrollPager;
import com.partseazy.android.ui.widget.CircleIndicator;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HackyViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by gaurav on 14/01/17.
 */

public class ImageViewPagerFragment extends BaseFragment {


    private static final String IMG_URL = "_img_urls";
    private static final String SELECTED_POSITION = "_selected_position";
    private static final String LAUNCH_SCREEN_NAME = "launchScreenName";
    private static final String DEAL_ID = "dealId";
    private static final String DEAL_NAME = "dealName";
    private List<String> imgUrlList;
    ImageView ivClose;
    private int currentPosition;
    private int dealId=0;
    private String launchScreenName,dealName;

    public static ImageViewPagerFragment newInstance(List<String> imgUrls, int selectedPosition) {
        Bundle bundle = new Bundle();
        bundle.putString(IMG_URL, new Gson().toJson(imgUrls));
        bundle.putInt(SELECTED_POSITION, selectedPosition);
        ImageViewPagerFragment fragment = new ImageViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ImageViewPagerFragment newInstance(List<String> imgUrls, int selectedPosition,String  launchScreen,int dealId,String dealName) {
        Bundle bundle = new Bundle();
        bundle.putString(IMG_URL, new Gson().toJson(imgUrls));
        bundle.putInt(SELECTED_POSITION, selectedPosition);
        bundle.putString(LAUNCH_SCREEN_NAME,launchScreen);
        bundle.putString(DEAL_NAME,dealName);
        bundle.putInt(DEAL_ID,dealId);
        ImageViewPagerFragment fragment = new ImageViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public static String getTagName() {
        return ImageViewPagerFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_image_view;
    }


    @Override
    protected String getFragmentTitle() {
        return "";
    }

    private static final String ISLOCKED_ARG = "isLocked";

    private AutoScrollPager mViewPager;

    private CircleIndicator circleIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String urlList = getArguments().getString(IMG_URL);
        imgUrlList = GsonHelper.getGson().fromJson(urlList, new TypeToken<List<String>>() {
        }.getType());
        currentPosition = getArguments().getInt(SELECTED_POSITION, 0);
        launchScreenName = getArguments().getString(LAUNCH_SCREEN_NAME,null);
        dealId = getArguments().getInt(DEAL_ID,dealId);
        dealName = getArguments().getString(DEAL_NAME,dealName);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        circleIndicator = (CircleIndicator) view.findViewById(R.id.indicator);
        mViewPager = (AutoScrollPager) view.findViewById(R.id.view_pager);

        ivClose = (ImageView) view.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   popBackStack(getFragmentManager());
                if(launchScreenName!=null && launchScreenName.equals(ProductBannerAdapter.DEAL_BUY_LAUNCH_FOR_YOUTUBE)){
                    addToBackStack((BaseActivity) getActivity(), BuyDealDetailFragment.newInstance(dealId,dealName), BuyDealDetailFragment.getTagName());
                }
                else if(launchScreenName!=null && launchScreenName.equals(ProductBannerAdapter.DEAL_SELL_LAUNCH_FOR_YOUTUBE)){
                    removeTopAndAddToBackStack((BaseActivity) getActivity(), SellDealDetailFragment.newInstance(dealId,dealName), SellDealDetailFragment.getTagName());
                }
                else{
                    popBackStack(getFragmentManager());
                }
            }
        });

        mViewPager.setAdapter(new SamplePagerAdapter());
        circleIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(currentPosition);

        if (imgUrlList.size() == 1) {
            circleIndicator.setVisibility(View.GONE);
        } else {
            circleIndicator.setVisibility(View.VISIBLE);
        }
    }


    class SamplePagerAdapter extends PagerAdapter {

        private final ImageLoader imageLoader;

        SamplePagerAdapter() {
            super();
            imageLoader = ImageManager.getInstance(context).getImageLoader();
        }

        @Override
        public int getCount() {
            return imgUrlList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            photoView.setImageUrl(CommonUtility.getFormattedImageUrl(imgUrlList.get(position), photoView, CommonUtility.IMGTYPE.FULLIMG), imageLoader);
//            photoView.setImageUrl(imgUrlList.get(position), imageLoader);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        if(launchScreenName!=null && launchScreenName.equals(ProductBannerAdapter.DEAL_BUY_LAUNCH_FOR_YOUTUBE)){
            addToBackStack((BaseActivity) getActivity(), BuyDealDetailFragment.newInstance(dealId,dealName), BuyDealDetailFragment.getTagName());
            return false;
        }
        if(launchScreenName!=null && launchScreenName.equals(ProductBannerAdapter.DEAL_SELL_LAUNCH_FOR_YOUTUBE)){
            removeTopAndAddToBackStack((BaseActivity) getActivity(), SellDealDetailFragment.newInstance(dealId,dealName), SellDealDetailFragment.getTagName());
            return false;
        }
        return true;
    }
}


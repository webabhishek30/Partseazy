package com.partseazy.android.ui.adapters.suppliers.shop;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.product.ImageViewPagerFragment;
import com.partseazy.android.ui.fragments.supplier.shop.SLIDEIMAGETYPE;
import com.partseazy.android.ui.fragments.supplier.shop.ShopDetailFragment;
import com.partseazy.android.ui.model.supplier.shop.Image;
import com.partseazy.android.ui.model.supplier.shop.ShopInfo;
import com.partseazy.android.utility.CommonUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopImageSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Image> imageList;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private SLIDEIMAGETYPE slideimagetype;
    private ShopInfo shopInfo;

    public static final int LIST_SLIDE_IMG_HEIGHT = 180;
    public static final int DETAIL_SLIDE_IMG_HEIGHT = 400;


    public ShopImageSliderAdapter(Context context, List<Image> imageList, SLIDEIMAGETYPE slideimagetype, ShopInfo shopInfo) {
        this.mContext = context;
        this.slideimagetype = slideimagetype;
        this.shopInfo = shopInfo;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(mContext).getImageLoader();

        if (imageList != null)
            this.imageList = imageList;
        else
            this.imageList = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (slideimagetype.ordinal() == SLIDEIMAGETYPE.RETAIL_LIST.ordinal()) {
            return new ImageViewHolder(mInflater.inflate(R.layout.row_shop_image_slider_item, parent, false));
        } else if (slideimagetype.ordinal() == SLIDEIMAGETYPE.RETAIL_DETAIL_PAGE.ordinal()) {
            return new ImageViewHolder(mInflater.inflate(R.layout.row_shop_detail_image_slider_item, parent, false));
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        final NetworkImageView networkImageView = imageViewHolder.productIconIV;
        final Image imageData = imageList.get(position);

        if (imageData.height == null){
            imageData.height = 180;
        }
        if (imageData.width == null){
            imageData.width = 90;
        }

        if (imageData != null) {
            if (slideimagetype.ordinal() == SLIDEIMAGETYPE.RETAIL_LIST.ordinal()) {

                double ratioToActual = ((double) imageData.width / imageData.height);
                int customWidth = (int) Math.round(ratioToActual * CommonUtility.convertDpToPixels(LIST_SLIDE_IMG_HEIGHT, mContext));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(customWidth, CommonUtility.convertDpToPixels(LIST_SLIDE_IMG_HEIGHT, mContext));
                if (position == 0) {

                    int pixel_left = CommonUtility.convertDpToPixels(15, mContext);
                    params.setMargins(pixel_left, 0, 0, 0);
                } else if (position == (imageList.size() - 1)) {

                    int pixel_Right = CommonUtility.convertDpToPixels(15, mContext);
                    params.setMargins(0, 0, pixel_Right, 0);
                }
                holder.itemView.setLayoutParams(params);

            } else if (slideimagetype.ordinal() == SLIDEIMAGETYPE.RETAIL_DETAIL_PAGE.ordinal()) {


                double ratioToActual = ((double) imageData.width / imageData.height);
                int customWidth = (int) Math.round(ratioToActual * CommonUtility.convertDpToPixels(DETAIL_SLIDE_IMG_HEIGHT, mContext));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(customWidth, CommonUtility.convertDpToPixels(DETAIL_SLIDE_IMG_HEIGHT, mContext));

                int pixel_Right = CommonUtility.convertDpToPixels(2, mContext);
                params.setMargins(0, 0, pixel_Right, 0);

                holder.itemView.setLayoutParams(params);
            }

            final int[] finalHeight = {0};
            final int[] finalWidth = {0};

            ViewTreeObserver vto = imageViewHolder.productIconIV.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imageViewHolder.productIconIV.getViewTreeObserver().removeOnPreDrawListener(this);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            finalHeight[0] = imageViewHolder.productIconIV.getMeasuredHeight();
                            finalWidth[0] = imageViewHolder.productIconIV.getMeasuredWidth();
                            CustomLogger.d("Height: " + finalHeight[0] + " Width: " + finalWidth[0]);
                            String formatedURL = null;
                            if (imageData != null) {
                                formatedURL = CommonUtility.getFormattedImageUrlForSlider(imageData.src, imageViewHolder.productIconIV, imageData.width, imageData.height, finalHeight[0]);
                                CustomLogger.d("formmatted URL ::" + formatedURL);
                            }

                            imageViewHolder.productIconIV.setImageDrawable(null);
                            CommonUtility.setImageSRC(imageLoader, imageViewHolder.productIconIV, formatedURL);


                        }
                    });
                    return true;
                }
            });


            handleAllClicks(imageViewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ImageViewHolder extends BaseViewHolder {

        @BindView(R.id.productIconIV)
        protected NetworkImageView productIconIV;


        public ImageViewHolder(View view) {
            super(view);

        }


    }


    public void updateImageList(List<Image> imageList) {

        this.imageList = imageList;
        notifyDataSetChanged();

    }

    private void handleAllClicks(ImageViewHolder productViewHolder, final int position) {
        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slideimagetype.ordinal() == SLIDEIMAGETYPE.RETAIL_DETAIL_PAGE.ordinal()) {
                    BaseFragment.addToBackStack((BaseActivity) mContext, ImageViewPagerFragment.newInstance(getImageList(), position), ImageViewPagerFragment.getTagName());

                } else if (slideimagetype.ordinal() == SLIDEIMAGETYPE.RETAIL_LIST.ordinal()) {
                    BaseFragment.addToBackStack((BaseActivity) mContext, ShopDetailFragment.newInstance(shopInfo.id, shopInfo.name), ShopDetailFragment.getTagName());

                }

            }
        });

    }


    private List<String> getImageList() {

        List<String> imageUrlList = new ArrayList<>();

        if (imageList != null && imageList.size() > 0) {
            for (Image image : imageList) {
                imageUrlList.add(image.src);
            }
        }
        return imageUrlList;

    }


}

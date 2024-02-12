package com.partseazy.android.ui.adapters.suppliers.retailer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.R;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.supplier.shop.Image;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 9/9/17.
 */

public class ShopPictureAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private List<Image> imageList;


    public ShopPictureAdapter(Context context, List<Image> imageList)
    {
        this.context = context;
        this.imageList = imageList;
        imageLoader = ImageManager.getInstance(context).getImageLoader();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ImageVH(LayoutInflater.from(context).inflate(R.layout.row_retailer_shop_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageVH imageVH = (ImageVH)holder;
        String shopPicURL = CommonUtility.getFormattedImageUrl(imageList.get(position).src, imageVH.shopIV, CommonUtility.IMGTYPE.HALFIMG);
        CommonUtility.setImageSRC(imageLoader, imageVH.shopIV, shopPicURL);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageVH extends BaseViewHolder{

        @BindView(R.id.shopIV)
        protected NetworkImageView shopIV;

        public ImageVH(View itemView) {
            super(itemView);
        }
    }
}

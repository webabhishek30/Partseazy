package com.partseazy.android.ui.adapters.favourite;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;

import com.partseazy.android.ui.fragments.favourites.FavProductFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.fav.FavData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;


import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by can on 19/12/16.
 */

public class FavProductAdapter extends RecyclerView.Adapter<FavProductAdapter.FavProductAdapterVH> {

    private BaseFragment mContext;
    private ArrayList<FavData> favDatas;
    private ImageLoader imageLoader;


    public FavProductAdapter(FavProductFragment context, ArrayList<FavData> cartItems) {
        mContext = context;
        favDatas = cartItems;
        imageLoader = ImageManager.getInstance(mContext.getContext()).getImageLoader();
    }

    @Override
    public FavProductAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FavProductAdapterVH(inflater.inflate(R.layout.row_fav_product, parent, false));
    }


    public void onBindViewHolder(FavProductAdapterVH holder, int position) {
        final FavData favData = favDatas.get(position);
        holder.pname_favTV.setText(favData.name);
        holder.moq_favTV.setText(mContext.getString(R.string.moq_price, favData.minQty));

        String productPrice = CommonUtility.getPriceRangeforPDP(favData.price, favData.priceHigh);

        if (productPrice != null && !productPrice.equals(0 + "")) {
            holder.price_favTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece, productPrice)));
            holder.price_favTV.setVisibility(View.VISIBLE);
        } else {
            holder.price_favTV.setVisibility(View.GONE);
        }

        if(favData.categoryId== AppConstants.REFURBISH_CAT_ID || favData.categoryId==AppConstants.REFURBISH_CAT_2_ID)
        {
            //holder.refurbishIcon.setVisibility(View.VISIBLE);
            holder.refurbishIcon.setVisibility(View.GONE);
        }else{
            holder.refurbishIcon.setVisibility(View.GONE);
        }
//        holder.price_favTV.setText(mContext.getString(R.string.price_per_piece_string, convertionPaiseToRupee(favData.price)+"",favData.type));
        if (favData.margin > 0) {
            holder.marginTV.setVisibility(View.VISIBLE);
            holder.marginTV.setText(mContext.getString(R.string.margin_fav, favData.margin));
        } else {
            holder.marginTV.setVisibility(View.GONE);
        }

        if (favData.imageUrl != null) {
//            String imageURL = CommonUtility.getFormattedImageUrl(favData.imageUrl);
//            holder.ivProductImage.setImageUrl(imageURL, imageLoader);

            String formatedURL = CommonUtility.getFormattedImageUrl(favData.imageUrl, holder.ivProductImage, CommonUtility.IMGTYPE.QUARTERIMG);
            CommonUtility.setImageSRC(imageLoader, holder.ivProductImage, formatedURL);


        }

        holder.remove_favCB.setSelected(true);

        holder.remove_favCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.REMOVE_FAV_ITEM_ID, favData.id);
                notifyDataSetChanged();
            }
        });

        holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment baseFragment = ProductDetailFragment.newInstance(favData.id, favData.name);
                BaseFragment.addToBackStack((BaseActivity) mContext.getActivity(), baseFragment, ProductDetailFragment.getTagName());
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment baseFragment = ProductDetailFragment.newInstance(favData.id, favData.name);
                BaseFragment.addToBackStack((BaseActivity) mContext.getActivity(), baseFragment, ProductDetailFragment.getTagName());
            }
        });

    }

    public void notifyAdapter(ArrayList<FavData> favDatas) {
        this.favDatas = favDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return favDatas.size();
    }

    class FavProductAdapterVH extends BaseViewHolder {

        @BindView(R.id.pname_favTV)
        TextView pname_favTV;
        @BindView(R.id.moq_favTV)
        TextView moq_favTV;
        @BindView(R.id.price_favTV)
        TextView price_favTV;
        @BindView(R.id.marginTV)
        TextView marginTV;
        @BindView(R.id.remove_favCB)
        ImageView remove_favCB;
        @BindView(R.id.ivProductImage)
        NetworkImageView ivProductImage;
        @BindView(R.id.refurbishIcon)
        protected  ImageView refurbishIcon;

        FavProductAdapterVH(View itemView) {
            super(itemView);
        }
    }
}

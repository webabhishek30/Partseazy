package com.partseazy.android.ui.adapters.home;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.home.HomeCategoryFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.home.category.DataItem;
import com.partseazy.android.utility.CommonUtility;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 27/1/17.
 */

public class HomeCardBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<DataItem> categoryList;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;


    public HomeCardBannerAdapter(Context context, List<DataItem> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(context).getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoxViewHolder(mInflater.inflate(R.layout.row_home_card_banner_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BoxViewHolder boxViewHolder = (BoxViewHolder) holder;
        final DataItem category = categoryList.get(position);


//        String formatedURL = category.src.replace("\\", "");
//        boxViewHolder.bannerIV.setImageUrl(formatedURL, imageLoader);

        String formatedURL = CommonUtility.getFormattedImageUrl(category.src, boxViewHolder.bannerIV, CommonUtility.IMGTYPE.HALFIMG);
        CommonUtility.setImageSRC(imageLoader, boxViewHolder.bannerIV, formatedURL);

        boxViewHolder.bannerNameTV.setText(category.label);

        boxViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.page.equals(HomeCategoryFragment.BROWSE_PAGE)) {
                    CustomLogger.d("Before The params are "+category.params);
                    String catalogueParams = new Gson().toJson(category.params);
                    BaseFragment cartFragment = CatalogueFragment.newInstance(category.refId, catalogueParams);
                    BaseFragment.addToBackStack((BaseActivity) context, cartFragment, CatalogueFragment.getTagName());
                } else if (category.page.equals(HomeCategoryFragment.PRODUCT_PAGE)) {
                    BaseFragment proFragment = ProductDetailFragment.newInstance(category.refId, null);
                    BaseFragment.addToBackStack((BaseActivity) context, proFragment, ProductDetailFragment.getTagName());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class BoxViewHolder extends BaseViewHolder {
        @BindView(R.id.bannerNameTV)
        protected TextView bannerNameTV;
        @BindView(R.id.bannerIV)
        protected NetworkImageView bannerIV;
        @BindView(R.id.cardView)
        protected CardView cardView;


        public BoxViewHolder(View view) {
            super(view);

        }
    }
}
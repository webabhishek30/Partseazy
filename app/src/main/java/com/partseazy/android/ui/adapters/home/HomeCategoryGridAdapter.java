package com.partseazy.android.ui.adapters.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.home.HomeCategoryFragment;
import com.partseazy.android.ui.model.home.category.DataItem;
import com.partseazy.android.utility.CommonUtility;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 23/1/17.
 */

public class HomeCategoryGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<DataItem> categoryList;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;


    public HomeCategoryGridAdapter(Context context, List<DataItem> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(context).getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoxViewHolder(mInflater.inflate(R.layout.row_home_l3_box_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BoxViewHolder boxViewHolder = (BoxViewHolder) holder;
        final DataItem category = categoryList.get(position);
        if(category.src!=null) {
//            String formatedURL = CommonUtility.getFormattedImageUrl(category.src);
//            boxViewHolder.categoryIconIV.setImageUrl(formatedURL, imageLoader);

            String formatedURL = CommonUtility.getFormattedImageUrl(category.src, boxViewHolder.categoryIconIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, boxViewHolder.categoryIconIV, formatedURL);


        }
        boxViewHolder.categoryNameTV.setText(category.label);
        boxViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.page.equals(HomeCategoryFragment.BROWSE_PAGE)) {
                    String catalogueParams = new Gson().toJson(category.params);
                    //Moengage Category data
                    PayloadBuilder builder = new PayloadBuilder();
                    builder.putAttrInt(MoengageConstant.Events.PRODUCT_ID, category.refId)
                            .putAttrString(MoengageConstant.Events.CATEGORY,category.page)
                            .putAttrString(MoengageConstant.Events.PARAMS, new Gson().toJson(category.params))
                            .putAttrString(MoengageConstant.Events.PROMOTION_TYPE,"CategoryGrid");
                    CustomLogger.d("Before The params are "+category.params);
                    BaseFragment catalogueFragment = CatalogueFragment.newInstance(category.refId, catalogueParams);
                    BaseFragment.addToBackStack((BaseActivity) context, catalogueFragment, CatalogueFragment.getTagName());
                    MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.PRODUCT_CLICKED, builder);

                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class BoxViewHolder extends BaseViewHolder {
        @BindView(R.id.categoryNameTV)
        protected TextView categoryNameTV;
        @BindView(R.id.categoryIconIV)
        protected NetworkImageView categoryIconIV;
        @BindView(R.id.card_view)
        protected RelativeLayout card_view;


        public BoxViewHolder(View view) {
            super(view);

        }
    }
}


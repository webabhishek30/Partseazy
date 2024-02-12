package com.partseazy.android.ui.adapters.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.productdialog.ProductDetailItem;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 3/1/17.
 */

public class ProductDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductDetailItem> detailsItem;
    private LayoutInflater mInflater;
    private boolean showHeading;


    public ProductDetailAdapter(Context context, List<ProductDetailItem> detailsItem, boolean showHeading) {
        this.detailsItem = detailsItem;
        mInflater = LayoutInflater.from(context);
        this.showHeading = showHeading;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductDetailsViewHolder(mInflater.inflate(R.layout.row_product_detail_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
   ProductDetailsViewHolder productDetailsViewHolder = (ProductDetailsViewHolder) holder;
        ProductDetailItem productDetailItem = detailsItem.get(position);
        if(productDetailItem.showParentName && showHeading)
        {
            productDetailsViewHolder.headingTV.setText(productDetailItem.parentName);
            productDetailsViewHolder.headingTV.setVisibility(View.VISIBLE);
        }else{
            productDetailsViewHolder.headingTV.setVisibility(View.GONE);
        }
        productDetailsViewHolder.detailTypeTV.setText(productDetailItem.itemName+":");
        if(productDetailItem.itemValue.equals("true"))
        {
            productDetailsViewHolder.detailValueTV.setText("Yes");
        }else if(productDetailItem.itemValue.equals("false"))
        {
            productDetailsViewHolder.detailValueTV.setText("No");
        }else{
            productDetailsViewHolder.detailValueTV.setText(productDetailItem.itemValue);
        }

    }

    @Override
    public int getItemCount() {
        return detailsItem.size();
    }

    class ProductDetailsViewHolder extends BaseViewHolder {
        @BindView(R.id.detailTypeTV)
        public  TextView detailTypeTV;
        @BindView(R.id.detailValueTV)
        public  TextView detailValueTV;
        @BindView(R.id.headingTV)
        public  TextView headingTV;


        public ProductDetailsViewHolder(View view) {
            super(view);

        }
    }
}

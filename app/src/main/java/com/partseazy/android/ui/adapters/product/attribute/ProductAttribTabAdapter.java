package com.partseazy.android.ui.adapters.product.attribute;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.productdetail.TabbedProductAttribute;
import com.partseazy.android.ui.widget.ColorTagBoxView;
import com.partseazy.android.ui.widget.TagBoxView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 30/12/16.
 */

public class ProductAttribTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TabbedProductAttribute> itemList;
    private LayoutInflater mInflater;
    private Context context;
    private String tagName;
    private ProductDetailFragment.OnAttributeItemSelectedListener onAttributeItemSelectedListener;
    private String tagNameOriginator;

    public ProductAttribTabAdapter(Context context, List<TabbedProductAttribute> itemList, String tagName, ProductDetailFragment.OnAttributeItemSelectedListener itemSelectedListener) {
        this.mInflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.context = context;
        this.tagName = tagName;
        this.onAttributeItemSelectedListener = itemSelectedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (tagName) {
            case AppConstants.ATTRIBUTE_COLOR_TAG:
                return new ColorAttributeTagViewHolder(mInflater.inflate(R.layout.color_tag_attribute_item, parent, false));
            case AppConstants.ATTRIBUTE_TAG:
                return new AttributeTagViewHolder(mInflater.inflate(R.layout.tag_attribute_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TabbedProductAttribute model = itemList.get(position);
        switch (tagName) {
            case AppConstants.ATTRIBUTE_COLOR_TAG:
                ColorAttributeTagViewHolder colorAttributeTagViewHolder = (ColorAttributeTagViewHolder) holder;
                handleColorTagClicks(colorAttributeTagViewHolder, model, position);
                return;

            case AppConstants.ATTRIBUTE_TAG:
                AttributeTagViewHolder attributeTagViewHolder = (AttributeTagViewHolder) holder;
                handleTagClicks(attributeTagViewHolder, model);
                return;

            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void handleColorTagClicks(final ColorAttributeTagViewHolder holder, final TabbedProductAttribute model, int position) {

        int colorCodeValue = Color.parseColor(getColorCode(model.value));
        CustomLogger.d("colorcOde ::" + colorCodeValue);
        final ColorTagBoxView tagBox = holder.tagBox;
        tagBox.setText(model.value);
        tagBox.setBoxIconColor(colorCodeValue);
        tagBox.setTag(model.productIDList);
        if (model.isSelected) {
           // if(colorCodeValue!=-1) {
                tagBox.setViewSelected(true, colorCodeValue);
            //}
        } else {
            tagBox.setViewSelected(false, colorCodeValue);
        }


        final int finalColorCodeValue = colorCodeValue;
        holder.tagBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> productIDList = (List<Integer>) holder.tagBox.getTag();
                onAttributeItemSelectedListener.onItemSelected(productIDList, tagName);
                if (holder.tagBox.getChecked()) {
                    holder.tagBox.setViewSelected(false, ContextCompat.getColor(context, R.color.white));
                    unselectAll();
                } else {
                   // if(finalColorCodeValue!=-1) {
                        holder.tagBox.setViewSelected(true, finalColorCodeValue);
                   // }
                    update(model);
                }
            }
        });

    }

    public void handleTagClicks(final AttributeTagViewHolder holder, final TabbedProductAttribute model) {
        final TagBoxView tagBox = holder.tagBox;
        tagBox.setText(model.value);
        tagBox.setTag(model.productIDList);
        if (model.isSelected) {
            tagBox.setViewSelected(true);
        } else {
            tagBox.setViewSelected(false);
        }
        holder.tagBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Integer> productIDList = (List<Integer>) holder.tagBox.getTag();
                onAttributeItemSelectedListener.onItemSelected(productIDList, tagName);

                if (holder.tagBox.getChecked()) {
                    holder.tagBox.setViewSelected(false);
                    unselectAll();
                } else {
                    holder.tagBox.setViewSelected(true);
                    update(model);
                }
            }
        });

    }

    class AttributeTagViewHolder extends BaseViewHolder {
        @BindView(R.id.tagBox)
        public TagBoxView tagBox;

        public AttributeTagViewHolder(View view) {
            super(view);
        }
    }

    class ColorAttributeTagViewHolder extends BaseViewHolder {
        @BindView(R.id.tagBox)
        public ColorTagBoxView tagBox;

        public ColorAttributeTagViewHolder(View view) {
            super(view);
        }
    }

    public void update(TabbedProductAttribute model) {


        for (int i = 0; i < itemList.size(); i++) {

            if (model.productIDList.size() == itemList.get(i).productIDList.size()
                    && itemList.get(i).productIDList.containsAll(model.productIDList)) {
                itemList.get(i).isSelected = true;

            } else {
                itemList.get(i).isSelected = false;
            }


        }
        notifyDataSetChanged();
    }

    public void unselectAll() {
        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).isSelected = false;
        }
        notifyDataSetChanged();
    }

    public String getColorCode(String colorAttributeName) {
        String colorCode = "#ffffff";
        colorAttributeName = colorAttributeName.toLowerCase();
        Map<String, String> colorMap = StaticMap.colorCodeMap;
        if (colorMap != null) {
            for (Map.Entry<String, String> colorEntry : colorMap.entrySet()) {
                if (colorAttributeName.equals(colorEntry.getKey())) {
                    return colorEntry.getValue();
                }
            }
        }
        return colorCode;
    }


}

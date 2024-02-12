package com.partseazy.android.ui.adapters.registration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.fragments.registration.RegisterL3SegmentFragment;
import com.partseazy.android.ui.widget.stickyHeaderRecylicView.SectioningAdapter;

import java.util.ArrayList;

/**
 * Created by naveen on 9/12/16.
 */

public class RegisterCategoryPriceRangeAdapter extends SectioningAdapter {


    BaseFragment context;
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();

    public RegisterCategoryPriceRangeAdapter(RegisterL3SegmentFragment context, ArrayList<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getNumberOfSections() {
        return categoryList.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return categoryList.get(sectionIndex).subCategoryModelList.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public SectioningAdapter.HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_category_list_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public SectioningAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_sub_category_price_range_dup, parent, false);
        return new ItemViewHolder(v);
    }

    @Override

    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
        String categoryName = categoryList.get(sectionIndex).categoryName;
        headerViewHolder.categoryHeaderTV.setText(categoryName);
    }

    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, final int itemIndex, int itemType) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        CategoryModel category = categoryList.get(sectionIndex);
        final CheckBox checkBox = itemViewHolder.checkBoxBtn;
        SubCategoryModel subCategoryModel = category.subCategoryModelList.get(itemIndex);
        String subCategoryName = subCategoryModel.name;
        itemViewHolder.subCatNameTv.setText(subCategoryName);
        itemViewHolder.checkboxLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    itemViewHolder.lowMarkIV.setVisibility(View.GONE);
                    itemViewHolder.mediumMarkIV.setVisibility(View.GONE);
                    itemViewHolder.highMarkIV.setVisibility(View.GONE);
                    itemViewHolder.luxuryMarkIV.setVisibility(View.GONE);


                } else {
                    checkBox.setChecked(true);
                    itemViewHolder.lowMarkIV.setVisibility(View.VISIBLE);
                    itemViewHolder.mediumMarkIV.setVisibility(View.VISIBLE);
                    itemViewHolder.highMarkIV.setVisibility(View.VISIBLE);
                    itemViewHolder.luxuryMarkIV.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {
        TextView subCatNameTv;
        RelativeLayout checkboxLL;
        CheckBox checkBoxBtn;
        ImageView lowMarkIV, mediumMarkIV, highMarkIV, luxuryMarkIV;

        public ItemViewHolder(View itemView) {
            super(itemView);
            subCatNameTv = (TextView) itemView.findViewById(R.id.subCatNameTv);
            checkboxLL = (RelativeLayout) itemView.findViewById(R.id.checkboxLL);
            checkBoxBtn = (CheckBox) itemView.findViewById(R.id.checkBoxBtn);
            lowMarkIV = (ImageView) itemView.findViewById(R.id.lowMarkIV);
            mediumMarkIV = (ImageView) itemView.findViewById(R.id.mediumMarkIV);
            highMarkIV = (ImageView) itemView.findViewById(R.id.highMarkIV);
            luxuryMarkIV = (ImageView) itemView.findViewById(R.id.luxuryMarkIV);
        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView categoryHeaderTV;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            categoryHeaderTV = (TextView) itemView.findViewById(R.id.categoryHeaderTV);
        }
    }

}


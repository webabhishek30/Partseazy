package com.partseazy.android.ui.adapters.registration;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.registration.RegisterL3SegmentFragment;
import com.partseazy.android.ui.model.categoryleveltwo.Category;

import com.partseazy.android.ui.model.categoryleveltwo.SubCategory;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 16/12/16.
 */

public class RegisterL3SegmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private final BaseFragment mContext;
    private List<Category> categoryList;
    private SubCategoryPriceSelectionAdapter adapter;
    private RegisterL3SegmentFragment registerL3SegmentFragment;


    public RegisterL3SegmentAdapter(RegisterL3SegmentFragment context, List<Category> categoryList) {
        this.mInflater = LayoutInflater.from(context.getContext());
        this.mContext = context;
        this.registerL3SegmentFragment =context;
        this.categoryList = categoryList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(mInflater.inflate(R.layout.row_category_price_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        Category category = categoryList.get(position);
        if (category.children != null && category.children.size() > 0) {
            categoryViewHolder.categoryNameTV.setText(category.name);
            populateAdapter(categoryViewHolder, category, position);
        } else {
            categoryViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.subCatRecylicView)
        public RecyclerView recyclerView;
        @BindView(R.id.categoryNameTV)
        public TextView categoryNameTV;
        @BindView(R.id.selectAllTV)
        public TextView selectAllTV;
        @BindView(R.id.selectAllIV)
        public ImageView selectAllIV;
        @BindView(R.id.selectAllLL)
        public LinearLayout selectAllLL;

        public CategoryViewHolder(View view) {
            super(view);
        }
    }

    public void populateAdapter(final CategoryViewHolder categoryHeadingViewHolder, final Category category, final int position) {

        final boolean isSelectedAll = category.isSelectedAll;
        if (isSelectedAll) {
            showColoredSelectAll(categoryHeadingViewHolder);
        } else {
            showUnSelectAll(categoryHeadingViewHolder);
        }

        RecyclerView recyclerView = categoryHeadingViewHolder.recyclerView;
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext.getContext());
        recyclerView.setLayoutManager(horizontalLayoutManager);
        List<SubCategory> subCategoryList = new ArrayList<>();
        if (category.children != null && category.children.size() > 0) {
            subCategoryList = category.children;
        }
        adapter = new SubCategoryPriceSelectionAdapter(mContext.getContext(),subCategoryList,registerL3SegmentFragment,category.id,position);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        categoryHeadingViewHolder.selectAllLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectedAll) {
                    category.isSelectedAll = false;
                } else {
                    category.isSelectedAll = true;
                }
                update(category, position);
            }
        });

    }


    public void update(Category category, int position) {

        if (category.isSelectedAll) {
            if (category.children != null && category.children.size() > 0) {
                for (int i = 0; i < category.children.size(); i++) {
                    category.children.get(i).isSelected = true;
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SELECT_ALL_SUBCAT_LIST, category.children, true);
            }

        } else {
            if (category.children != null && category.children.size() > 0) {
                for (int i = 0; i < category.children.size(); i++) {
                    category.children.get(i).isSelected = false;
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SELECT_ALL_SUBCAT_LIST, category.children, false);
            }
        }
        notifyItemChanged(position);
    }

    public void showColoredSelectAll(CategoryViewHolder categoryViewHolder) {
        categoryViewHolder.selectAllTV.setTextColor(mContext.getResources().getColor(R.color.green_checkout));
        categoryViewHolder.selectAllIV.setImageResource(R.drawable.circular_checked_icon);
    }

    public void showUnSelectAll(CategoryViewHolder categoryViewHolder) {
        categoryViewHolder.selectAllTV.setTextColor(mContext.getResources().getColor(R.color.text_dark));
        categoryViewHolder.selectAllIV.setImageResource(R.drawable.circular_unchecked_icon);
    }


}
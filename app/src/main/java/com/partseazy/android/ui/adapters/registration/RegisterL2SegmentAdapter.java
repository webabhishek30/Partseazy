package com.partseazy.android.ui.adapters.registration;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.registration.RegisterL2SegmentFragment;
import com.partseazy.android.ui.model.categorylevelone.Child;
import com.partseazy.android.ui.model.categorylevelone.Child_;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 8/12/16.
 */

public class RegisterL2SegmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private LayoutInflater mInflater;
    private final BaseFragment mContext;
    private List<Child> categoryList = null;
    RegisterSubCategoryAdapter registerSubCategoryAdapter;


    public RegisterL2SegmentAdapter(RegisterL2SegmentFragment context, List<Child> categoryList) {
        this.mInflater = LayoutInflater.from(context.getContext());
        this.mContext = context;
        this.categoryList = categoryList;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHeadingViewHolder(mInflater.inflate(R.layout.row_category_card, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CategoryHeadingViewHolder categoryHeadingViewHolder = (CategoryHeadingViewHolder) viewHolder;
        Child category = categoryList.get(position);
        if(category.children!=null && category.children.size()>0) {
            categoryHeadingViewHolder.categoryNameTV.setText(category.name);
            populateAdapter(categoryHeadingViewHolder, category);
        }else{
            categoryHeadingViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }



    class CategoryHeadingViewHolder extends BaseViewHolder {
        @BindView(R.id.subCatRecylicView)
        public RecyclerView recyclerView;
        @BindView(R.id.categoryNameTV)
        public TextView categoryNameTV;

        public CategoryHeadingViewHolder(View view) {
            super(view);
        }
    }

    public void populateAdapter(CategoryHeadingViewHolder categoryHeadingViewHolder, Child category) {
        RecyclerView recyclerView = categoryHeadingViewHolder.recyclerView;
        List<Child_> subCategoryList = category.children;
        if (subCategoryList != null && subCategoryList.size() > 0) {
            LinearLayoutManager horizontalLayoutManager  = new LinearLayoutManager(mContext.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            registerSubCategoryAdapter = new RegisterSubCategoryAdapter(mContext.getContext(), subCategoryList);
            recyclerView.setAdapter(registerSubCategoryAdapter);
            recyclerView.setNestedScrollingEnabled(false);

        }

    }

}

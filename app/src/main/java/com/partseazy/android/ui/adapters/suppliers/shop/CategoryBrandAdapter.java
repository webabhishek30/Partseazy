package com.partseazy.android.ui.adapters.suppliers.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class CategoryBrandAdapter extends RecyclerView.Adapter<CategoryBrandAdapter.CategoryViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> itemList;

    public CategoryBrandAdapter(Context context, List<String> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(mInflater.inflate(R.layout.row_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int position) {
        categoryViewHolder.categoryNameTV.setText(context.getString(R.string.bullet_category_name, itemList.get(position)));
    }


    @Override
    public int getItemCount() {
        if (itemList.size() > 5) {
            return 5;
        } else {
            return itemList.size();
        }
    }

    class CategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.categoryNameTV)
        protected TextView categoryNameTV;

        public CategoryViewHolder(View view) {
            super(view);

        }
    }
}

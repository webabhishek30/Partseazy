package com.partseazy.android.ui.adapters.deals.filters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.filters.FilterMaster;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 30/8/17.
 */

public class DealParentFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FilterMaster> itemList;

    private final OnParentItemSelectedListener listener;


    public interface OnParentItemSelectedListener {
        void onParentSelected(FilterMaster model);

    }



    public DealParentFilterAdapter(Context context, List<FilterMaster> itemList,OnParentItemSelectedListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ParentVH(LayoutInflater.from(context).inflate(R.layout.row_catalogue_filter_parent_item, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

       final ParentVH parentVH = (ParentVH) holder;
        final FilterMaster filterMaster = itemList.get(position);

        parentVH.categoryNameTV.setText(filterMaster.type);

        if (filterMaster.isSelected) {
            parentVH.categoryNameTV.setTextColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.black));
            parentVH.itemLYT.setBackgroundColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.white));
        } else {
            parentVH.categoryNameTV.setTextColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.white));
            parentVH.itemLYT.setBackgroundColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.colorPrimary));

        }
        parentVH.itemLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentVH.categoryNameTV.setTextColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.black));
                parentVH.itemLYT.setBackgroundColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.white));
                update(filterMaster);
                listener.onParentSelected(filterMaster);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ParentVH extends BaseViewHolder {
        @BindView(R.id.categoryNameTV)
        public TextView categoryNameTV;
        @BindView(R.id.checkMarkIV)
        public ImageView checkMarkIV;
        @BindView(R.id.itemLYT)
        public RelativeLayout itemLYT;

        public ParentVH(View view) {
            super(view);

        }
    }


    public void update(FilterMaster model) {
        for (int i = 0; i < itemList.size(); i++) {
            if (model.code == itemList.get(i).code) {
                itemList.get(i).isSelected = true;
            } else {
                itemList.get(i).isSelected = false;
            }
        }
        notifyDataSetChanged();
    }

}

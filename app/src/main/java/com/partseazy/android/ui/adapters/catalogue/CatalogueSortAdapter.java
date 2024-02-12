package com.partseazy.android.ui.adapters.catalogue;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.catalogue.SortAttibute;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 10/1/17.
 */


public class CatalogueSortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SortAttibute> itemList;
    private LayoutInflater mInflater;


    public CatalogueSortAdapter(Context context, List<SortAttibute> itemList) {
        this.itemList = itemList;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SortViewHolder(mInflater.inflate(R.layout.row_catalogue_sort_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SortViewHolder sortViewHolder = (SortViewHolder) holder;
        final TextView categoryNameTV = sortViewHolder.categoryNameTV;
        final ImageView checkMarkIV = sortViewHolder.checkMarkIV;
        final SortAttibute sortAttibute = itemList.get(position);
        if (sortAttibute.isSelected) {
            checkMarkIV.setVisibility(View.VISIBLE);
        } else {
            checkMarkIV.setVisibility(View.INVISIBLE);
        }
        categoryNameTV.setText(itemList.get(position).sortName);
        categoryNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortViewHolder.checkMarkIV.setVisibility(View.VISIBLE);
                update(sortAttibute);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_SORT_FILTER_ID,sortAttibute.sortCode);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class SortViewHolder extends BaseViewHolder {
        @BindView(R.id.categoryNameTV)
        public TextView categoryNameTV;
        @BindView(R.id.checkMarkIV)
        public ImageView checkMarkIV;

        public SortViewHolder(View view) {
            super(view);

        }
    }

    public void update(SortAttibute selectedStore) {
        for (int i = 0; i < itemList.size(); i++) {
            if (selectedStore.sortCode == itemList.get(i).sortCode) {
                itemList.get(i).isSelected = true;
            } else {
                itemList.get(i).isSelected = false;
            }
        }
        notifyDataSetChanged();
    }

}

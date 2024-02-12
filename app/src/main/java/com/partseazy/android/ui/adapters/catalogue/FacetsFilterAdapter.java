package com.partseazy.android.ui.adapters.catalogue;

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
import com.partseazy.android.ui.model.catalogue.Facet;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 10/1/17.
 */

public class FacetsFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Facet> itemList;
    private LayoutInflater mInflater;
    private final OnFacetItemSelectedListener listener;


    public interface OnFacetItemSelectedListener {
        void onFacetSelected(Facet facet);

    }


    public FacetsFilterAdapter(Context context, List<Facet> itemList, OnFacetItemSelectedListener listener) {
        this.mContext = context;
        this.itemList = itemList;
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParentViewHolder(mInflater.inflate(R.layout.row_catalogue_filter_parent_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
        final Facet facet = itemList.get(position);
        handleClicks(parentViewHolder, facet);

    }

    @Override
    public int getItemCount() {
        if (itemList != null)
            return itemList.size();
        else
            return 0;
    }

    public void handleClicks(final ParentViewHolder parentViewHolder, final Facet facet) {

        parentViewHolder.categoryNameTV.setText(facet.name);
        parentViewHolder.itemLYT.setTag(facet.buckets);
        if (facet.isSelected) {
            parentViewHolder.categoryNameTV.setTextColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.black));
            parentViewHolder.itemLYT.setBackgroundColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.white));
        } else {
            parentViewHolder.categoryNameTV.setTextColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.white));
            parentViewHolder.itemLYT.setBackgroundColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.colorPrimary));

        }
        parentViewHolder.itemLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentViewHolder.categoryNameTV.setTextColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.black));
                parentViewHolder.itemLYT.setBackgroundColor(PartsEazyApplication.getInstance().getResources().getColor(R.color.white));
                listener.onFacetSelected(facet);

                update(facet);
            }
        });

    }

    class ParentViewHolder extends BaseViewHolder {
        @BindView(R.id.categoryNameTV)
        public TextView categoryNameTV;
        @BindView(R.id.checkMarkIV)
        public ImageView checkMarkIV;
        @BindView(R.id.itemLYT)
        public RelativeLayout itemLYT;

        public ParentViewHolder(View view) {
            super(view);

        }
    }

    public void update(Facet facet) {
        for (int i = 0; i < itemList.size(); i++) {
            if (facet.name == itemList.get(i).name) {
                itemList.get(i).isSelected = true;
            } else {
                itemList.get(i).isSelected = false;
            }
        }
        notifyDataSetChanged();
    }


}

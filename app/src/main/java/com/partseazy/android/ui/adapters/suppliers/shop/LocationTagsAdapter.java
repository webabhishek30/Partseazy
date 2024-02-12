package com.partseazy.android.ui.adapters.suppliers.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.supplier.search.TagModel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 30/3/17.
 */


public class LocationTagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<TagModel> locationList;

    public LocationTagsAdapter(Context context, List<TagModel> locationList) {
        this.context = context;
        this.locationList = locationList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChipViewViewHolder(mInflater.inflate(R.layout.row_tag_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChipViewViewHolder chipViewViewHolder = (ChipViewViewHolder)holder;
        chipViewViewHolder.tagNameTV.setText(locationList.get(position).getValue());
        chipViewViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) context).onPopBackStack(true);
            }
        });
    }



    @Override
    public int getItemCount() {
        return locationList.size();
    }

    class ChipViewViewHolder extends BaseViewHolder {
        @BindView(R.id.tagNameTV)
        protected TextView tagNameTV;

        public ChipViewViewHolder(View view) {
            super(view);

        }
    }
}


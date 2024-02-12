package com.partseazy.android.ui.adapters.deals.buy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.filters.FilterValue;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 2/5/17.
 */

public class CategoryTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FilterValue> filterValueList;
    private LayoutInflater mInflater;

    public CategoryTabAdapter(Context context, List<FilterValue> filterValueList) {
        this.context = context;
        this.filterValueList = filterValueList;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagVH(mInflater.inflate(R.layout.row_tag_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TagVH tagVH = (TagVH) holder;

        final FilterValue item = filterValueList.get(position);

        tagVH.tagNameTV.setText(item.name);
        if (item.isSelected) {
            tagVH.tagRL.setBackgroundResource(R.drawable.rectangle_green_border);
            tagVH.checkIV.setVisibility(View.VISIBLE);
        } else {
            tagVH.tagRL.setBackgroundResource(R.drawable.rectangle_grey_border_white_solid);
            tagVH.checkIV.setVisibility(View.INVISIBLE);
        }
        tagVH.tagRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateList(position);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DEAL_CATID, filterValueList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return filterValueList.size();
    }

    public class TagVH extends BaseViewHolder {

        @BindView(R.id.tagRL)
        protected RelativeLayout tagRL;


        @BindView(R.id.tagNameTV)
        protected TextView tagNameTV;

        @BindView(R.id.checkIV)
        protected ImageView checkIV;

        public TagVH(View itemView) {
            super(itemView);
        }
    }

    private void updateList(int position) {
        if (filterValueList.get(position).isSelected) {
            filterValueList.get(position).isSelected = false;
        } else {
            filterValueList.get(position).isSelected = true;
        }

        notifyItemChanged(position);
    }

}

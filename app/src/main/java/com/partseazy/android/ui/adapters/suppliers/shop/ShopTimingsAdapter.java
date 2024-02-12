package com.partseazy.android.ui.adapters.suppliers.shop;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.supplier.WeekDay;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopTimingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WeekDay> detailsItem;
    private LayoutInflater mInflater;
    private boolean showHeading;
    private Context context;


    public ShopTimingsAdapter(Context context, List<WeekDay> detailsItem, boolean showHeading) {
        this.context = context;
        this.detailsItem = detailsItem;
        mInflater = LayoutInflater.from(context);
        this.showHeading = showHeading;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopTimingViewHolder(mInflater.inflate(R.layout.row_shop_time_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShopTimingViewHolder shopTimingViewHolder = (ShopTimingViewHolder) holder;
        WeekDay weekDay = detailsItem.get(position);

        shopTimingViewHolder.itemTV.setText(weekDay.value);
        if (weekDay.isSelected) {
            shopTimingViewHolder.itemTV.setPaintFlags(shopTimingViewHolder.itemTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            shopTimingViewHolder.itemTV.setTextColor(context.getResources().getColor(R.color.gray));

        }
    }

    @Override
    public int getItemCount() {
        return detailsItem.size();
    }

    class ShopTimingViewHolder extends BaseViewHolder {

        @BindView(R.id.itemTV)
        public TextView itemTV;


        public ShopTimingViewHolder(View view) {
            super(view);

        }
    }
}

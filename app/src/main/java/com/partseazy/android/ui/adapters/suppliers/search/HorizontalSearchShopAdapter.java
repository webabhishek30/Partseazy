package com.partseazy.android.ui.adapters.suppliers.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.supplier.search.ShopsSearchFragment;
import com.partseazy.android.ui.model.deal.CheckboxModel;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class HorizontalSearchShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CheckboxModel> itemList;
    private LayoutInflater mInflater;
    private final Context mContext;
    private ShopsSearchFragment.CHECKBOXTYPE checkboxtype;
    private boolean clickable;

    public HorizontalSearchShopAdapter(Context context, List<CheckboxModel> itemList, ShopsSearchFragment.CHECKBOXTYPE checkboxtype, boolean clickable) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.itemList = itemList;
        this.checkboxtype = checkboxtype;
        this.clickable = clickable;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoxViewHolder(mInflater.inflate(R.layout.row_horizontal_search_shop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final BoxViewHolder boxViewHolder = (BoxViewHolder) holder;
        CheckboxModel checkBoxModel = itemList.get(position);

        boxViewHolder.contentTV.setTag(checkBoxModel);
        boxViewHolder.contentTV.setText(checkBoxModel.value);
        boolean isSelected = checkBoxModel.isSelected;

        if (clickable) {
            if (isSelected) {
                //  boxViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.green_checkout));
                boxViewHolder.contentTV.setTextColor(mContext.getResources().getColor(R.color.white));
                boxViewHolder.contentTV.setBackgroundColor(mContext.getResources().getColor(R.color.light_pink));
                boxViewHolder.colorView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                boxViewHolder.contentTV.setTextColor(mContext.getResources().getColor(R.color.black));
                boxViewHolder.contentTV.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                boxViewHolder.colorView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }

            boxViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckboxModel checkBoxModel1 = (CheckboxModel) boxViewHolder.contentTV.getTag();
                    if (checkboxtype.ordinal() == ShopsSearchFragment.CHECKBOXTYPE.FOOTFALL.ordinal()) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_SEARCH_FOOTFALL, checkBoxModel1);
                    }
                    if (checkboxtype.ordinal() == ShopsSearchFragment.CHECKBOXTYPE.SHOP_SIZE.ordinal()) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_SEARCH_SIZE, checkBoxModel1);
                    }
                    if (checkboxtype.ordinal() == ShopsSearchFragment.CHECKBOXTYPE.TURNOVER.ordinal()) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_SEARCH_TURNOVER, checkBoxModel1);
                    }
                    update(position);
                }
            });
        } else {
            boxViewHolder.contentTV.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));
            boxViewHolder.colorView.setBackgroundColor(mContext.getResources().getColor(R.color.cb_dark_grey));
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class BoxViewHolder extends BaseViewHolder {
        @BindView(R.id.contentTV)
        protected TextView contentTV;
        @BindView(R.id.colorView)
        protected View colorView;

        public BoxViewHolder(View view) {
            super(view);
        }
    }

    public void update(int position) {

        CheckboxModel checkBoxModel = itemList.remove(position);
        if (checkBoxModel.isSelected) {
            checkBoxModel.isSelected = false;
        } else {
            checkBoxModel.isSelected = true;
        }
        itemList.add(position, checkBoxModel);
        notifyItemChanged(position);
    }

}

package com.partseazy.android.ui.adapters.registration;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.registration.RegisterBasicDetailsFragment;

import com.partseazy.android.ui.model.registration.StoreSize;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 15/12/16.
 */

public class StoreAreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<StoreSize> storeSizeList;
    private LayoutInflater mInflater;
    private final BaseFragment mContext;

    public StoreAreaAdapter(RegisterBasicDetailsFragment context, List<StoreSize> areaList) {
        this.mInflater = LayoutInflater.from(context.getContext());
        this.mContext = context;
        this.storeSizeList = areaList;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreAreaAdapter.AreaBoxViewHolder(mInflater.inflate(R.layout.row_store_area_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AreaBoxViewHolder boxHolder = (AreaBoxViewHolder) holder;
        final ImageView checkIcon = boxHolder.checkMarkIV;
        final StoreSize store = storeSizeList.get(position);
        if (store.isSelected) {
            checkIcon.setVisibility(View.VISIBLE);
        } else {
            checkIcon.setVisibility(View.GONE);
        }
        boxHolder.storeSizeTV.setText(store.sizeName);
        boxHolder.boxRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIcon.setVisibility(View.VISIBLE);
                update(store);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_STORE_SIZE, store);

            }
        });


    }

    @Override
    public int getItemCount() {
        return storeSizeList.size();
    }

    class AreaBoxViewHolder extends BaseViewHolder {
        @BindView(R.id.storeSizeTV)
        public TextView storeSizeTV;
        @BindView((R.id.boxRL))
        public RelativeLayout boxRL;
        @BindView(R.id.checkMarkIV)
        public ImageView checkMarkIV;

        public AreaBoxViewHolder(View view) {
            super(view);
        }
    }

    public void update(StoreSize selectedStore) {
        for (int i = 0; i < storeSizeList.size(); i++) {
            if (selectedStore.sizeType == storeSizeList.get(i).sizeType) {
                storeSizeList.get(i).isSelected = true;
            } else {
                storeSizeList.get(i).isSelected = false;
            }
        }
        notifyDataSetChanged();
    }


}

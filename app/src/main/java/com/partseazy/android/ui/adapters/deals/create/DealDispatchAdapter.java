package com.partseazy.android.ui.adapters.deals.create;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.CheckboxModel;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 3/5/17.
 */

public class DealDispatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<CheckboxModel> itemList;
    private LayoutInflater mInflater;


    public DealDispatchAdapter(Context context, List<CheckboxModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DispatchVH(mInflater.inflate(R.layout.row_dispatch_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DispatchVH dispatchVH = (DispatchVH) holder;
        final CheckboxModel item = itemList.get(position);
        dispatchVH.contentTV.setText(item.value);

        if (item.isSelected) {
            dispatchVH.boxRL.setBackgroundColor(context.getResources().getColor( R.color.green_success));
            dispatchVH.contentTV.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            dispatchVH.boxRL.setBackground(context.getResources().getDrawable( R.drawable.border));
            dispatchVH.contentTV.setTextColor(context.getResources().getColor(R.color.black));
        }
        dispatchVH.contentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateList(position);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.DEAL_DISPATCH,item.key);
               }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class DispatchVH extends BaseViewHolder {
        @BindView(R.id.contentTV)
        protected TextView contentTV;

        @BindView(R.id.boxRL)
        protected RelativeLayout boxRL;

        public DispatchVH(View itemView) {
            super(itemView);
        }
    }


    private void updateList(int position) {
      for (int i=0;i<itemList.size();i++)
      {
          itemList.get(i).isSelected = false;
      }
        itemList.get(position).isSelected = true;
       notifyDataSetChanged();
    }


}

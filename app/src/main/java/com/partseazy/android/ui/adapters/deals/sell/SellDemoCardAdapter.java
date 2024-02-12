package com.partseazy.android.ui.adapters.deals.sell;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.deal.deal_detail.DemoRequest;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 22/8/17.
 */

public class SellDemoCardAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private List<DemoRequest> demoRequestList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private Deal dealHolder;

    public SellDemoCardAdapter(Context context, List<DemoRequest> demoRequestList, Deal dealHolder) {
        this.context = context;
        this.demoRequestList = demoRequestList;
        this.mInflater = LayoutInflater.from(context);
        this.dealHolder = dealHolder;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DealVH(mInflater.inflate(R.layout.row_demo_deal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DealVH dealVH = (DealVH)holder;
        final DemoRequest demoRequest = demoRequestList.get(position);
        dealVH.contactNameTV.setText(demoRequest.name);

        dealVH.contentRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SHOW_MEETING_DAILOG,demoRequest);
            }
        });
       // dealVH.dateTV.setText(CommonUtility.getEMMMddyyyyHHmmDateFromDateCreated(demoRequest.createdAt));
        dealVH.dateTV.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return demoRequestList.size();
    }

    public class DealVH extends BaseViewHolder {

        @BindView(R.id.contentRL)
        protected RelativeLayout contentRL;

        @BindView(R.id.contactNameTV)
        protected TextView contactNameTV;
        @BindView(R.id.dateTV)
        protected TextView dateTV;

        public DealVH(View itemView) {
            super(itemView);
        }
    }
}

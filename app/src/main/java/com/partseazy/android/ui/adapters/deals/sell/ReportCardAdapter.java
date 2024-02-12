package com.partseazy.android.ui.adapters.deals.sell;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealDetailFragment;
import com.partseazy.android.ui.model.deal.selldeallist.ReportCardItem;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by shubhang on 23/11/17.
 */

public class ReportCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ReportCardItem> dealList;
    private LayoutInflater mInflater;

    public ReportCardAdapter(Context context, List<ReportCardItem> dealList) {
        this.context = context;
        this.dealList = dealList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReportCardAdapter.DealVH(mInflater.inflate(R.layout.row_report_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReportCardAdapter.DealVH dealVH = (ReportCardAdapter.DealVH) holder;

        final ReportCardItem reportCardItem = dealList.get(position);

        dealVH.dealNameTV.setText(reportCardItem.name);
        dealVH.dealViewTV.setText(reportCardItem.totalViews);
        dealVH.dealClickTV.setText(reportCardItem.totalOpens);
        dealVH.demoTV.setText(reportCardItem.totalDemo);
        dealVH.bookingsTV.setText(reportCardItem.totalBooking);
        dealVH.bookingAmountTV.setText(context.getString(R.string.booking_amount_1_s,
                String.valueOf(CommonUtility.convertionPaiseToRupee(Integer.parseInt(reportCardItem.totalBookingAmnt)))));
        dealVH.dealDateTV.setText(context.getString(R.string.date_1_s, CommonUtility.getLongDate(reportCardItem.startingAt)));

        dealVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellDealDetailFragment sellDealDetailFragment = SellDealDetailFragment.newInstance(Integer.parseInt(reportCardItem.id), reportCardItem.name);
                BaseFragment.addToBackStack((BaseActivity) context, sellDealDetailFragment, sellDealDetailFragment.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    public class DealVH extends BaseViewHolder {

        @BindView(R.id.dealNameTV)
        protected TextView dealNameTV;

        @BindView(R.id.dealDateTV)
        protected TextView dealDateTV;

        @BindView(R.id.dealViewTV)
        protected TextView dealViewTV;

        @BindView(R.id.dealClickTV)
        protected TextView dealClickTV;

        @BindView(R.id.demoTV)
        protected TextView demoTV;

        @BindView(R.id.bookingsTV)
        protected TextView bookingsTV;

        @BindView(R.id.bookingAmountTV)
        protected TextView bookingAmountTV;

        public DealVH(View itemView) {
            super(itemView);
        }
    }
}

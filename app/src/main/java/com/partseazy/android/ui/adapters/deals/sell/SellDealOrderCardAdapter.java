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
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.product.ProductBannerAdapter;
import com.partseazy.android.ui.fragments.deals.sell_deal.DealOrderDetailFragment;
import com.partseazy.android.ui.model.deal.deal_detail.Booking;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.utility.CommonUtility;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 16/5/17.
 */

public class SellDealOrderCardAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private List<Booking> bookingList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private Deal dealHolder;

    public SellDealOrderCardAdapter(Context context, List<Booking> bookingList, Deal dealHolder) {
        this.context = context;
        this.bookingList = bookingList;
        this.mInflater = LayoutInflater.from(context);
        this.dealHolder = dealHolder;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DealVH(mInflater.inflate(R.layout.row_sell_deal_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DealVH dealVH = (DealVH)holder;
        final Booking bookingItem = bookingList.get(position);
        dealVH.contactNameTV.setText(bookingItem.name);

        dealVH.qtyPerPriceTV.setText(context.getString(R.string.qty_per_price,bookingItem.qty, CommonUtility.convertionPaiseToRupee(bookingItem.price)));


        dealVH.contentRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isVideoAvailable = false;

                if(dealHolder.info!=null && dealHolder.info.youtubeId!=null && !dealHolder.info.youtubeId.equals("")) {
                    isVideoAvailable = true;
                }


                DealOrderDetailFragment dealOrderDetailFragment = DealOrderDetailFragment.newInstance(bookingItem.bkin, new Gson().toJson(dealHolder), ProductBannerAdapter.DEAL_BUY_LAUNCH_FOR_YOUTUBE);
                BaseFragment.addToBackStack((BaseActivity) context,dealOrderDetailFragment,dealOrderDetailFragment.getTag());

            }
        });

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class DealVH extends BaseViewHolder {

        @BindView(R.id.contentRL)
        protected RelativeLayout contentRL;

        @BindView(R.id.contactNameTV)
        protected TextView contactNameTV;
        @BindView(R.id.qtyPerPriceTV)
        protected TextView qtyPerPriceTV;

        public DealVH(View itemView) {
            super(itemView);
        }
    }
}

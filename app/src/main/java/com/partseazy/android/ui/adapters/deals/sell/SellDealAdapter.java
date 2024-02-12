package com.partseazy.android.ui.adapters.deals.sell;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.deals.SkuItemAdapter;
import com.partseazy.android.ui.fragments.deals.promote_deal.RetailerListFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealDetailFragment;
import com.partseazy.android.ui.model.deal.selldeallist.DealItem;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by naveen on 1/5/17.
 */

public class SellDealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DealItem> dealList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private SkuItemAdapter skuItemAdapter;


    public SellDealAdapter(Context context, List<DealItem> dealList) {
        this.context = context;
        this.dealList = dealList;
        this.mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DealVH(mInflater.inflate(R.layout.row_sell_deal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DealVH dealVH = (DealVH) holder;

        final DealItem sellDealItem = dealList.get(position);

        dealVH.dealNameTV.setText(sellDealItem.name);
        dealVH.dealerNameTV.setText(context.getString(R.string.sold_by,sellDealItem.supplier));

//        dealVH.soldTV.setText(sellDealItem.skus.get(0).sold+"");
        dealVH.viewTV.setText(sellDealItem.views+"");
        dealVH.clickTV.setText(sellDealItem.opens+"");

        if(sellDealItem.info!=null && sellDealItem.info.youtubeId!=null && !sellDealItem.info.youtubeId.equals(""))
        {
            dealVH.youtubeIcon.setVisibility(View.VISIBLE);
        }else{
            dealVH.youtubeIcon.setVisibility(View.GONE);
        }



        int claimPercentage = CommonUtility.getDealClaimedPercentage(sellDealItem.skus);
        dealVH.claimTV.setText(context.getString(R.string.claimed_percantage,claimPercentage+""));

        if(claimPercentage>5)
        {
            dealVH.simpleProgressBar.setProgress(claimPercentage);
        }else{
            dealVH.simpleProgressBar.setProgress(5);
        }


        String formatedURL= sellDealItem.image;
        formatedURL = CommonUtility.getFormattedImageUrl(formatedURL, dealVH.productIconIV, CommonUtility.IMGTYPE.QUARTERIMG);
        CommonUtility.setImageSRC(imageLoader, dealVH.productIconIV, formatedURL);


        if(sellDealItem.skus!=null && sellDealItem.skus.size()>1) {

            dealVH.skuRV.setVisibility(View.VISIBLE);
            dealVH.singleSkuLL.setVisibility(View.GONE);

            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            dealVH.skuRV.setLayoutManager(horizontalLayoutManager);
            skuItemAdapter = new SkuItemAdapter(context, sellDealItem.skus,sellDealItem,true);
            dealVH.skuRV.setAdapter(skuItemAdapter);
            dealVH.skuRV.setNestedScrollingEnabled(false);

            dealVH.claimView.setVisibility(View.GONE);
        }
        else {

            dealVH.skuRV.setVisibility(View.GONE);
            dealVH.singleSkuLL.setVisibility(View.VISIBLE);
            dealVH.claimView.setVisibility(View.VISIBLE);
            dealVH.skuPriceTV.setText(context.getString(R.string.rs,CommonUtility.convertionPaiseToRupee(sellDealItem.skus.get(0).price)));
            dealVH.skuMrpTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(sellDealItem.skus.get(0).mrp)));
            dealVH.skuMrpTV.setPaintFlags(dealVH.skuMrpTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            dealVH.skuDiscountTV.setText(context.getString(R.string.off_str, sellDealItem.skus.get(0).discount));

        }

        if(sellDealItem.promotion!=null && sellDealItem.promotion.equals("scheduled"))
        {
            dealVH.promoteDealBT.setEnabled(false);
            dealVH.promoteDealBT.setText(context.getString(R.string.promotion_schedule));
        }
        else{
            dealVH.promoteDealBT.setEnabled(true);
            dealVH.promoteDealBT.setText(context.getString(R.string.promote_deal));
        }

        dealVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellDealDetailFragment sellDealDetailFragment = SellDealDetailFragment.newInstance(sellDealItem.id,sellDealItem.name);
                BaseFragment.addToBackStack((BaseActivity)context,sellDealDetailFragment,sellDealDetailFragment.getTag());
            }
        });

        dealVH.promoteDealBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetailerListFragment retailerListFragment = RetailerListFragment.newInstance(sellDealItem.id);
                BaseFragment.addToBackStack((BaseActivity)context,retailerListFragment,retailerListFragment.getTag());

            }
        });

        CustomLogger.d("Time At position  :: "+CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(sellDealItem.endingAt) + " Date ::"+sellDealItem.endingAt);
        if(sellDealItem.active==1)
        {
            dealVH.timerTV.setVisibility(View.VISIBLE);
            dealVH.promoteDealLL.setVisibility(View.VISIBLE);
            dealVH.dealActiveTV.setVisibility(View.GONE);
        }else{
            dealVH.dealActiveTV.setVisibility(View.VISIBLE);
            dealVH.timerTV.setVisibility(View.GONE);
            dealVH.promoteDealLL.setVisibility(View.GONE);
        }
        startTimer(dealVH, CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(sellDealItem.endingAt));

    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    public class DealVH extends BaseViewHolder {

        @BindView(R.id.dealNameTV)
        protected TextView dealNameTV;

        @BindView(R.id.dealerNameTV)
        protected TextView dealerNameTV;

        @BindView(R.id.youtubeIcon)
        protected ImageView youtubeIcon;

//        @BindView(R.id.soldTV)
//        protected TextView soldTV;

        @BindView(R.id.viewTV)
        protected TextView viewTV;

        @BindView(R.id.clickTV)
        protected TextView clickTV;

        @BindView(R.id.claimTV)
        protected TextView claimTV;

        @BindView(R.id.simpleProgressBar)
        protected ProgressBar simpleProgressBar;


        @BindView(R.id.productIconIV)
        protected NetworkImageView productIconIV;

        @BindView(R.id.skuRV)
        protected RecyclerView skuRV;

        CountDownTimer countDownTimer;

        @BindView(R.id.timerTV)
        protected TextView timerTV;



        @BindView(R.id.singleSkuLL)
        protected LinearLayout singleSkuLL;


        @BindView(R.id.claimView)
        protected View claimView;


        @BindView(R.id.skuPriceTV)
        protected TextView skuPriceTV;

        @BindView(R.id.skuMrpTV)
        protected TextView skuMrpTV;

        @BindView(R.id.skuDiscountTV)
        protected TextView skuDiscountTV;

        @BindView(R.id.dealActiveTV)
        protected TextView dealActiveTV;


        @BindView(R.id.promoteDealLL)
        protected RelativeLayout promoteDealLL;

        @BindView(R.id.promoteDealBT)
        protected Button promoteDealBT;


        public DealVH(View itemView) {
            super(itemView);
        }
    }

    private void startTimer(final DealVH holder, long endTime) {

        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }

        holder.countDownTimer = new CountDownTimer(endTime, 1000) {
            // 500 means, onTick function will be called at every 500
            // milliseconds


            @Override
            public void onTick(long millisUntilFinished) {

                long day = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                long min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));


                //  long durationSeconds = leftTimeInMilliseconds / 1000;
                holder.timerTV.setText("");
                //  CustomLogger.d("Time :: " + durationSeconds % 60);
                if(day>0){
                    holder.timerTV.setText(context.getString(R.string.ends_in, String.format("%1d days", day)));

                }else {
                    holder.timerTV.setText(context.getString(R.string.ends_in, String.format("%02d:%02d:%02d",hour,min,second)));

                }

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                holder.timerTV.setText(context.getString(R.string.deal_is_over));
                holder.timerTV.setVisibility(View.VISIBLE);
            }

        }.start();

    }

}



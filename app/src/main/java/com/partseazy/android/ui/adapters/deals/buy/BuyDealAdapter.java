package com.partseazy.android.ui.adapters.deals.buy;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.deals.SkuItemAdapter;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealDetailFragment;
import com.partseazy.android.ui.model.deal.filters.FilterMaster;
import com.partseazy.android.ui.model.deal.selldeallist.DealItem;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;


/**
 * Created by naveen on 26/4/17.
 */

public class BuyDealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DealItem> dealList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private SkuItemAdapter skuItemAdapter;
    private CategoryTabAdapter categoryTabAdapter;
    private List<FilterMaster> filterMasterList;
    public boolean isLoading = false;

    public BuyDealAdapter(Context context, List<DealItem> dealList, List<FilterMaster> filterMasterList) {
        this.context = context;
        this.dealList = dealList;
        this.filterMasterList = filterMasterList;
        this.mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(context).getImageLoader();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_DEAL_BUY_INFO:
                return new DealInfoVH(mInflater.inflate(R.layout.row_buy_deal_info, parent, false));

            case VIEW_DEAL_BUY_CARD:
                return new DealVH(mInflater.inflate(R.layout.row_buy_deal_item, parent, false));

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DealInfoVH) {

            DealInfoVH dealInfoVH = (DealInfoVH) holder;

            if (filterMasterList != null && filterMasterList.size()>0) {
                for (int i = 0; i < filterMasterList.size(); i++) {
                    FilterMaster item = filterMasterList.get(i);
                    if (item.code.equals(DealConstants.CATEGORY_FILTER_PARAM_KEY)) {
                        if (item.values != null && item.values.size() > 0)
                        {
                                categoryTabAdapter = new CategoryTabAdapter(context, item.values);
                                dealInfoVH.categoryRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                dealInfoVH.categoryRV.setAdapter(categoryTabAdapter);
                        }
                    }
                }
            }

            if (dealList != null && dealList.size() > 1) {
                dealInfoVH.noDealRL.setVisibility(View.GONE);
            } else if (!isLoading) {
                dealInfoVH.noDealRL.setVisibility(View.VISIBLE);
                dealInfoVH.noDealIV.setImageResource(R.drawable.ic_no_deal);
                dealInfoVH.noDealTV.setTypeface(null, Typeface.BOLD);
                dealInfoVH.noDealTV.setText(context.getString(R.string.no_deals_found));
            } else {
                dealInfoVH.noDealRL.setVisibility(View.VISIBLE);
                dealInfoVH.noDealIV.setImageResource(R.drawable.loading_filled);
                dealInfoVH.noDealTV.setTypeface(null, Typeface.NORMAL);
                dealInfoVH.noDealTV.setText(context.getString(R.string.loading));
            }
        }
        if (holder instanceof DealVH) {
            DealVH dealVH = (DealVH) holder;
            final DealItem dealItem = dealList.get(position);


            dealVH.dealNameTV.setText(dealItem.name);
            dealVH.dealerNameTV.setText(context.getString(R.string.sold_by, dealItem.supplier));

            if (dealItem.info != null && dealItem.info.youtubeId != null && !dealItem.info.youtubeId.equals("")) {
                dealVH.youtubeIcon.setVisibility(View.VISIBLE);
            } else {
                dealVH.youtubeIcon.setVisibility(View.GONE);
            }

            int claimPercentage = CommonUtility.getDealClaimedPercentage(dealItem.skus);
            CustomLogger.d("claimPercentage :: " + claimPercentage);
            dealVH.claimTV.setText(context.getString(R.string.claimed_percantage, claimPercentage + ""));

            if (claimPercentage > 5) {
                dealVH.simpleProgressBar.setProgress(claimPercentage);
            } else {
                dealVH.simpleProgressBar.setProgress(5);
            }


            String formatedURL = dealItem.image;
            formatedURL = CommonUtility.getFormattedImageUrl(formatedURL, dealVH.productIconIV, CommonUtility.IMGTYPE.QUARTERIMG);
            CustomLogger.d("formattedUrl ::" + formatedURL);
            CommonUtility.setImageSRC(imageLoader, dealVH.productIconIV, formatedURL);


            if (dealItem.skus != null && dealItem.skus.size() > 1) {

                dealVH.skuRV.setVisibility(View.VISIBLE);
                dealVH.singleSkuLL.setVisibility(View.GONE);

                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//                if(dealItem.skus.size()==2)
//                    layoutManager = new GridLayoutManager(context,2);

                dealVH.skuRV.setLayoutManager(layoutManager);
                skuItemAdapter = new SkuItemAdapter(context, dealItem.skus, dealItem, false);
                dealVH.skuRV.setAdapter(skuItemAdapter);
                dealVH.skuRV.setNestedScrollingEnabled(false);

                dealVH.claimView.setVisibility(View.GONE);
            } else {
                dealVH.skuRV.setVisibility(View.GONE);
                dealVH.singleSkuLL.setVisibility(View.VISIBLE);
                dealVH.claimView.setVisibility(View.VISIBLE);
                dealVH.skuPriceTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(dealItem.skus.get(0).price)));
                dealVH.skuMrpTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(dealItem.skus.get(0).mrp)));
                dealVH.skuMrpTV.setPaintFlags(dealVH.skuMrpTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                dealVH.skuDiscountTV.setText(context.getString(R.string.off_str, dealItem.skus.get(0).discount));

            }

            if (dealItem.score != null && !dealItem.score.equals("")) {
                final double score = Double.parseDouble(dealItem.score);
                CustomLogger.d("Score Value :: " + score);
                if (score <= 0) {
                    dealVH.overlay.setVisibility(View.VISIBLE);
                } else {
                    dealVH.overlay.setVisibility(View.GONE);
                }


                dealVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (score > 0) {
                            BuyDealDetailFragment sellDealDetailFragment = BuyDealDetailFragment.newInstance(dealItem.id, dealItem.name, true);
                            BaseFragment.addToBackStack((BaseActivity) context, sellDealDetailFragment, sellDealDetailFragment.getTag());

                            //moengage dynamic deal event
                            PayloadBuilder builder = new PayloadBuilder();
                            builder.putAttrInt(MoengageConstant.Events.PROMOTION_ID, dealItem.id)
                                    .putAttrString(MoengageConstant.Events.LOCATION, dealItem.supplier)
                                    .putAttrString(MoengageConstant.Events.CREATIVE, dealItem.promotion)
                                    .putAttrString(MoengageConstant.Events.NAME, dealItem.name)
                                    .putAttrString(MoengageConstant.Events.POSITION, dealItem.name);
                            MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.DYNAMIC_DEALS_EVENT, builder);
                        }
                    }
                });
            }


            CustomLogger.d("Time At position  :: " + CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(dealItem.endingAt) + " Date ::" + dealItem.endingAt);

            startTimer(dealVH, CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(dealItem.endingAt));

            PartsEazyEventBus.getInstance().postEvent(EventConstant.VIEW_DEAL_METRICS, dealItem.id);
        }

    }

    public static void trackDynamicDealEvent(){

    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return HolderType.VIEW_DEAL_BUY_INFO.ordinal();

        return HolderType.VIEW_DEAL_BUY_CARD.ordinal();
    }

    public class DealInfoVH extends BaseViewHolder {

        @BindView(R.id.categoryRV)
        protected RecyclerView categoryRV;

        @BindView(R.id.dealSwitch)
        protected Switch dealSwitch;

        @BindView(R.id.noDealRL)
        protected RelativeLayout noDealRL;

        @BindView(R.id.noDealIV)
        protected ImageView noDealIV;

        @BindView(R.id.noDealTV)
        protected TextView noDealTV;

        @BindView(R.id.noDealCategoryTV)
        protected TextView noDealCategoryTV;

        public DealInfoVH(View view) {
            super(view);
        }
    }


    public class DealVH extends BaseViewHolder {
        @BindView(R.id.cardView)
        protected CardView cardView;

        @BindView(R.id.productIconIV)
        protected NetworkImageView productIconIV;

        @BindView(R.id.timerTV)
        protected TextView timerTV;

        @BindView(R.id.dealNameTV)
        protected TextView dealNameTV;

        @BindView(R.id.dealerNameTV)
        protected TextView dealerNameTV;

        @BindView(R.id.claimTV)
        protected TextView claimTV;

        @BindView(R.id.simpleProgressBar)
        protected ProgressBar simpleProgressBar;


        @BindView(R.id.singleSkuLL)
        protected LinearLayout singleSkuLL;


        @BindView(R.id.skuPriceTV)
        protected TextView skuPriceTV;

        @BindView(R.id.skuMrpTV)
        protected TextView skuMrpTV;

        @BindView(R.id.skuDiscountTV)
        protected TextView skuDiscountTV;


        @BindView(R.id.skuRV)
        protected RecyclerView skuRV;

        @BindView(R.id.claimView)
        protected View claimView;

        @BindView(R.id.overlay)
        protected LinearLayout overlay;

        @BindView(R.id.youtubeIcon)
        protected ImageView youtubeIcon;

        CountDownTimer countDownTimer;

        public DealVH(View itemView) {
            super(itemView);
        }
    }


    public void startTimer(final DealVH holder, long endTime) {

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
                if (day > 0) {
                    holder.timerTV.setText(context.getString(R.string.ends_in, String.format("%1d days", day)));

                } else {
                    holder.timerTV.setText(context.getString(R.string.ends_in, String.format("%02d:%02d:%02d", hour, min, second)));

                }

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                holder.timerTV.setText("Deal Ended !");
                holder.timerTV.setVisibility(View.VISIBLE);
            }

        }.start();

    }

    public void clearAdapter() {
        dealList.clear();
        this.notifyDataSetChanged();
    }


}



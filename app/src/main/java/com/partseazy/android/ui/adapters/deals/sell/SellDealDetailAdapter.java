package com.partseazy.android.ui.adapters.deals.sell;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.base.BaseViewPagerStateAdapter;
import com.partseazy.android.ui.adapters.deals.buy.BuySkuItemAdapter;
import com.partseazy.android.ui.fragments.deals.buy_deal.YoutubeFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealDetailFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.ViewsDetailFragment;
import com.partseazy.android.ui.model.deal.deal_detail.Booking;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.ui.fragments.deals.DealConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;


/**
 * Created by naveen on 15/5/17.
 */

public class SellDealDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private final Context context;
    private int mTotalItems = 6;
    private int LAKH = 100000;
    private int THOUSANDS = 1000;
    private int mDealId;

    public static final int MIN_ORDER_NUM = 3;
    private SellDealOrderCardAdapter dealOrderCardAdapter;
    private SellDemoCardAdapter demoCardAdapter;
    private Deal dealDetailHolder;
    private BuySkuItemAdapter buySkuItemAdapter;
    private BaseViewPagerStateAdapter pagerAdapter;
    public SellDealDetailFragment baseFragment;


    public SellDealDetailAdapter(SellDealDetailFragment basefragment, int mDealId, Deal dealDetailHolder) {
        this.baseFragment = basefragment;
        this.context = basefragment.getContext();
        this.mInflater = LayoutInflater.from(context);
        this.dealDetailHolder = dealDetailHolder;
        this.mDealId = mDealId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_DEAL_INFO:
                return new DealInfoViewHolder(mInflater.inflate(R.layout.row_deal_buy_item_info, parent, false));
            case VIEW_DEAL_YOUTUBE_CARD:
                return new DealYouTubeVH(mInflater.inflate(R.layout.row_deal_youtube_card_item, parent, false));
            case VIEW_DEAL_SKU:
                return new DealSkuVH(mInflater.inflate(R.layout.row_deal_sku_scroller, parent, false));
            case VIEW_DEAL_DESCRIPTION:
                return new DealDescVH(mInflater.inflate(R.layout.row_deal_description_item, parent, false));
            case VIEW_DEAL_ORDER_HISTORY:
                return new DealOrderViewHolder(mInflater.inflate(R.layout.row_deal_order_history_item, parent, false));
            case VIEW_DEAL_DEMO_LIST:
                return new DealDemoViewHolder(mInflater.inflate(R.layout.row_deal_demo_item, parent, false));


        }
        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DealInfoViewHolder) {
            final DealInfoViewHolder dealInfoViewHolder = (DealInfoViewHolder) holder;

            dealInfoViewHolder.bookDealBT.setVisibility(View.GONE);
            dealInfoViewHolder.bottomLineView.setVisibility(View.GONE);

            dealInfoViewHolder.dealNameTV.setText(dealDetailHolder.trade.name);
            dealInfoViewHolder.descTV.setText(dealDetailHolder.trade.desc);

            if (dealDetailHolder.opens > 0 && dealDetailHolder.nonGuestUsers) {
                dealInfoViewHolder.parentLayout.setPadding(CommonUtility.convertDpToPixels(10, context),
                        CommonUtility.convertDpToPixels(10, context), CommonUtility.convertDpToPixels(10, context),
                        CommonUtility.convertDpToPixels(5, context));

                dealInfoViewHolder.bottomLineView.setVisibility(View.VISIBLE);
                dealInfoViewHolder.viewsBT.setVisibility(View.VISIBLE);
                dealInfoViewHolder.viewsBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewsDetailFragment viewsDetailFragment = ViewsDetailFragment.newInstance(mDealId);
                        BaseFragment.addToBackStack((BaseActivity) context, viewsDetailFragment, viewsDetailFragment.getTag());
                    }
                });
            }
            dealInfoViewHolder.clicksQtyTV.setText(dealDetailHolder.opens + "");
            dealInfoViewHolder.viewQtyTV.setText(dealDetailHolder.views + "");

            final String minPriceStr = " ( " + context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(CommonUtility.getMinSKUDealPrice(dealDetailHolder.skus))) + " )";
            dealInfoViewHolder.shareIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionUtil.hasPermissions(context, PermissionUtil.CAMERA_PERMISSIONS)) {
                        CommonUtility.shareData(context, "Check out this amazing deal on B2C2 : " +
                                        dealDetailHolder.trade.name + minPriceStr,
                                DealConstants.SHARING_URL + dealDetailHolder.trade.id, dealDetailHolder.trade.images.get(0).src);
                    } else {
                        PermissionUtil.requestCameraPermission((BaseActivity) baseFragment.getActivity());
                    }
                }
            });
            startTimer(dealInfoViewHolder, CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(dealDetailHolder.trade.endingAt));
        }


        if (holder instanceof DealYouTubeVH) {
            DealYouTubeVH dealYTVH = (DealYouTubeVH) holder;
            if (dealDetailHolder.info != null && dealDetailHolder.info.youtubeId != null && !dealDetailHolder.info.youtubeId.equals("")) {
                setViewPager(dealYTVH.youtubeViewPager);
            } else {
                dealYTVH.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0));
            }

        }


        if (holder instanceof DealSkuVH) {
            DealSkuVH dealSkuVH = (DealSkuVH) holder;
            dealSkuVH.skuRV.setLayoutManager(new LinearLayoutManager(context));
            buySkuItemAdapter = new BuySkuItemAdapter(context, dealDetailHolder, false);
            dealSkuVH.skuRV.setAdapter(buySkuItemAdapter);
            dealSkuVH.skuRV.setNestedScrollingEnabled(false);
        }

        if (holder instanceof DealOrderViewHolder) {
            final DealOrderViewHolder orderViewHolder = (DealOrderViewHolder) holder;

            if (dealDetailHolder.bookings != null && dealDetailHolder.bookings.size() > 0) {

                orderViewHolder.subHeadingTV.setText(context.getString(R.string.booking_totalvalue, dealDetailHolder.bookings.size(), totalOrderSum()));

                final List<Booking> itemList = new ArrayList<>();
                if (MIN_ORDER_NUM < dealDetailHolder.bookings.size()) {
                    itemList.addAll(dealDetailHolder.bookings.subList(0, MIN_ORDER_NUM));
                    orderViewHolder.viewMoreLL.setVisibility(View.VISIBLE);
                } else {
                    itemList.addAll(dealDetailHolder.bookings);
                    orderViewHolder.viewMoreLL.setVisibility(View.GONE);

                }
                orderViewHolder.dealOrderRV.setLayoutManager(new LinearLayoutManager(context));
                dealOrderCardAdapter = new SellDealOrderCardAdapter(context, itemList, dealDetailHolder);
                orderViewHolder.dealOrderRV.setAdapter(dealOrderCardAdapter);
                orderViewHolder.dealOrderRV.setNestedScrollingEnabled(false);

                orderViewHolder.viewMoreLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (orderViewHolder.viewMoreLL.getVisibility() == View.VISIBLE) {
                            itemList.addAll(dealDetailHolder.bookings.subList(MIN_ORDER_NUM, dealDetailHolder.bookings.size()));
                            dealOrderCardAdapter.notifyDataSetChanged();
                            orderViewHolder.viewMoreLL.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                orderViewHolder.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0));
            }


        }


        if (holder instanceof DealDemoViewHolder) {

            final DealDemoViewHolder dealDemoViewHolder = (DealDemoViewHolder) holder;

            dealDemoViewHolder.viewMoreLL.setVisibility(View.GONE);
            if (dealDetailHolder.demoRequests != null && dealDetailHolder.demoRequests.size() > 0) {

                dealDemoViewHolder.subHeadingTV.setText(context.getString(R.string.request_number, dealDetailHolder.demoRequests.size()));

                dealDemoViewHolder.dealDemoRV.setLayoutManager(new LinearLayoutManager(context));
                demoCardAdapter = new SellDemoCardAdapter(context, dealDetailHolder.demoRequests, dealDetailHolder);
                dealDemoViewHolder.dealDemoRV.setAdapter(demoCardAdapter);
                dealDemoViewHolder.dealDemoRV.setNestedScrollingEnabled(false);

            } else {
                dealDemoViewHolder.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0));
            }

        }


        if (holder instanceof DealDescVH) {
            DealDescVH dealDescVH = (DealDescVH) holder;
            //String categoryStr = context.getString(R.string.deal_category,dealDetailHolder.l2Category)+"\n"+context.getString(R.string.deal_sub_category,dealDetailHolder.l3Category);
            dealDescVH.categoryTV.setText(context.getString(R.string.deal_category, dealDetailHolder.l2Category) + " => " + dealDetailHolder.l3Category);
            dealDescVH.descTV.setText(dealDetailHolder.trade.desc);
            dealDescVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }


    }


    @Override
    public int getItemCount() {
        return mTotalItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HolderType.VIEW_DEAL_YOUTUBE_CARD.ordinal();
        }
        if (position == 1) {
            return HolderType.VIEW_DEAL_INFO.ordinal();
        }
        if (position == 2) {
            return HolderType.VIEW_DEAL_SKU.ordinal();
        }
        if (position == 3) {
            return HolderType.VIEW_DEAL_ORDER_HISTORY.ordinal();
        }
        if (position == 4) {
            return HolderType.VIEW_DEAL_DEMO_LIST.ordinal();
        }
        if (position == 5) {
            return HolderType.VIEW_DEAL_DESCRIPTION.ordinal();
        }

        return 0;
    }


    private void setViewPager(final ViewPager viewPager) {
        pagerAdapter = new BaseViewPagerStateAdapter(baseFragment.getChildFragmentManager(), context);
        pagerAdapter.addFragment(YoutubeFragment.newInstance(dealDetailHolder.info.youtubeId), context.getString(R.string.billing_address));
        viewPager.setAdapter(pagerAdapter);
    }

    class DealInfoViewHolder extends BaseViewHolder {
        @BindView(R.id.dealNameTV)
        protected TextView dealNameTV;

        @BindView(R.id.timerTV)
        protected TextView timerTV;

        @BindView(R.id.descTV)
        protected TextView descTV;

        @BindView(R.id.shareIV)
        protected ImageView shareIV;

        @BindView(R.id.viewQtyTV)
        protected TextView viewQtyTV;

        @BindView(R.id.clicksQtyTV)
        protected TextView clicksQtyTV;

        @BindView(R.id.bookDealBT)
        protected Button bookDealBT;

        @BindView(R.id.bottomLineView)
        protected View bottomLineView;

        @BindView(R.id.viewsBT)
        protected Button viewsBT;

        @BindView(R.id.parentLayout)
        protected LinearLayout parentLayout;

        CountDownTimer countDownTimer;


        public DealInfoViewHolder(View view) {
            super(view);
        }

    }


    class DealDescVH extends BaseViewHolder {
        @BindView(R.id.descTV)
        protected TextView descTV;

        @BindView(R.id.categoryTV)
        protected TextView categoryTV;

        public DealDescVH(View view) {
            super(view);
        }
    }


    class DealOrderViewHolder extends BaseViewHolder {
        @BindView(R.id.dealOrderRV)
        protected RecyclerView dealOrderRV;

        @BindView(R.id.headerRL)
        protected RelativeLayout headerRL;

        @BindView(R.id.viewMoreLL)
        protected LinearLayout viewMoreLL;

        @BindView(R.id.subHeadingTV)
        protected TextView subHeadingTV;


        public DealOrderViewHolder(View view) {
            super(view);
        }

    }

    class DealDemoViewHolder extends BaseViewHolder {

        @BindView(R.id.dealDemoRV)
        protected RecyclerView dealDemoRV;

        @BindView(R.id.headerRL)
        protected RelativeLayout headerRL;

        @BindView(R.id.viewMoreLL)
        protected LinearLayout viewMoreLL;

        @BindView(R.id.subHeadingTV)
        protected TextView subHeadingTV;


        public DealDemoViewHolder(View view) {
            super(view);
        }

    }

    class DealSkuVH extends BaseViewHolder {

        @BindView(R.id.skuRV)
        protected RecyclerView skuRV;


        public DealSkuVH(View view) {
            super(view);
        }
    }

    class DealYouTubeVH extends BaseViewHolder {
        @BindView(R.id.youtubeViewPager)
        ViewPager youtubeViewPager;


        public DealYouTubeVH(View view) {
            super(view);
        }
    }


    private void startTimer(final DealInfoViewHolder holder, long endTime) {

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
                holder.timerTV.setText(context.getString(R.string.deal_is_over));
                holder.timerTV.setVisibility(View.VISIBLE);
            }

        }.start();

    }


    public List<Booking> addBookingList(int startIndex, int lastIndex, List<Booking> bookingList) {
        List<Booking> itemList = new ArrayList<>();

        for (int i = startIndex; i < lastIndex; i++) {
            Booking bookingItem = bookingList.get(i);
            itemList.add(bookingItem);
        }
        return itemList;
    }

    public String totalOrderSum() {
        double total = 0;
        for (int i = 0; i < dealDetailHolder.bookings.size(); i++) {
            Booking bookingItem = dealDetailHolder.bookings.get(i);
            total = total + bookingItem.price;
        }
        total = total / 100;

        if (total > LAKH) {
            total = total / LAKH;
            return total + " lacs";
        } else if (total > THOUSANDS) {
            total = total / THOUSANDS;
            return total + " thousands";
        } else {
            return total + "";
        }
    }
}

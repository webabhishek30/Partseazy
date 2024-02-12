package com.partseazy.android.ui.adapters.deals.buy;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.base.BaseViewPagerStateAdapter;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealDetailFragment;
import com.partseazy.android.ui.fragments.deals.buy_deal.YoutubeFragment;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.widget.CircularImageView;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.partseazy.android.ui.fragments.deals.DealConstants;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by naveen on 29/5/17.
 */

public class BuyDealDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private final Context context;
    public BuyDealDetailFragment baseFragment;
    private int mTotalItems = 6;

    private BuySkuItemAdapter buySkuItemAdapter;

    private BaseViewPagerStateAdapter pagerAdapter;

    private Deal dealDetailHolder;


    public BuyDealDetailAdapter(BuyDealDetailFragment baseFragment, Deal dealDetailHolder) {
        this.baseFragment = baseFragment;
        this.context = baseFragment.getContext();
        this.mInflater = LayoutInflater.from(context);
        this.dealDetailHolder = dealDetailHolder;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_DEAL_INFO:
                return new DealInfoViewHolder(mInflater.inflate(R.layout.row_deal_buy_item_info, parent, false));
            case VIEW_DEAL_YOUTUBE:
                return new DealYTVH(mInflater.inflate(R.layout.row_deal_youtube_item, parent, false));
            case VIEW_DEAL_YOUTUBE_CARD:
                return new DealYouTubeVH(mInflater.inflate(R.layout.row_deal_youtube_card_item, parent, false));
            case VIEW_DEAL_SKU:
                return new DealSkuVH(mInflater.inflate(R.layout.row_deal_sku_scroller, parent, false));
            case VIEW_DEAL_DESCRIPTION:
                return new DealDescVH(mInflater.inflate(R.layout.row_deal_description_item, parent, false));
            case VIEW_DEAL_SUPPLIER:
                return new DealSupplierViewHolder(mInflater.inflate(R.layout.row_deal_buy_supplier, parent, false));
            case VIEW_DEAL_TERMS_CONDITION:
                return new DealTermsConditionVH(mInflater.inflate(R.layout.row_deal_terms_condition_item_, parent, false));


        }
        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof DealInfoViewHolder) {
            DealInfoViewHolder dealInfoViewHolder = (DealInfoViewHolder) holder;
            dealInfoViewHolder.dealNameTV.setText(dealDetailHolder.trade.name);
            dealInfoViewHolder.descTV.setText(dealDetailHolder.trade.desc);

            dealInfoViewHolder.clicksQtyTV.setText(dealDetailHolder.opens + "");
            dealInfoViewHolder.viewQtyTV.setText(dealDetailHolder.views + "");

            final String minPriceStr = " ( " + context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(CommonUtility.getMinSKUDealPrice(dealDetailHolder.skus))) + " )";

            if (dealDetailHolder.trade.allowDemo != null && dealDetailHolder.trade.allowDemo == 1) {
                dealInfoViewHolder.bookDealBT.setVisibility(View.VISIBLE);
                dealInfoViewHolder.bottomLineView.setVisibility(View.VISIBLE);
            } else {
                dealInfoViewHolder.bookDealBT.setVisibility(View.GONE);
                dealInfoViewHolder.bottomLineView.setVisibility(View.GONE);
            }

            dealInfoViewHolder.shareIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionUtil.hasPermissions(context, PermissionUtil.CAMERA_PERMISSIONS)) {
                        CommonUtility.shareData(context, dealDetailHolder.trade.name + minPriceStr, DealConstants.SHARING_URL + dealDetailHolder.trade.id,
                                dealDetailHolder.trade.images.get(0).src);
                    } else {
                        PermissionUtil.requestCameraPermission((BaseActivity) baseFragment.getActivity());
                    }
                }
            });

            dealInfoViewHolder.bookDealBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.DEMO_MEETING_DEAL);
                }
            });

            startTimer(dealInfoViewHolder, CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(dealDetailHolder.trade.endingAt));


        }


        if (holder instanceof DealYTVH) {
            DealYTVH dealYTVH = (DealYTVH) holder;
            YouTubePlayerSupportFragment youtubePlayerFragment = new YouTubePlayerSupportFragment();
            youtubePlayerFragment.initialize(AppConstants.PARTSEAZY_APP_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                    if (!wasRestored) {
                        youTubePlayer.setFullscreen(false);
                        // youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        youTubePlayer.loadVideo("EGy39OMyHzw");
                        youTubePlayer.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });

            FragmentManager fragmentManager = baseFragment.getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(dealYTVH.youtubeFrameLayout.getId(), youtubePlayerFragment);
            fragmentTransaction.commit();

        }
        if (holder instanceof DealYouTubeVH) {
            DealYouTubeVH dealYTVH = (DealYouTubeVH) holder;
            if (dealDetailHolder.info != null && dealDetailHolder.info.youtubeId != null && !dealDetailHolder.info.youtubeId.equals("")) {
                setViewPager(dealYTVH.youtubeViewPager);
            } else {
                dealYTVH.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0));
            }

        }

        if (holder instanceof DealDescVH) {
            DealDescVH dealDescVH = (DealDescVH) holder;
            dealDescVH.descTV.setText(dealDetailHolder.trade.desc);

            dealDescVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }


        if (holder instanceof DealSupplierViewHolder) {
            DealSupplierViewHolder supplierViewHolder = (DealSupplierViewHolder) holder;

            supplierViewHolder.sellerCircularImage.setDefaultImageResId(R.drawable.supplier_icon);
            supplierViewHolder.supplierNameTV.setText(dealDetailHolder.user.name);
            supplierViewHolder.supplierRatingTV.setText(context.getString(R.string.supplier_rating, "" + dealDetailHolder.user.rating));
            supplierViewHolder.supplierCityTV.setText(dealDetailHolder.address.city + ", " + dealDetailHolder.address.state);


            supplierViewHolder.bookDealBT.setText(context.getString(R.string.demo_meeting_hindi));

            if (dealDetailHolder.trade.allowDemo != null && dealDetailHolder.trade.allowDemo == 1) {
                supplierViewHolder.bookDealBT.setVisibility(View.VISIBLE);
            } else {
                supplierViewHolder.bookDealBT.setVisibility(View.GONE);
            }


            supplierViewHolder.bookDealBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.DEMO_MEETING_DEAL);
                }
            });
        }


        if (holder instanceof DealTermsConditionVH) {
            DealTermsConditionVH termsConditionVH = (DealTermsConditionVH) holder;
            if (dealDetailHolder.trade.terms != null && !dealDetailHolder.trade.terms.equals("")) {
                termsConditionVH.termsConditionTV.setText(dealDetailHolder.trade.terms);
            } else {
                termsConditionVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }


        }

        if (holder instanceof DealSkuVH) {
            DealSkuVH dealSkuVH = (DealSkuVH) holder;
            dealSkuVH.skuRV.setLayoutManager(new LinearLayoutManager(context));
            if (buySkuItemAdapter == null)
                buySkuItemAdapter = new BuySkuItemAdapter(context, dealDetailHolder, true);
            dealSkuVH.skuRV.setAdapter(buySkuItemAdapter);
            dealSkuVH.skuRV.setNestedScrollingEnabled(false);

        }

    }


    private void setViewPager(final ViewPager viewPager) {
        pagerAdapter = new BaseViewPagerStateAdapter(baseFragment.getChildFragmentManager(), context);
        pagerAdapter.addFragment(YoutubeFragment.newInstance(dealDetailHolder.info.youtubeId), context.getString(R.string.billing_address));
        viewPager.setAdapter(pagerAdapter);
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
            return HolderType.VIEW_DEAL_DESCRIPTION.ordinal();
        }
        if (position == 4) {

            return HolderType.VIEW_DEAL_TERMS_CONDITION.ordinal();
        }
        if (position == 5) {
            return HolderType.VIEW_DEAL_SUPPLIER.ordinal();
        }
        return 0;
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

        CountDownTimer countDownTimer;

        public DealInfoViewHolder(View view) {
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

    class DealYTVH extends BaseViewHolder {

        FrameLayout youtubeFrameLayout;

        public DealYTVH(View view) {
            super(view);

        }
    }


    class DealDescVH extends BaseViewHolder {
        @BindView(R.id.descTV)
        protected TextView descTV;


        public DealDescVH(View view) {
            super(view);
        }
    }

    class DealSupplierViewHolder extends BaseViewHolder {
        @BindView(R.id.supplierNameTV)
        protected TextView supplierNameTV;

        @BindView(R.id.supplierRatingTV)
        protected TextView supplierRatingTV;

        @BindView(R.id.supplierCityTV)
        protected TextView supplierCityTV;

        @BindView(R.id.sellerCircularImage)
        protected CircularImageView sellerCircularImage;

        @BindView(R.id.bookDealBT)
        protected Button bookDealBT;

        public DealSupplierViewHolder(View view) {
            super(view);
        }
    }

    class DealTermsConditionVH extends BaseViewHolder {

        @BindView(R.id.termsConditionTV)
        protected TextView termsConditionTV;

        public DealTermsConditionVH(View view) {
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
                holder.timerTV.setText("Deal Ended !");
                holder.timerTV.setVisibility(View.VISIBLE);
            }

        }.start();

    }

}

package com.partseazy.android.ui.adapters.deals.promotions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.promotion.preview.PromoteRetailerDetail;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 28/8/17.
 */

public class RetailerPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PromoteRetailerDetail> retailerList;
    private LayoutInflater mInflater;

    public RetailerPreviewAdapter(Context context, List<PromoteRetailerDetail> retailerList) {
        this.context = context;
        this.retailerList = retailerList;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_DEAL_PROMOTE_PROGRESS:
                return new ProgressVH(mInflater.inflate(R.layout.row_promote_deal_progressbar, parent, false));

            case VIEW_DEAL_RETAILER_PREVIEW:
                return new RetailerVH(mInflater.inflate(R.layout.row_retailer_preview_item, parent, false));

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ProgressVH)
        {
            ProgressVH progressVH = (ProgressVH)holder;
            progressVH.secondCheckIV.setImageResource(R.drawable.check_green_circle);
            progressVH.firstLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));

            progressVH.headingTV.setText(context.getString(R.string.promotion_preview_heading));
            progressVH.headingTV.setVisibility(View.VISIBLE);
        }

        if (holder instanceof RetailerVH) {

            int newPostion = position-1;
            final RetailerVH retailerVH = (RetailerVH) holder;
            final PromoteRetailerDetail retailerDetail = retailerList.get(newPostion);
            if(retailerDetail.name!=null && !retailerDetail.name.equals("")) {
                retailerVH.nameTV.setText(CommonUtility.getFormattedName(retailerDetail.name));
            }
            retailerVH.addressTV.setText(retailerDetail.locality+", "+retailerDetail.district);
            retailerVH.cityTV.setText(retailerDetail.city+" - "+retailerDetail.pincode);

            if(newPostion==0)
            {
                retailerVH.topLineView.setVisibility(View.VISIBLE);
            }else{
                retailerVH.topLineView.setVisibility(View.GONE);
            }

            retailerVH.mapMarkerIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.SHOW_MAP_DAILOG,retailerDetail.latitude,retailerDetail.longitude,retailerDetail.name);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return retailerList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return HolderType.VIEW_DEAL_PROMOTE_PROGRESS.ordinal();

        return HolderType.VIEW_DEAL_RETAILER_PREVIEW.ordinal();
    }

    public class ProgressVH extends BaseViewHolder {

        @BindView(R.id.firstLineView)
        protected View firstLineView;

        @BindView(R.id.secondCheckIV)
        protected ImageView secondCheckIV;

        @BindView(R.id.headingTV)
        protected TextView headingTV;


        public ProgressVH(View view) {
            super(view);
        }
    }

    public class RetailerVH extends BaseViewHolder {

        @BindView(R.id.nameTV)
        protected TextView nameTV;

        @BindView(R.id.addressTV)
        protected TextView addressTV;

        @BindView(R.id.cityTV)
        protected TextView cityTV;

        @BindView(R.id.mapMarkerIV)
        protected ImageView mapMarkerIV;

        @BindView(R.id.topLineView)
        protected View topLineView;


        public RetailerVH(View view) {
            super(view);
        }
    }
}

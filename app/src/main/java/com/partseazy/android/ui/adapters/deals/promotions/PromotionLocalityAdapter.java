package com.partseazy.android.ui.adapters.deals.promotions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.promotion.locations.Locality;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 28/8/17.
 */

public class PromotionLocalityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<Locality> localityList;
    private int districtCount;

    public PromotionLocalityAdapter(Context context, List<Locality> localityList, int districtCount) {
        this.context = context;
        this.localityList = localityList;
        this.districtCount = districtCount;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocalityVH(LayoutInflater.from(context).inflate(R.layout.row_promotion_locality_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocalityVH localityVH = (LocalityVH) holder;

        if (position == (localityList.size())) {
            localityVH.subHeadingTV.setVisibility(View.GONE);
            try {
                int otherRetailer = districtCount - getTotalLocalityShop();
                localityVH.areaNameTV.setText("and "+otherRetailer+" others");
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }else{
            Locality locality = localityList.get(position);
            localityVH.areaNameTV.setText(locality.name);
            localityVH.subHeadingTV.setText("( "+locality.count+" )");
            localityVH.subHeadingTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return localityList.size()+1;
    }

    public class LocalityVH extends BaseViewHolder {

        @BindView(R.id.areaNameTV)
        protected TextView areaNameTV;

        @BindView(R.id.subHeadingTV)
        protected TextView subHeadingTV;


        public LocalityVH(View view) {
            super(view);
        }
    }

    private int  getTotalLocalityShop()
    {
        int count=0;
        for (int i=0;i<localityList.size();i++)
        {
            count=count+localityList.get(i).count;
        }
        return count;
    }
}

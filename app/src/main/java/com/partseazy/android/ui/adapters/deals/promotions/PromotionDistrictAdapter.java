package com.partseazy.android.ui.adapters.deals.promotions;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.promotion.locations.District;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 28/8/17.
 */

public class PromotionDistrictAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<District> districtList;

    public PromotionDistrictAdapter(Context context, List<District> districtList) {
        this.context = context;
        this.districtList = districtList;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new AreaVH(LayoutInflater.from(context).inflate(R.layout.row_promotion_district_item, parent, false));


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final AreaVH areaVH = (AreaVH) holder;

        final District district = districtList.get(position);

        areaVH.mainAreaCB.setText(district.name);
        areaVH.subHeadingTV.setText(district.count + " Shops");
        areaVH.mainAreaCB.setChecked(district.isSelected);
        areaVH.areaRV.setVisibility(View.GONE);
        areaVH.areaRV.setLayoutManager(new LinearLayoutManager(context));
        PromotionLocalityAdapter promotionLocalityAdapter = new PromotionLocalityAdapter(context, district.localityList, district.count);
        areaVH.areaRV.setAdapter(promotionLocalityAdapter);
        areaVH.areaRV.setNestedScrollingEnabled(false);

        areaVH.mainAreaCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areaVH.mainAreaCB.isChecked()) {
                    areaVH.areaRV.setVisibility(View.VISIBLE);
                    areaVH.arrowIV.setRotation(0);
                    districtList.get(position).isSelected = true;
                } else {
                    areaVH.areaRV.setVisibility(View.GONE);
                    areaVH.arrowIV.setRotation(270);
                    districtList.get(position).isSelected = false;
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECTED_CLUSTER_ID, district);

            }
        });

        areaVH.contentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                districtList.get(position).isSelected = !districtList.get(position).isSelected;
                areaVH.mainAreaCB.setChecked( districtList.get(position).isSelected);
                if (districtList.get(position).isSelected) {
                    areaVH.areaRV.setVisibility(View.VISIBLE);
                    areaVH.arrowIV.setRotation(0);
                } else {
                    areaVH.areaRV.setVisibility(View.GONE);
                    areaVH.arrowIV.setRotation(270);
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECTED_CLUSTER_ID, district);
            }
        });

    }


    @Override
    public int getItemCount() {
        return districtList.size();
    }


    public class AreaVH extends BaseViewHolder {


        @BindView(R.id.areaRV)
        RecyclerView areaRV;

        @BindView(R.id.mainAreaCB)
        protected CheckBox mainAreaCB;

        @BindView(R.id.arrowIV)
        protected ImageView arrowIV;

        @BindView(R.id.subHeadingTV)
        protected TextView subHeadingTV;

        @BindView(R.id.contentLL)
        protected LinearLayout contentLL;

        public AreaVH(View view) {
            super(view);
        }
    }
}

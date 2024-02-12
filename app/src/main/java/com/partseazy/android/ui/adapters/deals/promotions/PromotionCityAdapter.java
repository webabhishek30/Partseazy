package com.partseazy.android.ui.adapters.deals.promotions;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.promotion.locations.City;
import com.partseazy.android.utility.HolderType;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 31/8/17.
 */

public class PromotionCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<City> cityList;
    private LayoutInflater mInflater;

    public PromotionCityAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_DEAL_PROMOTE_PROGRESS:
                return new ProgressVH(mInflater.inflate(R.layout.row_promote_deal_progressbar, parent, false));

            case VIEW_DEAL_PROMOTE_CITY:
                return new CityVH(mInflater.inflate(R.layout.row_promotion_city_item, parent, false));

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ProgressVH)
        {
            ProgressVH progressVH = (ProgressVH)holder;
            progressVH.headingTV.setVisibility(View.VISIBLE);
        }

        if (holder instanceof CityVH) {

            final CityVH cityVH = (CityVH) holder;
            City city = cityList.get(position - 1);
            cityVH.cityTV.setText(city.name);
            cityVH.subHeadingTV.setText(city.count +" Shops");
            cityVH.localityRV.setLayoutManager(new LinearLayoutManager(context));
            PromotionDistrictAdapter areaCheckBoxAdapter = new PromotionDistrictAdapter(context, city.districtList);
            cityVH.localityRV.setAdapter(areaCheckBoxAdapter);
            cityVH.localityRV.setNestedScrollingEnabled(false);


        }

    }

    @Override
    public int getItemCount() {
        return cityList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return HolderType.VIEW_DEAL_PROMOTE_PROGRESS.ordinal();

        return HolderType.VIEW_DEAL_PROMOTE_CITY.ordinal();
    }

    public class ProgressVH extends BaseViewHolder {

        @BindView(R.id.headingTV)
        protected TextView headingTV;

        public ProgressVH(View view) {
            super(view);
        }
    }

    public class CityVH extends BaseViewHolder {


        @BindView(R.id.localityRV)
        RecyclerView localityRV;

        @BindView(R.id.cityTV)
        protected TextView cityTV;

        @BindView(R.id.subHeadingTV)
        protected TextView subHeadingTV;


        public CityVH(View view) {
            super(view);
        }
    }


}

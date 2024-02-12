package com.partseazy.android.ui.adapters.deals.buy;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.CheckboxModel;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 17/8/17.
 */

public class DealRightDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CheckboxModel> categoryList;
    private LayoutInflater mInflater;
    protected DrawerLayout mDrawer;
    protected RecyclerView rightDrawerRV;
    private  int isPublicDeal;
    protected DealCategoryAdapter dealCategoryAdapter;


    public DealRightDrawerAdapter(Context context, List<CheckboxModel> categoryList, DrawerLayout drawerLayout, RecyclerView recyclerView) {
        this.context = context;
        this.categoryList = categoryList;
        this.mInflater = LayoutInflater.from(context);
        this.mDrawer = drawerLayout;
        this.rightDrawerRV = recyclerView;

    }

    public DealRightDrawerAdapter(Context context, List<CheckboxModel> categoryList,int isPublicDeal) {
        this.context = context;
        this.categoryList = categoryList;
        this.mInflater = LayoutInflater.from(context);
        this.isPublicDeal = isPublicDeal;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_DEAL_FILTER_SWITCH_CARD:
                return new DealSwitchVH(mInflater.inflate(R.layout.row_deal_switch_item, parent, false));

            case VIEW_DEAL_FILTER_CATEGORY_CARD:
                return new DealCategoryVH(mInflater.inflate(R.layout.row_deal_category_item, parent, false));

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DealSwitchVH) {
            DealSwitchVH dealSwitchVH = (DealSwitchVH) holder;

            if(isPublicDeal==1) {
                dealSwitchVH.dealSwitch.setChecked(false);
            }else{
                dealSwitchVH.dealSwitch.setChecked(true);
            }

            dealSwitchVH.dealSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        isPublicDeal=0;
                       // PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DEAL_TYPE_ID, 0);
                    } else {
                        isPublicDeal=1;
                        //PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DEAL_TYPE_ID, 1);
                    }

                }
            });

            dealSwitchVH.crossIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // closeDrawer();
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CLOSE_DEAL_FILTER);
                }
            });
        }

        if (holder instanceof DealCategoryVH) {
            DealCategoryVH dealCategoryVH = (DealCategoryVH) holder;
            dealCategoryVH.categoryRV.setLayoutManager(new LinearLayoutManager(context));
            dealCategoryAdapter = new DealCategoryAdapter(context, categoryList);
            dealCategoryVH.categoryRV.setAdapter(dealCategoryAdapter);


            dealCategoryVH.applyFilterBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  closeDrawer();
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.APPLY_DEAL_FILTER,isPublicDeal,dealCategoryAdapter.getSelectedCategoryIdList());

                }
            });

            dealCategoryVH.resetFilterBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PartsEazyEventBus.getInstance().postEvent(EventConstant.RESET_DEAL_FILTER);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return HolderType.VIEW_DEAL_FILTER_SWITCH_CARD.ordinal();

        return HolderType.VIEW_DEAL_FILTER_CATEGORY_CARD.ordinal();
    }


    public class DealSwitchVH extends BaseViewHolder {

        @BindView(R.id.dealSwitch)
        protected Switch dealSwitch;

        @BindView(R.id.crossIV)
        protected ImageView crossIV;

        DealSwitchVH(View itemView) {
            super(itemView);
        }
    }

    public class DealCategoryVH extends BaseViewHolder {

        @BindView(R.id.categoryRV)
        protected RecyclerView categoryRV;

        @BindView(R.id.applyFilterBTN)
        protected Button applyFilterBTN;

        @BindView(R.id.resetFilterBTN)
                protected Button resetFilterBTN;


        DealCategoryVH(View itemView) {
            super(itemView);
        }
    }

    public void closeDrawer() {
        mDrawer.closeDrawers();
    }
}

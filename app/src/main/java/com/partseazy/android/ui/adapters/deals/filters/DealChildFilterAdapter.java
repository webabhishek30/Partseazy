package com.partseazy.android.ui.adapters.deals.filters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.filters.FilterValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 30/8/17.
 */

public class DealChildFilterAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FilterValue> itemList;
    private String filterMasterKey;


    private OnChildItemSelectedListener listener;

    public interface OnChildItemSelectedListener {
        void onSelectChild(String key, FilterValue checkboxModel);

        void onUnselectChild(String key, FilterValue checkboxModel);
    }


    public DealChildFilterAdapter(Context context,List<FilterValue> itemList,OnChildItemSelectedListener listener)
    {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    public DealChildFilterAdapter(Context context,String filterMasterKey,List<FilterValue> itemList,OnChildItemSelectedListener listener)
    {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
        this.filterMasterKey = filterMasterKey;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChildVH(LayoutInflater.from(context).inflate(R.layout.row_deal_value_filter_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ChildVH childVH = (ChildVH)holder;
        final FilterValue filterValue = itemList.get(position);
        childVH.itemCheckBox.setText(filterValue.name);

        childVH.itemCheckBox.setChecked(filterValue.isSelected);

        childVH.itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(position);
                if(childVH.itemCheckBox.isChecked()){
                    listener.onSelectChild(filterMasterKey,filterValue);
                }else{
                    listener.onUnselectChild(filterMasterKey+"",filterValue);
                }
                //  PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DEAL_CATID,item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class ChildVH extends BaseViewHolder {
        @BindView(R.id.itemCheckBox)
        public CheckBox itemCheckBox;

        public ChildVH(View view) {
            super(view);

        }
    }

    private void updateList(int position) {
        if (itemList.get(position).isSelected) {
            itemList.get(position).isSelected = false;
        } else {
            itemList.get(position).isSelected = true;
        }

        notifyItemChanged(position);
    }

    public List<String> getSelectedCategoryIdList(){
        List<String> selectedCategoryList= new ArrayList<>();

        for(int i=0;i<itemList.size();i++)
        {
            if(itemList.get(i).isSelected) {
                selectedCategoryList.add(itemList.get(i).id+"");
            }
        }
        return selectedCategoryList;
    }

    public void updateFilterData(String filterMasterKey,List<FilterValue> itemList)
    {
        this.filterMasterKey = filterMasterKey;
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clearList() {

        for (int i=0;i<itemList.size();i++)
        {
            itemList.get(i).isSelected = false;
        }
        notifyDataSetChanged();
    }

}

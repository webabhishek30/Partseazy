package com.partseazy.android.ui.adapters.deals.buy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.CheckboxModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 17/8/17.
 */

public class DealCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    public List<CheckboxModel> categoryList;
    private LayoutInflater mInflater;


    public DealCategoryAdapter(Context context, List<CheckboxModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryVH(mInflater.inflate(R.layout.row_checkbox_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryVH tagVH = (CategoryVH) holder;

        final CheckboxModel item = categoryList.get(position);

        tagVH.categoryCB.setText(item.value);
        tagVH.categoryCB.setChecked(item.isSelected);

        tagVH.categoryCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(position);
              //  PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DEAL_CATID,item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryVH extends BaseViewHolder {

        @BindView(R.id.categoryCB)
        protected CheckBox categoryCB;


        public CategoryVH(View itemView) {
            super(itemView);
        }
    }

    private void updateList(int position) {
        if (categoryList.get(position).isSelected) {
            categoryList.get(position).isSelected = false;
        } else {
            categoryList.get(position).isSelected = true;
        }

        notifyItemChanged(position);
    }

    public List<String> getSelectedCategoryIdList(){
        List<String> selectedCategoryList= new ArrayList<>();
        for(int i=0;i<categoryList.size();i++)
        {
            if(categoryList.get(i).isSelected) {
                selectedCategoryList.add(categoryList.get(i).key);
            }
        }
        return selectedCategoryList;
    }


}

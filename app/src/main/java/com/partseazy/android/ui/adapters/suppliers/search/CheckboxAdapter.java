package com.partseazy.android.ui.adapters.suppliers.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.CheckboxModel;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class CheckboxAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<CheckboxModel> itemList;

    public CheckboxAdapter(Context context, List<CheckboxModel> attributeList) {
        this.context = context;
        this.itemList = attributeList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckBoxViewHolder(mInflater.inflate(R.layout.row_checkbox_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CheckBoxViewHolder checkBoxViewHolder = (CheckBoxViewHolder) holder;
        final CheckboxModel checkBoxModel = itemList.get(position);
        if(checkBoxModel.isSelected)
        {
            checkBoxViewHolder.categoryCB.setChecked(true);
        }else{
            checkBoxViewHolder.categoryCB.setChecked(false);
        }
        checkBoxViewHolder.categoryCB.setText(checkBoxModel.value);
        checkBoxViewHolder.categoryCB.setTag(checkBoxModel);
        checkBoxViewHolder.categoryCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                CheckboxModel model = (CheckboxModel) checkBox.getTag();
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_SEARCH_CATEGORY,model);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class CheckBoxViewHolder extends BaseViewHolder {
        @BindView(R.id.categoryCB)
        protected CheckBox categoryCB;

        public CheckBoxViewHolder(View view) {
            super(view);

        }
    }
}


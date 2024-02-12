package com.partseazy.android.ui.adapters.suppliers.search;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.partseazy.android.R;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.supplier.placeAutocomplete.Prediction;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 22/3/17.
 */

public class AutoCompletePlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Prediction> itemList;
    private LayoutInflater mInflater;
    private final Context context;
    private OnPlaceClickListener listener;
    private Dialog dialog;


    public AutoCompletePlaceAdapter(Context context, List<Prediction> itemList, Dialog dialog) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.itemList = itemList;
        this.dialog = dialog;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AutoViewHolder(mInflater.inflate(R.layout.row_auto_complete_place_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        AutoViewHolder autoViewHolder = (AutoViewHolder)holder;
        final Prediction prediction = itemList.get(position);
        autoViewHolder.mainHeadingTV.setText(prediction.structuredFormatting.mainText);
        autoViewHolder.subHeadingTV.setText(prediction.structuredFormatting.secondaryText);
        autoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DataStore.addInRecentLocalityList(context, prediction);
                listener.onSelectPlaceClick(prediction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class AutoViewHolder extends BaseViewHolder {
        @BindView(R.id.mainHeadingTV)
        protected TextView mainHeadingTV;
        @BindView(R.id.subHeadingTV)
        protected TextView subHeadingTV;

        public AutoViewHolder(View view) {
            super(view);
        }
    }


    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }

}

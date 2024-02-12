package com.partseazy.android.ui.adapters.catalogue;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.catalogue.Bucket;
import com.partseazy.android.ui.model.catalogue.Facet;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 10/1/17.
 */

public class BucketFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Bucket> bucketList;
    private LayoutInflater mInflater;
    private onBucketSelectedListener listener;
    private Facet facet;

    public interface onBucketSelectedListener {
        void onSelectBucket(String facetCode, Bucket bucket);

        void unSelectBucket(String facetCode, Bucket bucket);
    }


    public BucketFilterAdapter(Context context, List<Bucket> bucketList, Facet facet, onBucketSelectedListener onBucketSelectedListener) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.bucketList = bucketList;
        this.listener = onBucketSelectedListener;
        this.facet = facet;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParentViewHolder(mInflater.inflate(R.layout.row_catalogue_check_filter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
        final Bucket bucket = bucketList.get(position);
        final CheckBox itemCheckBox = parentViewHolder.itemCheckBox;
        itemCheckBox.setTag(bucket);
        itemCheckBox.setText(bucket.value);
        if(bucket.value.equals(""))
        {
            itemCheckBox.setVisibility(View.GONE);
        }else{
            itemCheckBox.setVisibility(View.VISIBLE);
        }

        if (bucket.isSelected) {
            itemCheckBox.setChecked(true);
        } else {
            itemCheckBox.setChecked(false);
        }

        itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bucket bucket = (Bucket) itemCheckBox.getTag();
                if (itemCheckBox.isChecked()) {
                    bucket.isSelected = true;
                    listener.onSelectBucket(facet.code, bucket);
                } else {
                    bucket.isSelected = false;
                    listener.unSelectBucket(facet.code, bucket);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    class ParentViewHolder extends BaseViewHolder {
        @BindView(R.id.itemCheckBox)
        public CheckBox itemCheckBox;

        public ParentViewHolder(View view) {
            super(view);

        }
    }

    public void clearList(List<Bucket> itemList) {
       for (int i=0;i<bucketList.size();i++)
       {
            bucketList.get(i).isSelected = false;
       }
        notifyDataSetChanged();
    }
}
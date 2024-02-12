package com.partseazy.android.ui.adapters.search;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 7/2/17.
 */

public class SearchAutoSuggestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> autoSuggestList;
    private LayoutInflater mInflater;
    private  Context mContext;
    private Dialog searchDailog;

    public SearchAutoSuggestAdapter(Context context, List<String> autoSuggestList, Dialog dialog) {
        this.mContext = context;
        this.autoSuggestList = autoSuggestList;
        this.searchDailog = dialog;
        this.mInflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuggestionViewHolder(mInflater.inflate(R.layout.row_auto_suggest_item, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SuggestionViewHolder suggestionViewHolder = (SuggestionViewHolder)holder;
        suggestionViewHolder.suggestTV.setText(autoSuggestList.get(position));
        suggestionViewHolder.suggestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDailog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return autoSuggestList.size();
    }

    class SuggestionViewHolder extends BaseViewHolder {

        @BindView(R.id.suggestIconIV)
        protected ImageView suggestIconIV;
        @BindView(R.id.suggestTV)
        protected TextView suggestTV;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
        }
    }
}

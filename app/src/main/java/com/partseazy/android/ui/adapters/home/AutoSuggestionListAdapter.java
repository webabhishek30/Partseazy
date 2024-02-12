package com.partseazy.android.ui.adapters.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.home.autosuggestion.AutoSuggesstionKeywordData;

import java.util.ArrayList;
import java.util.List;

import static com.partseazy.android.base.BaseFragment.addToBackStack;


public class AutoSuggestionListAdapter extends RecyclerView.Adapter<AutoSuggestionListAdapter.MyViewHolder> {
   private List<AutoSuggesstionKeywordData> items;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView textViewSuggestionName;
        public MyViewHolder(View v) {
            super(v);
            textViewSuggestionName = (TextView) v.findViewById(R.id.textViewSuggestionName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AutoSuggestionListAdapter(Context context, List<AutoSuggesstionKeywordData> items) {
        this.context = context;
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AutoSuggestionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_autosuggestion_search, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AutoSuggesstionKeywordData item = items.get(position);
        holder.textViewSuggestionName.setText(item.getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
package com.partseazy.android.ui.adapters.deals.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.model.deal.SpinnerModel;

import java.util.List;

/**
 * Created by naveen on 4/5/17.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {


    private Context context;
    private LayoutInflater inflater;
    private List<SpinnerModel> spinnerModelList;
    private SpinnerModel tempValues = null;

    public CustomSpinnerAdapter(Context context, List<SpinnerModel> l2CategoryDataList) {
        this.context = context;
        this.spinnerModelList = l2CategoryDataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return spinnerModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return spinnerModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.row_custom_spinner, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = spinnerModelList.get(position);

        TextView itemNameTV = (TextView) row.findViewById(R.id.itemNameTV);
        ImageView itemIconIV = (ImageView) row.findViewById(R.id.itemIconIV);

        itemNameTV.setText(tempValues.spinnerValue);


        return row;
    }
}


package com.partseazy.android.ui.adapters.catalogue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.model.home.usershop.L3CategoryData;

import java.util.List;

/**
 * Created by Naveen Kumar on 31/1/17.
 */

public class CatalogueSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<L3CategoryData> l2CategoryDataList;
    private LayoutInflater inflater;
    private String tempValues = null;
    private int selectedPoistion = 0;


    public CatalogueSpinnerAdapter(Context context, List<L3CategoryData> l2CategoryDataList) {
        this.context = context;
        this.l2CategoryDataList = l2CategoryDataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public int getCount() {
        return l2CategoryDataList.size();
    }

    public Object getItem(int i) {
        return l2CategoryDataList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, true, parent);
    }

    public View getView(int i, View view, ViewGroup viewgroup) {

        return getCustomView(i, false, viewgroup);

    }

    public View getCustomView(int position, boolean isDropDownRow, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_drop_list, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (String) l2CategoryDataList.get(position).name;

        TextView label = (TextView) row.findViewById(android.R.id.text1);
        ImageView iv = (ImageView) row.findViewById(R.id.spinnerIV);

        label.setText(tempValues);

        if (isDropDownRow) {
            if (selectedPoistion == position) {
                iv.setVisibility(View.VISIBLE);

            } else {
                iv.setVisibility(View.INVISIBLE);

            }
        } else {
            iv.setVisibility(View.GONE);
        }

        return row;
    }


    public void updateSelectedItem(int position) {
        this.selectedPoistion = position;
    }


}

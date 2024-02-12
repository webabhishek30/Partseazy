package com.partseazy.android.ui.adapters.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.fragments.finance.Education;

import java.util.List;

/**
 * Created by naveen on 25/2/17.
 */

public class EducationAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<Education> educationList;
    private LayoutInflater inflater;
    private String tempValues = null;
    private int selectedPoistion = 0;


    public EducationAdapter(Context context, List<Education> l2CategoryDataList) {
        this.context = context;
        this.educationList = l2CategoryDataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public int getCount() {
        return educationList.size();
    }

    public Object getItem(int i) {
        return educationList.get(i);
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
        View row = inflater.inflate(R.layout.white_spinner_list_item, parent, false);

        tempValues = null;
        Education education  = (Education) educationList.get(position);

        TextView label = (TextView) row.findViewById(android.R.id.text1);

        label.setText(education.educationValue);

        return row;
    }


    public void updateSelectedItem(int position) {
        this.selectedPoistion = position;
    }


}

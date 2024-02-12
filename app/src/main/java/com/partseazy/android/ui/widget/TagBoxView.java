package com.partseazy.android.ui.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;

/**
 * Created by naveen on 30/12/16.
 */


public class TagBoxView extends RelativeLayout {
    private LayoutInflater inflater = null;
    private TextView tagNameTV;
    private LinearLayout tagLYT;
    private boolean isChecked = false;

    public TagBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    public TagBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public TagBoxView(Context context) {
        super(context);
        initViews();
    }

    void initViews() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_box_widget, this, true);
        tagNameTV = (TextView) findViewById(R.id.tagNameTV);
        tagLYT = (LinearLayout) findViewById(R.id.tagLYT);
    }


    public void setViewSelected(boolean isChecked) {
        this.isChecked = isChecked;
        if (isChecked) {
            tagNameTV.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            tagLYT.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tag_background_selected));
    ;
        } else {
            tagNameTV.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            tagLYT.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tag_background_unselected));
        }


    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setText(String text) {
        tagNameTV.setVisibility(VISIBLE);
        tagNameTV.setText(text);
    }
}

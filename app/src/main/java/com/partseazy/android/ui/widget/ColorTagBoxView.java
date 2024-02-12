package com.partseazy.android.ui.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;

/**
 * Created by naveen on 30/12/16.
 */

public class ColorTagBoxView extends RelativeLayout {
    private LayoutInflater inflater = null;
    private TextView tagNameTV;
    private LinearLayout tagLYT;
    private ImageView checkMarkIV;
    private View view;
    private boolean isChecked = false;

    public ColorTagBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    public ColorTagBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ColorTagBoxView(Context context) {
        super(context);
        initViews();
    }

    void initViews() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.color_tag_box_widget, this, true);
        tagNameTV = (TextView) findViewById(R.id.tagNameTV);
        tagLYT = (LinearLayout) findViewById(R.id.tagLYT);
        view = (View) findViewById(R.id.colorView);
        checkMarkIV = (ImageView)findViewById(R.id.checkMarkIV);
    }


    public void setViewSelected(boolean isChecked, int color) {
        this.isChecked = isChecked;
        checkMarkIV.setVisibility(GONE);
        view.setVisibility(VISIBLE);

        if (isChecked) {
            checkMarkIV.setVisibility(VISIBLE);
            view.setVisibility(GONE);
            if (color != -1) {
                tagNameTV.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                tagLYT.setBackgroundColor(color);
            }else{

                tagNameTV.setTextColor(ContextCompat.getColor(getContext(), R.color.green_success));
                tagLYT.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dull_white));
            }
        } else {
            checkMarkIV.setVisibility(GONE);
            view.setVisibility(VISIBLE);
            tagNameTV.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            tagLYT.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tag_background_unselected));
        }

    }

    public void setBoxIconColor(int color) {
        view.setBackgroundColor(color);
    }

    public boolean getChecked() {
        return isChecked;
    }


    public void setText(String text) {
        tagNameTV.setVisibility(VISIBLE);
        tagNameTV.setText(text);
    }
}

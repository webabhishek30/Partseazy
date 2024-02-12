package com.partseazy.android.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;

/**
 * Created by naveen on 21/12/16.
 */

public class RectangularCheckedBox extends RelativeLayout {
    private LayoutInflater inflater = null;
    private TextView priceTV;
    private ImageView checkIcon;
    private RelativeLayout boxRL;
    private boolean isChecked = false;

    public RectangularCheckedBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    public RectangularCheckedBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public RectangularCheckedBox(Context context) {
        super(context);
        initViews();
    }

    void initViews() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.checked_rectangle_box, this, true);
        priceTV = (TextView) findViewById(R.id.priceTV);
        checkIcon = (ImageView) findViewById(R.id.checkIconIV);
        boxRL = (RelativeLayout) findViewById(R.id.boxRL);
    }


    public void setViewSelected(boolean isChecked) {
        if (isChecked) {
            priceTV.setTextColor(ContextCompat.getColor(getContext(), R.color.green_success));
            boxRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.price_range_green_round_border));
            checkIcon.setVisibility(VISIBLE);
        } else {
            priceTV.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            boxRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.price_range_round_border));
            checkIcon.setVisibility(GONE);
        }


    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setText(String text) {
        priceTV.setVisibility(VISIBLE);
        priceTV.setText(text);
    }
}

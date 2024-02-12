package com.partseazy.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.partseazy.android.R;

/**
 * Created by naveen on 17/12/16.
 */


public class CircularCheckedImageView extends LinearLayout {
    private LayoutInflater inflater = null;
    private CircularImageView checkedCircularImage;
    private ImageView checkIcon;
    private boolean isChecked = false;

    public CircularCheckedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    public CircularCheckedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CircularCheckedImageView(Context context) {
        super(context);
        initViews();
    }

    void initViews() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.checked_circular_image, this, true);
        checkedCircularImage = (CircularImageView) findViewById(R.id.checkedCircularImage);
        checkIcon = (ImageView) findViewById(R.id.checkedIcon);
    }


    public CircularImageView getCircularImageView(boolean isChecked) {
        if (isChecked) {
            checkedCircularImage.setBorderColor(R.color.red);
            checkedCircularImage.setBorderOverlay(true);
            checkedCircularImage.setAlpha(1f);
            checkIcon.setImageResource(R.drawable.circular_checked_icon);
        } else {

            checkedCircularImage.setBorderColor(R.color.text_dark);
            checkedCircularImage.setDrawingCacheEnabled(true);
            checkedCircularImage.setAlpha(0.6f);
            checkIcon.setImageResource(R.drawable.circular_unchecked_icon);
        }
        return checkedCircularImage;

    }
    public void  setCircularImageViewSelected(boolean isChecked) {
        if (isChecked) {
            checkedCircularImage.setBorderColor(R.color.red);
            checkedCircularImage.setBorderOverlay(true);
            checkedCircularImage.setAlpha(1f);
            checkIcon.setImageResource(R.drawable.circular_checked_icon);
        } else {

            checkedCircularImage.setBorderColor(R.color.text_dark);
            checkedCircularImage.setDrawingCacheEnabled(true);
            checkedCircularImage.setAlpha(0.6f);
            checkIcon.setImageResource(R.drawable.circular_unchecked_icon);
        }


    }

    public void setDefaultImage()
    {
        checkedCircularImage.setImageResource(R.drawable.whitepic);
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
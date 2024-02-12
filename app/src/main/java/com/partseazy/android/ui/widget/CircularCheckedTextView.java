package com.partseazy.android.ui.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;

/**
 * Created by naveen on 4/5/17.
 */

public class CircularCheckedTextView extends RelativeLayout {
    private LayoutInflater inflater = null;
    private RelativeLayout circularCheckedRL;
    private TextView circularTV;
    private ImageView checkIcon;
    private boolean isChecked = false;

    public CircularCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    public CircularCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CircularCheckedTextView(Context context) {
        super(context);
        initViews();
    }

    void initViews() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.checked_circular_textview, this, true);
        circularCheckedRL = (RelativeLayout)findViewById(R.id.circularCheckedRL);
        circularTV = (TextView) findViewById(R.id.circularTV);
        checkIcon = (ImageView) findViewById(R.id.checkedIcon);
    }


    public TextView getCircularTextView() {
        return circularTV;
    }

    public void setText(String text) {
        if (circularTV != null) {
            circularTV.setText(text);
        }
    }

    public void setTextViewSelected(boolean isChecked) {
        if (isChecked) {
            checkIcon.setVisibility(VISIBLE);
            circularTV.setVisibility(GONE);
            circularCheckedRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.green_circle_filled));

        } else {
            checkIcon.setVisibility(GONE);
            circularTV.setVisibility(VISIBLE);
            circularCheckedRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gray_circle_filled));
        }


    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        if (isChecked) {
            checkIcon.setVisibility(VISIBLE);
            circularTV.setVisibility(GONE);
            circularCheckedRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.green_circle_filled));

        } else {
            checkIcon.setVisibility(GONE);
            circularTV.setVisibility(VISIBLE);
            circularCheckedRL.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gray_circle_filled));
        }
    }
}

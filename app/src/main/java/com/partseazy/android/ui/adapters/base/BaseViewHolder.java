package com.partseazy.android.ui.adapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.partseazy.android.utility.fonts.FontUtiity;

import butterknife.ButterKnife;

/**
 * Created by gaurav on 07/12/16.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        FontUtiity.changeFonts(itemView);


    }
}
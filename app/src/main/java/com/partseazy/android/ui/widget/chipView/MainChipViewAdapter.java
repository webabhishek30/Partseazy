package com.partseazy.android.ui.widget.chipView;

import android.content.Context;
import android.view.View;

import com.partseazy.android.R;
import com.partseazy.android.ui.model.supplier.search.TagModel;


/**
 * Created by naveen on 21/3/17.
 */

public class MainChipViewAdapter extends ChipViewAdapter {
    public MainChipViewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes(int position) {
        TagModel tag = (TagModel) getChip(position);

        switch (tag.getType()) {
            default:
                return R.layout.chip_close;
        }
    }

    @Override
    public int getBackgroundColor(int position) {
        TagModel tag = (TagModel) getChip(position);

        switch (tag.getType()) {
            default:
                return getColor(R.color.light_pink);

        }
    }

    @Override
    public int getBackgroundColorSelected(int position) {
        return 0;
    }

    @Override
    public int getBackgroundRes(int position) {
        return 0;
    }

    @Override
    public void onLayout(View view, int position) {
        TagModel tag = (TagModel) getChip(position);
    }
}

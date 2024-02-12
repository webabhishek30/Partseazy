package com.partseazy.android.ui.widget;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Naveen Kumar on 8/2/17.
 */

public abstract class ChangeScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_THRESHOLD = 250;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            setNewColor();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            setOldColor();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void setNewColor();
    public abstract void setOldColor();
}

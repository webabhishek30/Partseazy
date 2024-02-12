package com.partseazy.android.ui.model.deal.views;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shubhang on 07/11/17.
 */

public class ViewResultList {
    @SerializedName("result")
    @Expose
    public List<ViewUser> result;
}

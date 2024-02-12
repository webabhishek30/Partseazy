package com.partseazy.android.ui.model.deal.selldeallist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shubhang on 23/11/17.
 */

public class ReportCardList {
    @SerializedName("result")
    @Expose
    public List<ReportCardItem> dealItemList = null;
}

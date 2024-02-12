package com.partseazy.android.ui.model.deal.promotion.preview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 31/8/17.
 */

public class PromoteRetailerList {

    @SerializedName("result")
    @Expose
    public List<PromoteRetailerDetail> retailerList = null;
}

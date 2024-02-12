package com.partseazy.android.ui.model.deal.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 1/9/17.
 */

public class DealFilterListResult {
    @SerializedName("result")
    @Expose
    public List<FilterMaster> result = null;
}

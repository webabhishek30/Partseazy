package com.partseazy.android.ui.model.deal.subcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 4/5/17.
 */

public class DealSubCategoryResult {
    @SerializedName("result")
    @Expose
    public List<DealSubCategory> result = null;
}

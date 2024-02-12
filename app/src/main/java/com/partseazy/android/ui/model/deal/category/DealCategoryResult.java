
package com.partseazy.android.ui.model.deal.category;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealCategoryResult {

    @SerializedName("result")
    @Expose
    public List<DealCategory> result = null;

}

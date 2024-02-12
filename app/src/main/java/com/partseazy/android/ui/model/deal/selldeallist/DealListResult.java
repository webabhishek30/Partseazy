
package com.partseazy.android.ui.model.deal.selldeallist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealListResult {

    @SerializedName("result")
    @Expose
    public List<DealItem> dealItemList = null;

}

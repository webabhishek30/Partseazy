
package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockCorrection {

    @SerializedName("min_pieces")
    @Expose
    public Integer minPieces;
    @SerializedName("max_days")
    @Expose
    public Integer maxDays;
    @SerializedName("returnable")
    @Expose
    public Integer returnable;

}

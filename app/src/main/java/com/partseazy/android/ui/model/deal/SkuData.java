package com.partseazy.android.ui.model.deal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 5/5/17.
 */

public class SkuData {


    @SerializedName("desc")
    @Expose
    public String desc;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("mrp")
    @Expose
    public String mrp;

    @SerializedName("min_qty")
    @Expose
    public String minQty;

    @SerializedName("stock")
    @Expose
    public String stock;
}

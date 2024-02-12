
package com.partseazy.android.ui.model.deal.selldeallist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sku {

    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("discount")
    @Expose
    public Integer discount;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("min_qty")
    @Expose
    public Integer minQty;
    @SerializedName("mrp")
    @Expose
    public Integer mrp;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("remaining")
    @Expose
    public Integer remaining;
    @SerializedName("sold")
    @Expose
    public Integer sold;
    @SerializedName("stock")
    @Expose
    public Integer stock;

    @SerializedName("max_qty")
    @Expose
    public Integer maxQty;





}

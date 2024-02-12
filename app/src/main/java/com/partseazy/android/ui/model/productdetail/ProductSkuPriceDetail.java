package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by shubhang on 13/05/17.
 */

public class ProductSkuPriceDetail {

    // New Json Format
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("cost")
    @Expose
    public Integer cost;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("mrp")
    @Expose
    public Integer mrp;
    @SerializedName("info")
    @Expose
    public Map info;
    @SerializedName("is_special_price")
    @Expose
    public Integer isSpecialPrice;
    @SerializedName("divergent")
    @Expose
    public Integer divergent;
}

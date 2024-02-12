package com.partseazy.android.ui.model.customer_management;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhang on 15/02/18.
 */

public class ProductSKU {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("product_master_id")
    @Expose
    public Integer pmId;
    @SerializedName("product_id")
    @Expose
    public Integer pId;
    @SerializedName("supplier_id")
    @Expose
    public Integer supplierId;
}

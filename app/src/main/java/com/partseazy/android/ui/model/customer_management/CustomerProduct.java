package com.partseazy.android.ui.model.customer_management;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhang on 16/02/18.
 */

public class CustomerProduct {

    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("specification")
    @Expose
    public String specification;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("cost")
    @Expose
    public Integer cost;
    @SerializedName("stock")
    @Expose
    public Integer stock;
    @SerializedName("allow_customer_sale")
    @Expose
    public Integer allowCustomerSale;
    @SerializedName("product_sku_id")
    @Expose
    public Integer productSkuId;
    @SerializedName("product_item_id")
    @Expose
    public Integer productItemId;
}

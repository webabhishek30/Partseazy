package com.partseazy.android.ui.model.customer_management;

import com.partseazy.android.ui.model.deal.deal_detail.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhang on 08/02/18.
 */

public class SupplierDetailsResult {

    @SerializedName("address")
    @Expose
    public Address address = null;
    @SerializedName("customers")
    @Expose
    public Integer customers;
    @SerializedName("products")
    @Expose
    public Integer products;
    @SerializedName("shop_image")
    @Expose
    public String shopImage;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
    @SerializedName("supplier_id")
    @Expose
    public Integer supplierId;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
}

package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 29/12/16.
 */

public class ItemsCartItemData {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("quantity")
    @Expose
    public Integer quantity;
    @SerializedName("product_sku_id")
    @Expose
    public Integer productSkuId;
    @SerializedName("product_id")
    @Expose
    public Integer productId;
    @SerializedName("product_master_id")
    @Expose
    public Integer productMasterId;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}

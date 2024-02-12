
package com.partseazy.android.ui.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sku {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("product_master_id")
    @Expose
    public Integer productMasterId;
    @SerializedName("product_id")
    @Expose
    public Integer productId;
    @SerializedName("seller_id")
    @Expose
    public Integer sellerId;
    @SerializedName("bin")
    @Expose
    public String bin;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("spl_price_from")
    @Expose
    public Object splPriceFrom;
    @SerializedName("spl_price_to")
    @Expose
    public Object splPriceTo;
    @SerializedName("spl_price")
    @Expose
    public Object splPrice;
    @SerializedName("stock_up")
    @Expose
    public Integer stockUp;
    @SerializedName("isActive")
    @Expose
    public Integer active;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("machine_state")
    @Expose
    public Object machineState;
    @SerializedName("stated_at")
    @Expose
    public Object statedAt;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}

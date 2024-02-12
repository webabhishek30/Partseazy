
package com.partseazy.android.ui.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductInfo {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("product_master_id")
    @Expose
    public Integer productMasterId;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("bin")
    @Expose
    public String bin;
    @SerializedName("bag")
    @Expose
    public Bag_ bag;
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

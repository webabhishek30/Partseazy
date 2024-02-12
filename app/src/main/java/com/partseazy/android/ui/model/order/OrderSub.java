
package com.partseazy.android.ui.model.order;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSub {

    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("odin")
    @Expose
    public String odin;
    @SerializedName("master_odin")
    @Expose
    public String orderMasterOdin;
    @SerializedName("path")
    @Expose
    public Path path;
    @SerializedName("payable")
    @Expose
    public Integer payable;
    @SerializedName("product_master")
    @Expose
    public List<ProductMaster> productMaster = null;
    @SerializedName("state_comment")
    @Expose
    public String stateComment;
    @SerializedName("state_tag")
    @Expose
    public String stateTag;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}

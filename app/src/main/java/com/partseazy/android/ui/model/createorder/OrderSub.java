
package com.partseazy.android.ui.model.createorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSub {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("order_master_id")
    @Expose
    public Integer orderMasterId;
    @SerializedName("odin")
    @Expose
    public String odin;
    @SerializedName("path")
    @Expose
    public Path path;
    @SerializedName("payable")
    @Expose
    public Integer payable;
    @SerializedName("wms_odin")
    @Expose
    public Object wmsOdin;
    @SerializedName("created_at_wms")
    @Expose
    public Object createdAtWms;
    @SerializedName("remarks")
    @Expose
    public Object remarks;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
    @SerializedName("state_tag")
    @Expose
    public String stateTag;
    @SerializedName("state_comment")
    @Expose
    public String stateComment;
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

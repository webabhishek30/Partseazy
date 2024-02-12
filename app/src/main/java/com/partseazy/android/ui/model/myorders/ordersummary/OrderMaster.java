
package com.partseazy.android.ui.model.myorders.ordersummary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderMaster {

    @SerializedName("odin")
    @Expose
    public String odin;
    @SerializedName("agent_user_id")
    @Expose
    public Object agentUserId;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("retailer_id")
    @Expose
    public Integer retailerId;
    @SerializedName("amount")
    @Expose
    public Amount__ amount;
    @SerializedName("cur")
    @Expose
    public String cur;
    @SerializedName("coupon")
    @Expose
    public Object coupon;
    @SerializedName("cform")
    @Expose
    public Integer cform;
    @SerializedName("shipping")
    @Expose
    public String shipping;
    @SerializedName("delivery_address")
    @Expose
    public DeliveryAddress deliveryAddress;
    @SerializedName("comment")
    @Expose
    public Object comment;
    @SerializedName("payable")
    @Expose
    public Integer payable;
    @SerializedName("paid")
    @Expose
    public Integer paid;
    @SerializedName("balance")
    @Expose
    public Integer balance;
    @SerializedName("test")
    @Expose
    public Integer test;
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

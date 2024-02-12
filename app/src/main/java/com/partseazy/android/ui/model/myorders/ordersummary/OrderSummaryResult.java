
package com.partseazy.android.ui.model.myorders.ordersummary;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSummaryResult {

    @SerializedName("amount")
    @Expose
    public Amount amount;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("delivery_address")
    @Expose
    public DeliveryAddress deliveryAddress;

    @SerializedName("billing_address")
    @Expose
    public DeliveryAddress billingAddress;


    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("master_odin")
    @Expose
    public String masterOdin;
    @SerializedName("odin")
    @Expose
    public String odin;
    @SerializedName("order_line")
    @Expose
    public List<OrderLine> orderLine = null;
    @SerializedName("order_master")
    @Expose
    public OrderMaster orderMaster;
    @SerializedName("payable")
    @Expose
    public Integer payable;
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

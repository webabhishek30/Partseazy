
package com.partseazy.android.ui.model.deal.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Booking {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("bkin")
    @Expose
    public String bkin;
    @SerializedName("trade_id")
    @Expose
    public Integer tradeId;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("cur")
    @Expose
    public String cur;
    @SerializedName("amount")
    @Expose
    public Amount amount;
    @SerializedName("payable")
    @Expose
    public Integer payable;
    @SerializedName("delivery_address")
    @Expose
    public DeliveryAddress deliveryAddress;
    @SerializedName("payment_method")
    @Expose
    public Object paymentMethod;
    @SerializedName("ship_method")
    @Expose
    public String shipMethod;
    @SerializedName("active")
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

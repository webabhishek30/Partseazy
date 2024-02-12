
package com.partseazy.android.ui.model.deal.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingSku {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("trade_booking_id")
    @Expose
    public Integer tradeBookingId;
    @SerializedName("trade_id")
    @Expose
    public Integer tradeId;
    @SerializedName("trade_sku_id")
    @Expose
    public Integer tradeSkuId;
    @SerializedName("quantity")
    @Expose
    public Integer quantity;
    @SerializedName("amount")
    @Expose
    public Amount_ amount;
    @SerializedName("payable")
    @Expose
    public Integer payable;
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

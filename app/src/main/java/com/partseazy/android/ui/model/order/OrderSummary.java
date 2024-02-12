
package com.partseazy.android.ui.model.order;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSummary {

    @SerializedName("amount_paid")
    @Expose
    public Integer amountPaid;
    @SerializedName("delivery_address")
    @Expose
    public DeliveryAddress deliveryAddress;
    @SerializedName("order_sub")
    @Expose
    public List<OrderSub> orderSub = null;
    @SerializedName("total_amount")
    @Expose
    public Integer totalAmount;

}


package com.partseazy.android.ui.model.createorder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateOrder {

    @SerializedName("order_item")
    @Expose
    public List<OrderItem> orderItem = null;
    @SerializedName("order_master")
    @Expose
    public OrderMaster orderMaster;
    @SerializedName("order_payment")
    @Expose
    public OrderPayment orderPayment;
    @SerializedName("order_sub")
    @Expose
    public List<OrderSub> orderSub = null;

}

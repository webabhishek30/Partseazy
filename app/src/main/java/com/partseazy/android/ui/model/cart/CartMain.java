
package com.partseazy.android.ui.model.cart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartMain {

    @SerializedName("items")
    @Expose
    public List<CartItem> items = null;
    @SerializedName("order_summary")
    @Expose
    public OrderSummary orderSummary;

}

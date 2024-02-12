
package com.partseazy.android.ui.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSummary {

    @SerializedName("discount")
    @Expose
    public Integer discount;
    @SerializedName("shipping")
    @Expose
    public Integer shipping;
    @SerializedName("sub_total")
    @Expose
    public Integer subTotal;
    @SerializedName("tax")
    @Expose
    public Integer tax;
    @SerializedName("total")
    @Expose
    public Integer total;

}

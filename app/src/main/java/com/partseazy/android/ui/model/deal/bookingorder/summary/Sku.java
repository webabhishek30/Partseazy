
package com.partseazy.android.ui.model.deal.bookingorder.summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sku {

    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("qty")
    @Expose
    public Integer qty;

}

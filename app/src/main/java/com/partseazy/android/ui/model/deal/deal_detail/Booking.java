package com.partseazy.android.ui.model.deal.deal_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 21/6/17.
 */

public class Booking {
    @SerializedName("bkin")
    @Expose
    public String bkin;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("qty")
    @Expose
    public Integer qty;
    @SerializedName("price")
    @Expose
    public Integer price;
}

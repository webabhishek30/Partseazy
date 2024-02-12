package com.partseazy.android.ui.model.deal.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 16/6/17.
 */

public class BookingItem {


    @SerializedName("sku_id")
    @Expose
    public Integer skuId;

    @SerializedName("qty")
    @Expose
    public Integer qty;



}

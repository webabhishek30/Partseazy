package com.partseazy.android.ui.model.deal.bookingorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 21/6/17.
 */

public class BookingData {
    @SerializedName("bkin")
    @Expose
    public String bkin;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
}

package com.partseazy.android.ui.model.supplier.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopInfo {
    @SerializedName("brands")
    @Expose
    public List<String> brands = null;
    @SerializedName("close_time")
    @Expose
    public Integer closeTime;
    @SerializedName("contact_name")
    @Expose
    public String contactName;
    @SerializedName("floor")
    @Expose
    public String floor;
    @SerializedName("footfall")
    @Expose
    public String footfall;
    @SerializedName("format")
    @Expose
    public String format;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("images")
    @Expose
    public List<Image> images = null;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("open_time")
    @Expose
    public Integer openTime;
    @SerializedName("pincode_lock")
    @Expose
    public String pincodeLock;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("try_rooms")
    @Expose
    public Integer tryRooms;
    @SerializedName("turnover")
    @Expose
    public String turnover;
    @SerializedName("weekly_off")
    @Expose
    public String weeklyOff;
}

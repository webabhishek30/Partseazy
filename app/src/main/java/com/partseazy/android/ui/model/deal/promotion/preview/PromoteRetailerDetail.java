package com.partseazy.android.ui.model.deal.promotion.preview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 31/8/17.
 */

public class PromoteRetailerDetail {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("locality")
    @Expose
    public String locality;
    @SerializedName("district")
    @Expose
    public String district;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("pincode")
    @Expose
    public String pincode;
    @SerializedName("latitude")
    @Expose
    public Double latitude;
    @SerializedName("longitude")
    @Expose
    public Double longitude;
}

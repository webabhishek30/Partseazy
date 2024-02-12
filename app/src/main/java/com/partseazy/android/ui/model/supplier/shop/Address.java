package com.partseazy.android.ui.model.supplier.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 6/9/17.
 */

public class Address {
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("pincode")
    @Expose
    public String pincode;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("street")
    @Expose
    public String street;
}

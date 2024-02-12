package com.partseazy.android.ui.model.supplier.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 6/9/17.
 */

public class Location {


    @SerializedName("lat")
    @Expose
    public Float lat;
    @SerializedName("lon")
    @Expose
    public Float lon;

}

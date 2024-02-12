package com.partseazy.android.ui.model.supplier.geolocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 25/3/17.
 */

public class LocalityLocation {

    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lon")
    @Expose
    public Double lon;
}

package com.partseazy.android.ui.model.supplier.geolocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 24/3/17.
 */

public class GeoBox {
    @SerializedName("top_left")
    @Expose
    public LocalityLocation topLeft;
    @SerializedName("bottom_right")
    @Expose
    public LocalityLocation bottomRight;
}

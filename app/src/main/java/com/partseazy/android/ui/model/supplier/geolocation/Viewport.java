
package com.partseazy.android.ui.model.supplier.geolocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("northeast")
    @Expose
    public Northeast_ northeast;
    @SerializedName("southwest")
    @Expose
    public Southwest_ southwest;

}

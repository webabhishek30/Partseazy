
package com.partseazy.android.ui.model.supplier.geolocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bounds {

    @SerializedName("northeast")
    @Expose
    public Location northEast;
    @SerializedName("southwest")
    @Expose
    public Location southWest;

}


package com.partseazy.android.ui.model.supplier.geolocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoLocationResult {

    @SerializedName("results")
    @Expose
    public List<GeoPlaceData> results = null;
    @SerializedName("status")
    @Expose
    public String status;

}

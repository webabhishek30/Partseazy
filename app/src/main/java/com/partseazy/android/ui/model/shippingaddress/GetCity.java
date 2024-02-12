package com.partseazy.android.ui.model.shippingaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 27/12/16.
 */

public class GetCity {
    @SerializedName("data")
    @Expose
    public CityDetail data;
    @SerializedName("found")
    @Expose
    public Integer found;
}

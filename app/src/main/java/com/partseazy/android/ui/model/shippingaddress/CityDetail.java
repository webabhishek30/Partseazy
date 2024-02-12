package com.partseazy.android.ui.model.shippingaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 27/12/16.
 */

public class CityDetail {

    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("city_id")
    @Expose
    public Integer cityId;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("state_id")
    @Expose
    public Integer stateId;
}

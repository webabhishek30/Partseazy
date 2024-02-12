package com.partseazy.android.ui.model.shippingaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by can on 24/12/16.
 */

public class ShippingAddressList {

    @SerializedName("list")
    @Expose
    public List<ShippingAddressDetail> result = null;

    @SerializedName("selected")
    @Expose
    public Integer selected;
}

package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by can on 29/12/16.
 */

public class ShippingData {
    @SerializedName("available")
    @Expose
    public List<String> available = null;
    @SerializedName("options")
    @Expose
    public List<ShippinMethodData> methods = null;
    @SerializedName("selected")
    @Expose
    public String selected;
}

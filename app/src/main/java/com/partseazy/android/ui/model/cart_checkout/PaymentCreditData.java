package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by can on 29/12/16.
 */

public class PaymentCreditData implements Serializable{

    @SerializedName("enabled")
    @Expose
    public boolean enabled;
    @SerializedName("charge")
    @Expose
    public Integer charge;
    @SerializedName("pcent")
    @Expose
    public Double pcent;
    @SerializedName("total")
    @Expose
    public Integer total;
}

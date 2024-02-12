package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by can on 29/12/16.
 */

public class PaymentPrepaidData implements Serializable {

    @SerializedName("discount")
    @Expose
    public Integer discount;
    @SerializedName("pcent")
    @Expose
    public Double pcent;
    @SerializedName("total")
    @Expose
    public Integer total;

}

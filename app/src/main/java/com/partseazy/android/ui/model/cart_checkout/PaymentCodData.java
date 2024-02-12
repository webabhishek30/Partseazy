package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by can on 29/12/16.
 */

public class PaymentCodData implements Serializable {

    @SerializedName("charge")
    @Expose
    public Integer charges;
    @SerializedName("prepay")
    @Expose
    public PaymentCodPrepayData prepay;
    @SerializedName("total")
    @Expose
    public Integer total;

}

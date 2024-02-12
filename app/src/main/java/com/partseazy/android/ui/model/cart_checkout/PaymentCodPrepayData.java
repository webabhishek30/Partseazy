package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by can on 29/12/16.
 */

public class PaymentCodPrepayData implements Serializable{

    @SerializedName("pcent")
    @Expose
    public Integer pcent;
    @SerializedName("pending")
    @Expose
    public Integer pending;
    @SerializedName("required")
    @Expose
    public boolean required;
    @SerializedName("total")
    @Expose
    public Integer total;
}

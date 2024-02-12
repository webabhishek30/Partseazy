package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by can on 29/12/16.
 */

public class PaymentData {

    @SerializedName("available")
    @Expose
    public List<String> available = null;

    @SerializedName("cod")
    @Expose
    public PaymentCodData cod;
    @SerializedName("fin")
    @Expose
    public PaymentCreditData credit;
    @SerializedName("prepaid")
    @Expose
    public PaymentPrepaidData prepaid;


}

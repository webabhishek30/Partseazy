package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by CAN on 1/17/2017.
 */
public class CouponData {

    @SerializedName("applied")
    @Expose
    public Boolean applied;
    @SerializedName("coupon")
    @Expose
    public String coupon;
    @SerializedName("message")
    @Expose
    public String message;
}

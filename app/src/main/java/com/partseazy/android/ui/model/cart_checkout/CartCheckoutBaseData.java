package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 29/12/16.
 */

public class CartCheckoutBaseData {

    @SerializedName("messages")
    @Expose
    public java.util.List<String> messages = null;
    @SerializedName("list")
    @Expose
    public java.util.List<ItemsData> list = null;
    @SerializedName("summary")
    @Expose
    public SummaryData summary;
    @SerializedName("shipping")
    @Expose
    public ShippingData shipping;
    @SerializedName("payment")
    @Expose
    public PaymentData payment;
    @SerializedName("continue")
    @Expose
    public Boolean _continue;

    @SerializedName("coupon")
    @Expose
    public CouponData couponData;


    @SerializedName("count")
    @Expose
    public int cartCount;

    public String selectedAddresssId;

    public String selectedPaymentMethod;
}

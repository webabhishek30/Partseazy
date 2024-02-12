package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 29/12/16.
 */

public class ItemsFiguresData {

    @SerializedName("mrp")
    @Expose
    public Integer mrp;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("price_spl")
    @Expose
    public Integer priceSpl;
    @SerializedName("tax_vat")
    @Expose
    public Integer taxVat;
    @SerializedName("tax_cst")
    @Expose
    public Integer taxCst;
    @SerializedName("tax_refund")
    @Expose
    public Integer taxRefund;
    @SerializedName("coupon_rebate")
    @Expose
    public Integer couponRebate;
    @SerializedName("cart_rebate")
    @Expose
    public Integer cartRebate;
    @SerializedName("price_eff")
    @Expose
    public Integer priceEff;
    @SerializedName("sub_total")
    @Expose
    public Integer subTotal;
    @SerializedName("courier_fee")
    @Expose
    public Integer courierFee;
    @SerializedName("aux_fee")
    @Expose
    public Integer auxFee;
    @SerializedName("aux_rebate")
    @Expose
    public Integer auxRebate;
    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("cod_fee")
    @Expose
    public Integer codFee;
    @SerializedName("credit_fee")
    @Expose
    public Integer creditFee;
    @SerializedName("pg_fee")
    @Expose
    public Integer pgFee;
    @SerializedName("pay_rebate")
    @Expose
    public Integer payRebate;
    @SerializedName("final_total")
    @Expose
    public Integer finalTotal;
    @SerializedName("extra_fee")
    @Expose
    public Integer extraFee;
    @SerializedName("extra_rebate")
    @Expose
    public Integer extraRebate;
    @SerializedName("grand_total")
    @Expose
    public Integer grandTotal;

    @SerializedName("pricing")
    @Expose
    public Integer pricing;

    @SerializedName("tax_gst")
    @Expose
    public Integer taxGST;

    @SerializedName("tax_cess")
    @Expose
    public Integer taxCess;


}


package com.partseazy.android.ui.model.deal.bookingorder.summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amount {

    @SerializedName("mrp")
    @Expose
    public Integer mrp;
    @SerializedName("mop")
    @Expose
    public Integer mop;
    @SerializedName("market_price")
    @Expose
    public Integer marketPrice;
    @SerializedName("cost")
    @Expose
    public Integer cost;
    @SerializedName("cost_spl")
    @Expose
    public Integer costSpl;
    @SerializedName("costing")
    @Expose
    public Integer costing;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("price_spl")
    @Expose
    public Integer priceSpl;
    @SerializedName("pricing")
    @Expose
    public Integer pricing;
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
    @SerializedName("pg_fee")
    @Expose
    public Integer pgFee;
    @SerializedName("credit_fee")
    @Expose
    public Integer creditFee;
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
    @SerializedName("payable")
    @Expose
    public Integer payable;

}

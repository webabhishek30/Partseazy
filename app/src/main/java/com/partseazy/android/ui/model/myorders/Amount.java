
package com.partseazy.android.ui.model.myorders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amount {

    @SerializedName("aux_fee")
    @Expose
    public Integer auxFee;
    @SerializedName("aux_rebate")
    @Expose
    public Integer auxRebate;
    @SerializedName("cart_rebate")
    @Expose
    public Integer cartRebate;
    @SerializedName("cod_fee")
    @Expose
    public Integer codFee;
    @SerializedName("coupon_rebate")
    @Expose
    public Integer couponRebate;
    @SerializedName("courier_fee")
    @Expose
    public Integer courierFee;
    @SerializedName("credit_fee")
    @Expose
    public Integer creditFee;
    @SerializedName("extra_fee")
    @Expose
    public Integer extraFee;
    @SerializedName("extra_rebate")
    @Expose
    public Integer extraRebate;
    @SerializedName("final_total")
    @Expose
    public Integer finalTotal;
    @SerializedName("grand_total")
    @Expose
    public Integer grandTotal;
    @SerializedName("mop")
    @Expose
    public Integer mop;
    @SerializedName("mrp")
    @Expose
    public Integer mrp;
    @SerializedName("pay_rebate")
    @Expose
    public Integer payRebate;
    @SerializedName("pg_fee")
    @Expose
    public Integer pgFee;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("price_eff")
    @Expose
    public Integer priceEff;
    @SerializedName("price_spl")
    @Expose
    public Integer priceSpl;
    @SerializedName("sub_total")
    @Expose
    public Integer subTotal;
    @SerializedName("tax_cst")
    @Expose
    public Integer taxCst;
    @SerializedName("tax_refund")
    @Expose
    public Integer taxRefund;
    @SerializedName("tax_vat")
    @Expose
    public Integer taxVat;
    @SerializedName("total")
    @Expose
    public Integer total;

}

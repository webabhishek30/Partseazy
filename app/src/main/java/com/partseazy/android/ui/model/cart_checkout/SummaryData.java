package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 29/12/16.
 */

public class SummaryData {

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
    @SerializedName("courier_fee")
    @Expose
    public Integer courierFee;
    @SerializedName("cod_fee")
    @Expose
    public Integer codFee;
    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("bulk_rebate")
    @Expose
    public Integer bulkRebate;
    @SerializedName("grand_total")
    @Expose
    public Integer grandTotal;

    @SerializedName("sub_total")
    @Expose
    public Integer sub_total;
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

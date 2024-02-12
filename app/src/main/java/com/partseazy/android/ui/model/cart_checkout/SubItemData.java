package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Adeptical Solutions on 1/6/2017.
 */

public class SubItemData {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("pid")
    @Expose
    public Integer pid;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("qty")
    @Expose
    public Integer qty;
    @SerializedName("attr")
    @Expose
    public String attr;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("dispatch")
    @Expose
    public Integer dispatch;
    @SerializedName("delivery")
    @Expose
    public Integer delivery;
    @SerializedName("margin")
    @Expose
    public Double margin;

    @SerializedName("ok")
    @Expose
    public Boolean ok;
    @SerializedName("figure")
    @Expose
    public ItemsFiguresData figures;
    @SerializedName("cart_item")
    @Expose
    public ItemsCartItemData cartItem;

    @SerializedName("stock")
    @Expose
    public Integer stock;

    public Boolean isThereAnyErrorInSubItem;
    public String errortSubItem;

    @SerializedName("error")
    @Expose
    public String error_type;


    @SerializedName("min_qty")
    @Expose
    public Integer minQty;



}

package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 29/12/16.
 */

public class ItemsData {

    @SerializedName("reference")
    @Expose
    public String reference;
    @SerializedName("product_master_id")
    @Expose
    public Integer productMasterId;
    @SerializedName("category_id")
    @Expose
    public Integer categoryId;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("name")
    @Expose
    public String name;
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
    public Integer margin;
    @SerializedName("min_qty")
    @Expose
    public Integer minQty;
    @SerializedName("max_qty")
    @Expose
    public Integer maxQty;

    @SerializedName("ok")
    @Expose
    public Boolean ok;
    @SerializedName("figure")
    @Expose
    public ItemsFiguresData figures;
    @SerializedName("rows")
    @Expose
    public java.util.List<SubItemData> rows = null;


    @SerializedName("error")
    @Expose
    public String error_type;


    public Boolean isThereAnyErrorInCart;
    public String errortCart;
}

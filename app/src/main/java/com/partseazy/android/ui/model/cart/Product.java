
package com.partseazy.android.ui.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product")
    @Expose
    public ProductInfo product;
    @SerializedName("sku")
    @Expose
    public Sku sku;

}

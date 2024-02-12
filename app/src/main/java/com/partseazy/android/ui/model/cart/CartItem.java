
package com.partseazy.android.ui.model.cart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartItem {

    @SerializedName("isProductSet")
    @Expose
    public Boolean isProductSet;
    @SerializedName("product_master")
    @Expose
    public ProductMaster productMaster;
    @SerializedName("products")
    @Expose
    public List<Product> products = null;
    @SerializedName("quantity")
    @Expose
    public String quantity;

}

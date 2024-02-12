package com.partseazy.android.ui.model.cart;

/**
 * Created by gaurav on 23/12/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemCount {

    @SerializedName("cart_count")
    @Expose
    public Integer cartCount;
    @SerializedName("fav_count")
    @Expose
    public Integer favCount;
    @SerializedName("cart_amount")
    @Expose
    public Integer cartAmount;
}


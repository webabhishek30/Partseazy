
package com.partseazy.android.ui.model.addtocart;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToCartResponse {

    @SerializedName("items")
    @Expose
    public List<Item> items = null;

}

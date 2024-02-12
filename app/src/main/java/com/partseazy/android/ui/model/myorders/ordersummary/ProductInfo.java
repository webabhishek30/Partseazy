
package com.partseazy.android.ui.model.myorders.ordersummary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductInfo {

    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("tag")
    @Expose
    public String tag;

}

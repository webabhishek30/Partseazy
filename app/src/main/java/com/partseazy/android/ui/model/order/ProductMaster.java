
package com.partseazy.android.ui.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductMaster {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("quantity")
    @Expose
    public Integer quantity;

}

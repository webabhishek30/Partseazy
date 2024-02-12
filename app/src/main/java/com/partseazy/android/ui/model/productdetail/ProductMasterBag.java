
package com.partseazy.android.ui.model.productdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductMasterBag {

    @SerializedName("items")
    @Expose
    public List<Item> items = null;
    @SerializedName("name")
    @Expose
    public String name;

}

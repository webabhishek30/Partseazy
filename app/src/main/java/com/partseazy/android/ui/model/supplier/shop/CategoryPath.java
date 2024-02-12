package com.partseazy.android.ui.model.supplier.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 6/9/17.
 */

public class CategoryPath {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("level")
    @Expose
    public Integer level;
    @SerializedName("name")
    @Expose
    public String name;

}

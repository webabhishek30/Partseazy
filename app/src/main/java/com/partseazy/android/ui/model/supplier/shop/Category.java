package com.partseazy.android.ui.model.supplier.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class Category {

    @SerializedName("category_path")
    @Expose
    public List<CategoryPath> categoryPath = null;
}

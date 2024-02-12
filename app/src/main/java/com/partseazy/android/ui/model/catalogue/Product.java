package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by taushif on 17/1/17.
 */

public class Product {
    @SerializedName("category_path")
    @Expose
    public List<CategoryPath> categoryPath = null;
    @SerializedName("in_stock")
    @Expose
    public int inStock;
    @SerializedName("margin")
    @Expose
    public int margin;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("price_high")
    @Expose
    public Integer priceHigh;
    @SerializedName("product_master")
    @Expose
    public ProductMaster productMaster;
    @SerializedName("products")
    @Expose
    public List<Product_> products = null;
    @SerializedName("score")
    @Expose
    public double score;

}

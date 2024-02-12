package com.partseazy.android.ui.model.supplier.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class Shop {
    @SerializedName("address")
    @Expose
    public Address address;
    @SerializedName("brands")
    @Expose
    public List<Brand> brands = null;
    @SerializedName("categories")
    @Expose
    public List<Category> categories = null;
    @SerializedName("score")
    @Expose
    public Float score;
    @SerializedName("shop_info")
    @Expose
    public ShopInfo shopInfo;
    @SerializedName("user")
    @Expose
    public User user;
}

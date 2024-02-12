package com.partseazy.android.ui.model.home.products;

import com.partseazy.android.ui.model.catalogue.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naveen Kumar on 25/1/17.
 */

public class HomeProductsResult {

    @SerializedName("result")
    @Expose
    public List<Product> productList = null;
}

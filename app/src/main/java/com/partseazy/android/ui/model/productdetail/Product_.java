
package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product_ {

//    @SerializedName("product_bag")
//    @Expose
//    public ProductBag productBag;


    // New Json Format
    @SerializedName("allow_sale")
    @Expose
    public Integer allowSale;
    @SerializedName("allow_sample")
    @Expose
    public Integer allowSample;
    @SerializedName("bag")
    @Expose
    public List<ProductBag> productBag = null;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("sample_price")
    @Expose
    public Integer samplePrice;
    @SerializedName("sku_id")
    @Expose
    public Integer skuId;
    @SerializedName("stock")
    @Expose
    public Integer stock;

    @SerializedName("min_qty")
    @Expose
    public Integer minQty;

}

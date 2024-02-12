package com.partseazy.android.ui.model.home.category;

import java.io.Serializable;

/**
 * Created by Naveen Kumar on 25/1/17.
 */

public class ProductData implements Serializable {

    public int productMasterId;
    public String productName;
    public int productPrice;
    public int productPriceHigh;
    public int margin;
    public String image;
    public int minQty;
    public int packQty;
    public int inStock;
    public String format;
    public int categoryId;

    // for single product skus
    public String productSkuDesc;
    public boolean isProductSku;
    public int productSkuId;
}

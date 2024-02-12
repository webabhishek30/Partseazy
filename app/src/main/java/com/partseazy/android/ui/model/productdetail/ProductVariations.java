package com.partseazy.android.ui.model.productdetail;

import com.partseazy.android.ui.fragments.product.ProductDetailFragment;

/**
 * Created by gaurav on 07/01/17.
 */

public class ProductVariations {


    //Type of Design i.e. Sinle Tab, single clomun ..
    public ProductDetailFragment.ATTRIBUTE_VIEW attribType;
    //tab1
    public String tabbedAttrName1;
    public String tabbedAttrValue1;

    //tab2
    public String tabbedAttrName2;
    public String tabbedAttrValue2;

    //column1
    public String columnAttrName1;
    public String columnAttrName2;

    //column2
    public String columnAttrValue1;
    public String columnAttrValue2;

    public int skuId;
    public int price;
    public int allow_sample;
    public int sample_price;




}

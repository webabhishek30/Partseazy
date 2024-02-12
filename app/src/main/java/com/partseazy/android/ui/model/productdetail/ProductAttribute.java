package com.partseazy.android.ui.model.productdetail;

/**
 * Created by gaurav on 06/01/17.
 */

public class ProductAttribute {

    public boolean isSelected;
    public int price;
    public String value;
    public int productID;
    public String name;
    public String name1;
    public String value1;
    public int setQuantity;
    public int stockQuantity;
    public int minQuantity;
    public boolean isSaleAllowed;
    public boolean isSampleProduct;


    public ProductAttribute() {

    }

    public ProductAttribute(String name, String value, String name1, String value1, int productID, boolean isSelected, int price, int quantity, int maxQuantity) {
        this.name = name;
        this.value = value;
        this.name1 = name1;
        this.value1 = value1;
        this.productID = productID;
        this.isSelected = isSelected;
        this.price = price;
        this.setQuantity = quantity;
        this.stockQuantity = maxQuantity;
    }


}

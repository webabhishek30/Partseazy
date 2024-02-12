package com.partseazy.android.ui.model.productdialog;

/**
 * Created by naveen on 2/1/17.
 */

public class ProductAttributeModel {
    public int id;
    public String attributeName;
    public boolean isSelected;
    public int price;
    public ProductAttributeModel(int id,String attributeName,int price)
    {
        this.id = id;
        this.attributeName = attributeName;
        this.price = price;
    }
}

package com.partseazy.android.ui.model.productdetail;

import java.util.List;

/**
 * Created by gaurav on 08/01/17.
 */

public class TabbedProductAttribute {


    public boolean isSelected;
    public String value;
    public List<Integer> productIDList;
    public String name;

    public TabbedProductAttribute() {

    }


    public TabbedProductAttribute(String name, String value,List<Integer> productID, boolean isSelected) {
        this.name = name;
        this.value = value;
        this.productIDList = productID;
        this.isSelected = isSelected;
    }
}

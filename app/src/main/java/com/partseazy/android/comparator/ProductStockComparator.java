package com.partseazy.android.comparator;

import com.partseazy.android.ui.model.productdetail.ProductAttribute;

import java.util.Comparator;

/**
 * Created by Naveen Kumar on 9/2/17.
 */

public class ProductStockComparator implements Comparator<ProductAttribute> {



    public int compare(ProductAttribute attribute1,ProductAttribute attribute2){


        if(attribute1.stockQuantity ==attribute2.stockQuantity)
            return 0;
        else if(attribute1.stockQuantity <attribute2.stockQuantity)
            return 1;
        else
            return -1;
    }
}

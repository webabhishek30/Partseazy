package com.partseazy.android.ui.fragments.factory_data;

import com.partseazy.android.ui.callbacks.UpdateProductQuantityCallback;

/**
 * Created by CAN on 1/10/2017.
 */

public class ProductQuantityFactory implements UpdateProductQuantityCallback {

    private static int total_Quantity;

    private static int totalQTy(){
        total_Quantity=total_Quantity+total_Quantity;
        return total_Quantity;
    }

    public static boolean getProductQuantity(int minQty) {
        if (minQty<totalQTy()){
            return true;
        }
        return false;
    }




    @Override
    public int modifyQty(int productQty) {
        total_Quantity=productQty;
        return total_Quantity;
    }
}

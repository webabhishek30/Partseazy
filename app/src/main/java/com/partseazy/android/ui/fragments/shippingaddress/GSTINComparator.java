package com.partseazy.android.ui.fragments.shippingaddress;

import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;

import java.util.Comparator;

/**
 * Created by naveen on 12/7/17.
 */

public class GSTINComparator implements Comparator<ShippingAddressDetail> {
    @Override
    public int compare(ShippingAddressDetail o1, ShippingAddressDetail o2) {

//        if(o1.gstn==null && o2!=null) {
//            return o1.gstn.compareTo(o2.gstn);
//        }else {
//            return 0;
//        }

        if (o1.gstn == null) {
            if (o2.gstn == null) {
                return 0;
            }
            return 1;
        } else if (o2.gstn == null) {
            return -1;
        }
        return o1.gstn.compareTo(o2.gstn);
    }
}

package com.partseazy.android.ui.model.customer_management;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shubhang on 16/02/18.
 */

public class CustomerProductList {
    @SerializedName("result")
    @Expose
    public List<CustomerProduct> result = null;
}

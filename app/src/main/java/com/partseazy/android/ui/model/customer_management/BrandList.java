package com.partseazy.android.ui.model.customer_management;

import com.partseazy.android.ui.model.productdetail.Brand;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shubhang on 14/02/18.
 */

public class BrandList {
    @SerializedName("result")
    @Expose
    public List<Brand> result = null;
}

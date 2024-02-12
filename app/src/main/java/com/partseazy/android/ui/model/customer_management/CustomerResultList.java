package com.partseazy.android.ui.model.customer_management;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shubhang on 15/02/18.
 */

public class CustomerResultList {
    @SerializedName("result")
    @Expose
    public List<Customer> result = null;
}

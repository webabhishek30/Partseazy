package com.partseazy.android.ui.model.agentapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 13/2/17.
 */

public class RetailerResult {

    @SerializedName("address")
    @Expose
    public Object address;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("pincode_lock")
    @Expose
    public String pincodeLock;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
}


package com.partseazy.android.ui.model.createorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditOrder {

    @SerializedName("customer_approved")
    @Expose
    public Boolean customerApproved;
    @SerializedName("customer_register")
    @Expose
    public Boolean customerRegister;

}

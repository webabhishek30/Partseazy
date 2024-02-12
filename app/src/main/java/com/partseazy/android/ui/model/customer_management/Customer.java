package com.partseazy.android.ui.model.customer_management;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhang on 15/02/18.
 */

public class Customer {
    @SerializedName("customer_user_id")
    @Expose
    public Integer customerUserId;
    @SerializedName("name")
    @Expose
    public String name = null;
    @SerializedName("email")
    @Expose
    public String email = null;
    @SerializedName("dob")
    @Expose
    public String dob = null;
    @SerializedName("anniversary")
    @Expose
    public String anniversary = null;
    @SerializedName("groups")
    @Expose
    public Object groups = null;
    @SerializedName("mobile")
    @Expose
    public String mobile;
}

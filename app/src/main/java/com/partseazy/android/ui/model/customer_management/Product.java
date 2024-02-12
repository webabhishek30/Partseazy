package com.partseazy.android.ui.model.customer_management;

import com.partseazy.android.ui.model.productdetail.Image_;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by shubhang on 15/02/18.
 */

public class Product {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("product_master_id")
    @Expose
    public Integer pmId;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("bcin")
    @Expose
    public String bcin;
    @SerializedName("mrp")
    @Expose
    public Integer mrp;
    @SerializedName("mop")
    @Expose
    public Integer mop;
    @SerializedName("min_qty")
    @Expose
    public Integer minQty;
    @SerializedName("reference")
    @Expose
    public String reference;
    @SerializedName("bag")
    @Expose
    public LinkedTreeMap bag;
    @SerializedName("image")
    @Expose
    public Image_ image = null;
    @SerializedName("active")
    @Expose
    public Integer active;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
}

package com.partseazy.android.ui.model.deal.demo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 22/8/17.
 */

public class DemoData {


    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("tdin")
    @Expose
    public String tdin;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("trade_id")
    @Expose
    public Integer tradeId;
    @SerializedName("supplier_user_id")
    @Expose
    public Integer supplierUserId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("Mobile")
    @Expose
    public String mobile;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("image")
    @Expose
    public Object image;
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
    @SerializedName("state_tag")
    @Expose
    public String stateTag;
    @SerializedName("state_comment")
    @Expose
    public String stateComment;
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

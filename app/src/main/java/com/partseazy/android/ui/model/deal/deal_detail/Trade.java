
package com.partseazy.android.ui.model.deal.deal_detail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trade {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("trin")
    @Expose
    public String trin;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("l2")
    @Expose
    public Integer l2;
    @SerializedName("l3")
    @Expose
    public Integer l3;
    @SerializedName("images")
    @Expose
    public List<Image> images = null;
    @SerializedName("starting_at")
    @Expose
    public String startingAt;
    @SerializedName("ending_at")
    @Expose
    public String endingAt;
    @SerializedName("public")
    @Expose
    public Integer _public;
    @SerializedName("contacts")
    @Expose
    public Object contacts;
    @SerializedName("escrow")
    @Expose
    public Integer escrow;
    @SerializedName("pay_methods")
    @Expose
    public List<String> payMethods = null;
    @SerializedName("tax_included")
    @Expose
    public Integer taxIncluded;
    @SerializedName("allow_demo")
    @Expose
    public Integer allowDemo;
    @SerializedName("address_id")
    @Expose
    public Integer addressId;
    @SerializedName("ship_methods")
    @Expose
    public List<String> shipMethods = null;
    @SerializedName("courier_fee")
    @Expose
    public Integer courierFee;
    @SerializedName("dispatch_in")
    @Expose
    public Integer dispatchIn;
    @SerializedName("active")
    @Expose
    public Integer active;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("meta_title")
    @Expose
    public String metaTitle;
    @SerializedName("meta_desc")
    @Expose
    public String metaDesc;
    @SerializedName("meta_keywords")
    @Expose
    public String metaKeywords;
    @SerializedName("sitemap")
    @Expose
    public Integer sitemap;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("machine_state")
    @Expose
    public Object machineState;
    @SerializedName("stated_at")
    @Expose
    public Object statedAt;
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

    @SerializedName("terms")
    @Expose
    public String terms;

}

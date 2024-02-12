package com.partseazy.android.ui.model.deal.create_deal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 10/5/17.
 */

public class NewDealData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("tin")
    @Expose
    public String tin;
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
    public Object images;
    @SerializedName("attachments")
    @Expose
    public List<Attachment> attachments = null;
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
    @SerializedName("allow_cod")
    @Expose
    public Integer allowCod;
    @SerializedName("allow_prepaid")
    @Expose
    public Integer allowPrepaid;
    @SerializedName("allow_payless")
    @Expose
    public Integer allowPayless;
    @SerializedName("tax_included")
    @Expose
    public Integer taxIncluded;
    @SerializedName("buyer_pickup")
    @Expose
    public Integer buyerPickup;
    @SerializedName("seller_ships")
    @Expose
    public Integer sellerShips;
    @SerializedName("address_id")
    @Expose
    public Integer addressId;
    @SerializedName("courier_fee")
    @Expose
    public Integer courierFee;
    @SerializedName("dispatch")
    @Expose
    public Integer dispatch;
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
}

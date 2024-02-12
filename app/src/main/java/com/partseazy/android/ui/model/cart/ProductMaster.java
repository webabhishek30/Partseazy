
package com.partseazy.android.ui.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductMaster {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("page")
    @Expose
    public Object page;
    @SerializedName("bin")
    @Expose
    public String bin;
    @SerializedName("brand_id")
    @Expose
    public Integer brandId;
    @SerializedName("category_id")
    @Expose
    public Integer categoryId;
    @SerializedName("format")
    @Expose
    public String format;
    @SerializedName("saleable")
    @Expose
    public String saleable;
    @SerializedName("pack_of")
    @Expose
    public Integer packOf;
    @SerializedName("min_qty")
    @Expose
    public Integer minQty;
    @SerializedName("bag")
    @Expose
    public Bag bag;
    @SerializedName("isActive")
    @Expose
    public Integer active;
    @SerializedName("web_url")
    @Expose
    public String webUrl;
    @SerializedName("app_url")
    @Expose
    public String appUrl;
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

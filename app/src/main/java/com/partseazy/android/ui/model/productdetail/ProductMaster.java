
package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("bcin")
    @Expose
    public String bcin;
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
    @SerializedName("exponential")
    @Expose
    public Integer exponential;
    @SerializedName("cur")
    @Expose
    public String cur;
    @SerializedName("bag")
    @Expose
    public Bag bag;
    @SerializedName("images")
    @Expose
    public List<Image_> images = null;
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

    @SerializedName("max_qty")
    @Expose
    public Integer maxQty;

}


package com.partseazy.android.ui.model.productdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("parent_id")
    @Expose
    public Integer parentId;
    @SerializedName("path")
    @Expose
    public List<Integer> path = null;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("level")
    @Expose
    public Integer level;
    @SerializedName("image")
    @Expose
    public Image image;
    @SerializedName("icon")
    @Expose
    public Icon icon;
    @SerializedName("isActive")
    @Expose
    public Integer active;
    @SerializedName("sequence")
    @Expose
    public Integer sequence;
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
    public Info info;
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

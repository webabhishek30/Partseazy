package com.partseazy.android.ui.model.deal.subcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 4/5/17.
 */

public class DealSubCategory {
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
    @SerializedName("active")
    @Expose
    public Integer active;
    @SerializedName("sequence")
    @Expose
    public Integer sequence;
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

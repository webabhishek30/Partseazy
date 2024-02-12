package com.partseazy.android.ui.model.deal.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 4/5/17.
 */

public class DealCategory {
    @SerializedName("active")
    @Expose
    public Integer active;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("icon")
    @Expose
    public Icon icon;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image")
    @Expose
    public Image image;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("level")
    @Expose
    public Integer level;
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("meta_desc")
    @Expose
    public String metaDesc;
    @SerializedName("meta_keywords")
    @Expose
    public String metaKeywords;
    @SerializedName("meta_title")
    @Expose
    public String metaTitle;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("parent_id")
    @Expose
    public Integer parentId;
    @SerializedName("path")
    @Expose
    public List<Integer> path = null;
    @SerializedName("sequence")
    @Expose
    public Integer sequence;
    @SerializedName("sitemap")
    @Expose
    public Integer sitemap;
    @SerializedName("state_comment")
    @Expose
    public String stateComment;
    @SerializedName("state_tag")
    @Expose
    public String stateTag;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("url")
    @Expose
    public String url;
}

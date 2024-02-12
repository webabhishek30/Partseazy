package com.partseazy.android.ui.model.registration.banner;

import android.graphics.drawable.Icon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 13/12/16.
 */

public class MainCategoryList {

    @SerializedName("isActive")
    @Expose
    public Integer active;
    @SerializedName("app_url")
    @Expose
    public String appUrl;
    @SerializedName("children")
    @Expose
    public List<Child> children = null;
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
    public Icon_ icon;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image")
    @Expose
    public Image_ image;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("level")
    @Expose
    public Integer level;
    @SerializedName("machine_state")
    @Expose
    public Object machineState;
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
    @SerializedName("stated_at")
    @Expose
    public Object statedAt;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("web_url")
    @Expose
    public String webUrl;

    public class Image {

        @SerializedName("ref")
        @Expose
        public Integer ref;
        @SerializedName("src")
        @Expose
        public String src;

    }

    public class Image_ {

        @SerializedName("ref")
        @Expose
        public Integer ref;
        @SerializedName("src")
        @Expose
        public String src;

    }

    public class Icon_ {

        @SerializedName("ref")
        @Expose
        public Integer ref;
        @SerializedName("src")
        @Expose
        public String src;

    }

    public class Child {

        @SerializedName("isActive")
        @Expose
        public Integer active;
        @SerializedName("app_url")
        @Expose
        public String appUrl;
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
        public Object machineState;
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
        @SerializedName("stated_at")
        @Expose
        public Object statedAt;
        @SerializedName("tags")
        @Expose
        public Object tags;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("web_url")
        @Expose
        public String webUrl;

    }
}
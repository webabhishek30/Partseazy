package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by taushif on 17/1/17.
 */

public class ProductMaster {
    @SerializedName("isActive")
    @Expose
    public int active;
    @SerializedName("app_url")
    @Expose
    public String appUrl;
    @SerializedName("bcin")
    @Expose
    public String bcin;
    @SerializedName("brand_id")
    @Expose
    public int brandId;
    @SerializedName("brand_name")
    @Expose
    public String brandName;
    @SerializedName("category_id")
    @Expose
    public int categoryId;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("deleted")
    @Expose
    public int deleted;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("facet_bag")
    @Expose
    public List<FacetBag> facetBag = null;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("images")
    @Expose
    public List<Image> images = null;
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("min_qty")
    @Expose
    public int minQty;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("pack_of")
    @Expose
    public int packOf;
    @SerializedName("search_bag")
    @Expose
    public List<SearchBag> searchBag = null;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("web_url")
    @Expose
    public String webUrl;
    @SerializedName("format")
    @Expose
    public String format;

}

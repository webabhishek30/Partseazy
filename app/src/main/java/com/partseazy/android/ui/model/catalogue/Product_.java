package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by taushif on 17/1/17.
 */

public class Product_ {



    @SerializedName("active")
    @Expose
    public Integer active;
    @SerializedName("bcin")
    @Expose
    public String bcin;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("facet_bag")
    @Expose
    public List<FacetBag_> facetBag = null;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("margin")
    @Expose
    public Integer margin;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("search_bag")
    @Expose
    public List<SearchBag_> searchBag = null;
}

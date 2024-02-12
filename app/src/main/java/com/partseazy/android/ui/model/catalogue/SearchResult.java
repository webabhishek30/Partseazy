package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naveen Kumar on 6/2/17.
 */

public class SearchResult {
    @SerializedName("facets")
    @Expose
    public List<Facet> facets = null;
    @SerializedName("meta")
    @Expose
    public Meta meta;
    @SerializedName("products")
    @Expose
    public List<Product> products = null;
}

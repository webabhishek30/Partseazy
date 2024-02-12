package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by taushif on 17/1/17.
 */

public class CatalogueResult {
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

package com.partseazy.android.ui.model.supplier.shop;

import com.partseazy.android.ui.model.catalogue.Meta;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopResultData {

    @SerializedName("facets")
    @Expose
    public Object facets;
    @SerializedName("meta")
    @Expose
    public Meta meta;
    @SerializedName("shops")
    @Expose
    public List<Shop> shops = null;

}

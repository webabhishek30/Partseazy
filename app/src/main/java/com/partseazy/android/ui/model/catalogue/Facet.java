package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by taushif on 17/1/17.
 */

public class Facet {
    @SerializedName("buckets")
    @Expose
    public List<Bucket> buckets = null;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("max")
    @Expose
    public int max;
    @SerializedName("min")
    @Expose
    public int min;

    public boolean isSelected;
}

package com.partseazy.android.ui.model.home.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naveen Kumar on 25/1/17.
 */

public class DataItem {
    @SerializedName("src")
    @Expose
    public String src;
    @SerializedName("page")
    @Expose
    public String page;
    @SerializedName("ref_id")
    @Expose
    public Integer refId;
    @SerializedName("params")
    @Expose
    public List<Param> params = null;
    @SerializedName("label")
    @Expose
    public String label;
}

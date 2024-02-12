package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by taushif on 17/1/17.
 */

public class Meta {
    @SerializedName("facet")
    @Expose
    public int facet;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("no_of_result")
    @Expose
    public String noOfResult;
    @SerializedName("page")
    @Expose
    public int page;
    @SerializedName("result")
    @Expose
    public int result;
    @SerializedName("rows")
    @Expose
    public int rows;
    @SerializedName("sort")
    @Expose
    public String sort;
}

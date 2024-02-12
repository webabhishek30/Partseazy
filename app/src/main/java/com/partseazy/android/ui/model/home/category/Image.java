package com.partseazy.android.ui.model.home.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class Image {
    @SerializedName("page")
    @Expose
    public String page;
    @SerializedName("pageid")
    @Expose
    public Integer pageid;
    @SerializedName("params")
    @Expose
    public Object params;
    @SerializedName("src")
    @Expose
    public String src;
}

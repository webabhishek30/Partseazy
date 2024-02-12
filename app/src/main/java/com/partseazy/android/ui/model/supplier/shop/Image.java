package com.partseazy.android.ui.model.supplier.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 6/9/17.
 */

public class Image {

    @SerializedName("file_id")
    @Expose
    public Integer fileId;
    @SerializedName("height")
    @Expose
    public Integer height;
    @SerializedName("src")
    @Expose
    public String src;
    @SerializedName("width")
    @Expose
    public Integer width;
}

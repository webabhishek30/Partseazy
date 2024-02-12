package com.partseazy.android.ui.model.deal.subcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 4/5/17.
 */

public class Image {
    @SerializedName("file_id")
    @Expose
    public Integer fileId;
    @SerializedName("src")
    @Expose
    public String src;

}

package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by taushif on 20/1/17.
 */

public class Image {
    @SerializedName("file_id")
    @Expose
    public int fileId;
    @SerializedName("src")
    @Expose
    public String src;
}

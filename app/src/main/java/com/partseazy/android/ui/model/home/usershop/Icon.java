package com.partseazy.android.ui.model.home.usershop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class Icon {
    @SerializedName("file_id")
    @Expose
    public Integer fileId;
    @SerializedName("src")
    @Expose
    public String src;
}

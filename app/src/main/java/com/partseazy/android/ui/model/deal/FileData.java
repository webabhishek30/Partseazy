package com.partseazy.android.ui.model.deal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 9/5/17.
 */

public class FileData {

    @SerializedName("file_id")
    @Expose
    public Integer fileId;
    @SerializedName("src")
    @Expose
    public String src;
}


package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Icon {

    @SerializedName("file_id")
    @Expose
    public Integer fileId;
    @SerializedName("src")
    @Expose
    public String src;

}

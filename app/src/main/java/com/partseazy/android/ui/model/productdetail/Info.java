
package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("seg_hi")
    @Expose
    public Integer segHi;
    @SerializedName("seg_lo")
    @Expose
    public Integer segLo;
    @SerializedName("seg_md")
    @Expose
    public Integer segMd;

}


package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bag {

    @SerializedName("mem.type")
    @Expose
    public String memType;
    @SerializedName("osproc.os")
    @Expose
    public String osprocOs;
    @SerializedName("pdetail.color")
    @Expose
    public String pdetailColor;
    @SerializedName("pdetail.length")
    @Expose
    public Integer pdetailLength;
    @SerializedName("pdetail.model")
    @Expose
    public String pdetailModel;
    @SerializedName("pdetail.weight")
    @Expose
    public Integer pdetailWeight;
    @SerializedName("pdetail.width")
    @Expose
    public Integer pdetailWidth;

}

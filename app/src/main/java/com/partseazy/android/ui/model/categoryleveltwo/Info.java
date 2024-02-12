package com.partseazy.android.ui.model.categoryleveltwo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 20/12/16.
 */
public class Info {

    @SerializedName("seg_upper")
    @Expose
    public Integer segHi;

    @SerializedName("seg_economic")
    @Expose
    public Integer segLo;

    @SerializedName("seg_middle")
    @Expose
    public Integer segMd;

}

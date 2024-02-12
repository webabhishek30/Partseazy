package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by taushif on 17/1/17.
 */

public class Bucket {

    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("count")
    @Expose
    public int count;

    public boolean isSelected;
}

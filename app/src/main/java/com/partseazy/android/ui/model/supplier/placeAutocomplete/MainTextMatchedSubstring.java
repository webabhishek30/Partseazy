package com.partseazy.android.ui.model.supplier.placeAutocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 6/9/17.
 */

public class MainTextMatchedSubstring {
    @SerializedName("length")
    @Expose
    public Integer length;
    @SerializedName("offset")
    @Expose
    public Integer offset;
}

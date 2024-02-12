package com.partseazy.android.ui.model.deal.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 1/9/17.
 */

public class FilterValue {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;

    public boolean isSelected;


}

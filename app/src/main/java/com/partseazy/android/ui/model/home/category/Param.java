package com.partseazy.android.ui.model.home.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class Param {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("value")
    @Expose
    public List<String> value = null;
}

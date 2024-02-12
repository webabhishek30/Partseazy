package com.partseazy.android.ui.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by taushif on 17/1/17.
 */

public class CategoryPath {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("level")
    @Expose
    public int level;
    @SerializedName("name")
    @Expose
    public String name;
}

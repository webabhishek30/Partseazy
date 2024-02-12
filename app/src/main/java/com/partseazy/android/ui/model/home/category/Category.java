package com.partseazy.android.ui.model.home.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class Category {
    @SerializedName("image")
    @Expose
    public Image image;
    @SerializedName("name")
    @Expose
    public String name;
}

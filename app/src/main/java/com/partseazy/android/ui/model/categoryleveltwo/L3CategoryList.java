package com.partseazy.android.ui.model.categoryleveltwo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 20/12/16.
 */
public class L3CategoryList {

    @SerializedName("result")
    @Expose
    public List<Category> result = null;

}

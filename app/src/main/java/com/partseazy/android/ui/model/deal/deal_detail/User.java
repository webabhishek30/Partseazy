package com.partseazy.android.ui.model.deal.deal_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 25/5/17.
 */

public class User {


    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("rating")
    @Expose
    public Double rating;
    @SerializedName("id")
    @Expose
    public Integer id;
}

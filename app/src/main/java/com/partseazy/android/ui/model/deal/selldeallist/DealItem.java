
package com.partseazy.android.ui.model.deal.selldeallist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealItem {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("info")
    @Expose
    public Info info;

    @SerializedName("promotion")
    @Expose
    public String promotion;


    @SerializedName("ending_at")
    @Expose
    public String endingAt;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("opens")
    @Expose
    public Integer opens;
    @SerializedName("skus")
    @Expose
    public List<Sku> skus = null;
    @SerializedName("starting_at")
    @Expose
    public String startingAt;
    @SerializedName("supplier")
    @Expose
    public String supplier;
    @SerializedName("views")
    @Expose
    public Integer views;

    @SerializedName("score")
    @Expose
    public String score;

    @SerializedName("active")
    @Expose
    public Integer active;



}

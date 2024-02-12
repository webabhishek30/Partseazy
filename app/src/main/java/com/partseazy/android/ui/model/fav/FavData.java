package com.partseazy.android.ui.model.fav;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Adeptical Solutions on 1/12/2017.
 */

public class FavData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("margin")
    @Expose
    public long margin;
    @SerializedName("max_qty")
    @Expose
    public Integer maxQty;
    @SerializedName("min_qty")
    @Expose
    public Integer minQty;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("price_high")
    @Expose
    public Integer priceHigh;
    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("category_id")
    @Expose
    public Integer categoryId;

}

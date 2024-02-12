package com.partseazy.android.ui.model.home.autosuggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class AutoSuggesstionKeywordData {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("productId")
    @Expose
    private String productId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}


package com.partseazy.android.ui.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bag {

    @SerializedName("pdetail.length")
    @Expose
    public Integer pdetailLength;
    @SerializedName("pdetail.width")
    @Expose
    public Integer pdetailWidth;

}

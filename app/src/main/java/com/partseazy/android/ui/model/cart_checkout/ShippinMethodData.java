package com.partseazy.android.ui.model.cart_checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by can on 29/12/16.
 */

public class ShippinMethodData {

    @SerializedName("charge")
    @Expose
    public Integer charge;
    @SerializedName("type")
    @Expose
    public String type;

    public int chooseOption;
    public int getChooseOption() {
        return chooseOption;
    }

    public void setChooseOption(int chooseOption) {
        this.chooseOption = chooseOption;
    }
}

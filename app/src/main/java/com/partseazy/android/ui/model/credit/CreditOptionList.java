package com.partseazy.android.ui.model.credit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Adeptical Solutions on 1/2/2017.
 */

public class CreditOptionList {

    @SerializedName("days")
    @Expose
    public Integer days;
    @SerializedName("interest")
    @Expose
    public Double interest;
    private int chooseOption;

    public int getChooseOption() {
        return chooseOption;
    }

    public void setChooseOption(int chooseOption) {
        this.chooseOption = chooseOption;
    }


}

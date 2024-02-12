package com.partseazy.android.ui.model.prepaid;

/**
 * Created by naveen on 2/3/17.
 */

public class ShippingRadioData {
    private String option;

    public String methodId;

    int chooseOption;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getChooseOption() {
        return chooseOption;
    }

    public void setChooseOption(int chooseOption) {
        this.chooseOption = chooseOption;
    }
}

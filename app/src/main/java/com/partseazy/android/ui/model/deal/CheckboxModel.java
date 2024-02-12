package com.partseazy.android.ui.model.deal;

/**
 * Created by naveen on 2/5/17.
 */

public class CheckboxModel {

    public String key;
    public String  value;
    public boolean isSelected;

    public CheckboxModel(){}

    public CheckboxModel(String key,String value){
        this.key = key;
        this.value = value;
    }
}

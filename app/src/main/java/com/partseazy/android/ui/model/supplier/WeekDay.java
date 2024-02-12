package com.partseazy.android.ui.model.supplier;

/**
 * Created by naveen on 14/4/17.
 */

public class WeekDay {

    public String key;
    public String value;
    public boolean isSelected;

    public WeekDay() {
    }

    public WeekDay(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public WeekDay(String key, String value, boolean isSelected) {
        this.key = key;
        this.value = value;
        this.isSelected = isSelected;
    }
}

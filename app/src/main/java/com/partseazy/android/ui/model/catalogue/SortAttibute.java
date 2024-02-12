package com.partseazy.android.ui.model.catalogue;

/**
 * Created by taushif on 19/1/17.
 */

public class SortAttibute {
    public int id;
    public boolean isSelected;
    public String sortCode;
    public String sortName;


    public SortAttibute (String sortCode, String sortName)
    {
        this.sortCode = sortCode;
        this.sortName = sortName;
    }
    public SortAttibute (String sortCode, String sortName, boolean isSelected)
    {
        this.sortCode = sortCode;
        this.sortName = sortName;
        this.isSelected = isSelected;
    }
}
